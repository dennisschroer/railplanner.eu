package eu.railplanner.server.controller;

import eu.railplanner.core.model.Station;
import eu.railplanner.core.model.timetable.Connection;
import eu.railplanner.core.repository.StationRespository;
import eu.railplanner.core.repository.timetable.ConnectionRepository;
import eu.railplanner.core.repository.timetable.TripRepository;
import eu.railplanner.server.graph.Edge;
import eu.railplanner.server.graph.Graph;
import eu.railplanner.server.graph.Node;
import eu.railplanner.server.graph.algorithm.Dijkstra;
import eu.railplanner.server.graph.algorithm.NoShortestPathFoundException;
import eu.railplanner.server.response.ConnectionResponse;
import eu.railplanner.server.response.JourneyResponse;
import eu.railplanner.server.response.StationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/journey")
public class JourneyController {

    @Autowired
    private ConnectionRepository connectionRepository;

    @Autowired
    private StationRespository stationRespository;

    @Autowired
    private TripRepository tripRepository;

    @GetMapping("/earliest-arrival")
    public ResponseEntity<JourneyResponse> earliestArrival(
            @RequestParam(defaultValue = "8400212") String startUic,
            @RequestParam(defaultValue = "8400293") String destinationUic,
            @RequestParam(defaultValue = "2021-12-01T10:00:00.000") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime departure) {

        // Check existence of stations
        Station start = stationRespository.findByUicCode(startUic).orElseThrow(() -> new IllegalArgumentException("startUic"));
        Station destination = stationRespository.findByUicCode(destinationUic).orElseThrow(() -> new IllegalArgumentException("destinationUic"));

        // Determine time zone
        ZoneId timezone = start.getTimezone();

        // Determine start time in UTC
        Instant utcStartTime = departure.atZone(timezone).toInstant();

        // Get all valid connections
        List<Connection> connections = connectionRepository.findAllValidConnections(utcStartTime, (short) 240);

        // Build graph
        Graph graph = new Graph();
        Node startNode = addStationToGraph(graph, start);
        Node destinationNode = addStationToGraph(graph, destination);
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

        // Apply algorithm
        // TODO handle case where there is no journey with the current window
        // TODO handle case where there are disconnected nodes
        Dijkstra dijkstra = new Dijkstra();
        LinkedList<Edge> path = null;
        try {
            path = dijkstra.computeEarliestArrival(graph, utcStartTime.getEpochSecond(), startNode, destinationNode);
        } catch (NoShortestPathFoundException e) {
            // No journey found
        }

        // Create response
        JourneyResponse journey = new JourneyResponse();
        Set<Long> stationIds = new HashSet<>();
        if (path != null) {
            for (Edge edge : path) {
                ConnectionResponse connection = new ConnectionResponse();
                connection.setStartId(edge.getStart().getId());
                connection.setEndId(edge.getEnd().getId());
                connection.setDeparture(Instant.ofEpochSecond(edge.getDeparture()).atZone(timezone));
                connection.setArrival(connection.getDeparture().plus(edge.getDuration(), ChronoUnit.SECONDS));
                journey.getJourney().add(connection);

                stationIds.add(edge.getStart().getId());
                stationIds.add(edge.getEnd().getId());
            }
        }
        journey.getStations().addAll(stationRespository.findAllById(stationIds).stream().map(station -> {
                    StationResponse stationResponse = new StationResponse();
                    stationResponse.setId(station.getId());
                    stationResponse.setName(station.getName());
                    stationResponse.setUicCode(station.getUicCode());
                    stationResponse.setCountry(station.getCountry());
                    return stationResponse;
                }
        ).collect(Collectors.toList()));

        // return response
        return ResponseEntity.ok(journey);
    }

    private Node addStationToGraph(Graph graph, Station station) {
        return graph.getNodes().stream()
                .filter(node -> node.getId().equals(station.getId()))
                .findFirst()
                .orElseGet(() -> {
                    Node node = new Node(station.getId());
                    graph.getNodes().add(node);
                    return node;
                });
    }
}
