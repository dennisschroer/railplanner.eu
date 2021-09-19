package eu.railplanner.server.controller;

import eu.railplanner.core.model.Station;
import eu.railplanner.server.graph.Edge;
import eu.railplanner.server.graph.Graph;
import eu.railplanner.server.graph.Node;
import eu.railplanner.server.graph.algorithm.Dijkstra;
import eu.railplanner.server.response.ConnectionResponse;
import eu.railplanner.server.response.JourneyResponse;
import eu.railplanner.server.response.StationResponse;
import eu.railplanner.core.model.timetable.Connection;
import eu.railplanner.core.repository.StationRespository;
import eu.railplanner.core.repository.timetable.ConnectionRepository;
import eu.railplanner.core.repository.timetable.TripRepository;
import eu.railplanner.core.repository.timetable.TripValidityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
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

    @Autowired
    private TripValidityRepository tripValidityRepository;

    @GetMapping("/earliest-arrival")
    public ResponseEntity<JourneyResponse> earliestArrival(
            @RequestParam(defaultValue = "8400212") String startUic,
            @RequestParam(defaultValue = "8400293") String destinationUic,
            @RequestParam(defaultValue = "2021-09-15") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(defaultValue = "600") short startTime) {

        // Check existence of stations
        Station start = stationRespository.findByUicCode(startUic).orElseThrow(() -> new IllegalArgumentException("startUic"));
        Station destination = stationRespository.findByUicCode(destinationUic).orElseThrow(() -> new IllegalArgumentException("destinationUic"));

        // Get all valid connections
        List<Connection> connections = connectionRepository.findAllValidConnections(date, startTime, (short) 240);

        // Build graph
        Graph graph = new Graph();
        Node startNode = addStationToGraph(graph, start);
        Node destinationNode = addStationToGraph(graph, destination);
        for (Connection connection : connections) {
            Node connectionStart = addStationToGraph(graph, connection.getStart());
            Node connectionEnd = addStationToGraph(graph, connection.getEnd());

            // TODO consider change time
            connectionStart.addEdge(new Edge(connectionStart, connectionEnd, connection.getDeparture(), connection.getArrival() - connection.getDeparture()));
        }

        // Apply algorithm
        // TODO handle case where there is no journey with the current window
        // TODO handle case where there are disconnected nodes
        Dijkstra dijkstra = new Dijkstra();
        LinkedList<Edge> path = dijkstra.computeEarliestArrival(graph, startTime, startNode, destinationNode);

        // Create response
        JourneyResponse journey = new JourneyResponse();
        Set<Long> stationIds = new HashSet<>();
        for (Edge edge : path) {
            ConnectionResponse connection = new ConnectionResponse();
            connection.setStartId(edge.getStart().getId());
            connection.setEndId(edge.getEnd().getId());
            connection.setDeparture(OffsetDateTime.of(date, LocalTime.of(edge.getDeparture() / 60, edge.getDeparture() % 60), ZoneOffset.UTC));
            connection.setArrival(connection.getDeparture().plus(edge.getDuration(), ChronoUnit.MINUTES));
            journey.getJourney().add(connection);

            stationIds.add(edge.getStart().getId());
            stationIds.add(edge.getEnd().getId());
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
