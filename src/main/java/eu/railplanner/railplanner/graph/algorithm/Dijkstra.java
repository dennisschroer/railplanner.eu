package eu.railplanner.railplanner.graph.algorithm;

import eu.railplanner.railplanner.graph.Edge;
import eu.railplanner.railplanner.graph.Graph;
import eu.railplanner.railplanner.graph.Node;
import lombok.Data;
import lombok.extern.apachecommons.CommonsLog;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@CommonsLog
public class Dijkstra {

    @Data
    private static class NodeArrival {
        private final Edge incomingEdge;
        private final int arrival;
    }

    public LinkedList<Edge> computeEarliestArrival(Graph graph, int startTime, Node startNode, Node destinationNode) {
        // Map of node to object describing how to reach it and the earliest arrival
        Map<Node, NodeArrival> arrivals = new HashMap<>();
        Set<Node> unsettledNodes = new HashSet<>(graph.getNodes());

        // Initialize all nodes to infinity, and startnode at startTime.
        graph.getNodes().forEach(node -> arrivals.put(node, new NodeArrival(null, Integer.MAX_VALUE)));
        arrivals.put(startNode, new NodeArrival(null, startTime));

        while (!unsettledNodes.isEmpty()) {
            Node node = getLowestDistanceNode(arrivals, unsettledNodes);
            int arrival = arrivals.get(node).getArrival();
            log.info(String.format("Visiting node %d with current arrival %s", node.getId(), arrival));

            int newArrival;

            for (Edge edge : node.getEdges()) {
                // Check if edge is possible
                if (edge.getDeparture() >= arrival) {
                    // Check if edge makes route shorter
                    newArrival = edge.getDeparture() + edge.getDuration();

                    if (newArrival < arrivals.get(edge.getEnd()).getArrival()) {
                        // Yaj it is faster
                        arrivals.put(edge.getEnd(), new NodeArrival(edge, newArrival));

                        log.info(String.format("Faster arrival to %d via %d at %d",
                                edge.getEnd().getId(),
                                node.getId(), newArrival));
                    }
                }
            }

            unsettledNodes.remove(node);
        }

        // Now extract the shortest path
        LinkedList<Edge> path = new LinkedList<>();
        path.add(arrivals.get(destinationNode).getIncomingEdge());
        while (!path.getFirst().getStart().equals(startNode)) {
            path.addFirst(arrivals.get(path.getFirst().getStart()).getIncomingEdge());
        }

        log.info(String.format("The shortest path is: %s",
                path.stream().map(Edge::toString).collect(Collectors.toList())));

        return path;
    }

    private Node getLowestDistanceNode(Map<Node, NodeArrival> arrivals, Set<Node> unsettledNodes) {
        int minimumDistance = Integer.MAX_VALUE;
        Node result = null;

        for (Map.Entry<Node, NodeArrival> entry : arrivals.entrySet()) {
            if (unsettledNodes.contains(entry.getKey()) && (result == null || entry.getValue().getArrival() < minimumDistance)) {
                result = entry.getKey();
                minimumDistance = entry.getValue().getArrival();
            }
        }

        return result;
    }
}
