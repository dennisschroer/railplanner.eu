package eu.railplanner.server.controller;

import eu.railplanner.core.model.Station;
import eu.railplanner.core.model.timetable.Connection;
import eu.railplanner.core.repository.StationRespository;
import eu.railplanner.core.repository.timetable.ConnectionRepository;
import eu.railplanner.core.repository.timetable.TripRepository;
import eu.railplanner.server.api.JourneyApi;
import eu.railplanner.server.graph.Edge;
import eu.railplanner.server.graph.Graph;
import eu.railplanner.server.graph.Node;
import eu.railplanner.server.graph.algorithm.Dijkstra;
import eu.railplanner.server.graph.algorithm.NoShortestPathFoundException;
import eu.railplanner.server.model.Journey;
import eu.railplanner.server.model.JourneysCollection;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class JourneyController implements JourneyApi {

    /**
     * Time window in minutes for which all connections will be fetched and loaded in a graph.
     */
    public static final short CONNECTION_SEARCH_WINDOW = 360;

    @Autowired
    private ConnectionRepository connectionRepository;

    @Autowired
    private StationRespository stationRespository;

    @Override
    public ResponseEntity<JourneysCollection> earliestArrival(String startUic, String destinationUic, OffsetDateTime departure) {
        // Check existence of stations
        Station start = stationRespository.findByUicCode(startUic).orElseThrow(() -> new IllegalArgumentException("startUic"));
        Station destination = stationRespository.findByUicCode(destinationUic).orElseThrow(() -> new IllegalArgumentException("destinationUic"));

        // Collect all required data
        JourneySearchData journeySearchData = createJourneySearchData(start, destination, departure.toInstant());

        // Apply algorithm
        LinkedList<Edge> path = findPathWithEarliestArrival(journeySearchData);

        // Return response
        return ResponseEntity.ok(createJourneysCollection(path));
    }

    private JourneysCollection createJourneysCollection(LinkedList<Edge> path) {
        JourneysCollection response = new JourneysCollection();
        Journey journey = new Journey();
        response.addJourneysItem(journey);

        Set<Long> stationIds = new HashSet<>();
        if (path != null) {
            for (Edge edge : path) {
                eu.railplanner.server.model.Connection connection = new eu.railplanner.server.model.Connection();
                connection.setStartId(edge.getStart().getId());
                connection.setEndId(edge.getEnd().getId());
                connection.setDeparture(Instant.ofEpochSecond(edge.getDeparture()).atOffset(ZoneOffset.UTC));
                connection.setArrival(connection.getDeparture().plus(edge.getDuration(), ChronoUnit.SECONDS));
                journey.addConnectionsItem(connection);

                stationIds.add(edge.getStart().getId());
                stationIds.add(edge.getEnd().getId());
            }
        }

        response.setStations(stationRespository.findAllById(stationIds).stream().map(stationEntity -> {
                    eu.railplanner.server.model.Station station = new eu.railplanner.server.model.Station();
                    station.setId(stationEntity.getId());
                    station.setName(stationEntity.getName());
                    station.setUic(stationEntity.getUicCode());
                    station.setCountry(eu.railplanner.server.model.Station.CountryEnum.fromValue(stationEntity.getCountry().name()));
                    return station;
                }
        ).collect(Collectors.toList()));

        return response;
    }

    @Nonnull
    private JourneySearchData createJourneySearchData(@Nonnull Station start, @Nonnull Station destination, @Nonnull Instant utcStartTime) {
        JourneySearchData searchData = new JourneySearchData();

        // Build graph
        searchData.setGraph(new Graph());
        searchData.setStartNode(addStationToGraph(searchData.getGraph(), start));
        searchData.setDestinationNode(addStationToGraph(searchData.getGraph(), destination));
        searchData.setStartTime(utcStartTime);
        fillGraphWithConnections(searchData);

        return searchData;
    }

    private void fillGraphWithConnections(@Nonnull JourneySearchData journeySearchData) {
        // Get all valid connections
        List<Connection> connections = connectionRepository.findAllValidConnections(journeySearchData.getStartTime(), CONNECTION_SEARCH_WINDOW);

        // Add connections to graph
        Graph graph = journeySearchData.getGraph();
        for (Connection connection : connections) {
            Node connectionStart = addStationToGraph(graph, connection.getStart());
            Node connectionEnd = addStationToGraph(graph, connection.getEnd());

            // TODO consider change time
            Duration duration = Duration.between(connection.getDeparture(), connection.getArrival());
            Edge edge = new Edge();
            edge.setStart(connectionStart);
            edge.setEnd(connectionEnd);
            edge.setDeparture(connection.getDeparture().getEpochSecond());
            edge.setDuration(duration.toSeconds());
            connectionStart.addEdge(edge);
        }
    }

    @Nonnull
    private Node addStationToGraph(@Nonnull Graph graph, @Nonnull Station station) {
        return graph.getNodes().stream()
                .filter(node -> node.getId().equals(station.getId()))
                .findFirst()
                .orElseGet(() -> {
                    Node node = new Node(station.getId());
                    graph.getNodes().add(node);
                    return node;
                });
    }

    @Nullable
    private LinkedList<Edge> findPathWithEarliestArrival(@Nonnull JourneySearchData journeySearchData) {
        // TODO handle case where there are disconnected nodes
        Dijkstra dijkstra = new Dijkstra();
        try {
            return dijkstra.computeEarliestArrival(
                    journeySearchData.getGraph(),
                    journeySearchData.getStartTime().getEpochSecond(),
                    journeySearchData.getStartNode(),
                    journeySearchData.getDestinationNode());
        } catch (NoShortestPathFoundException e) {
            // No journey found
        }

        return null;
    }

    @Data
    static class JourneySearchData {
        private Graph graph;
        private Node startNode;
        private Node destinationNode;
        private Instant startTime;
    }
}
