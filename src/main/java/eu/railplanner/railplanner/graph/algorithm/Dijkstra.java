package eu.railplanner.railplanner.graph.algorithm;

import eu.railplanner.railplanner.graph.Edge;
import eu.railplanner.railplanner.graph.Graph;
import eu.railplanner.railplanner.graph.Node;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.data.util.Pair;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@CommonsLog
public class Dijkstra {

    public static void main(String[] args) {
        Node a = new Node("A");
        Node b = new Node("B");
        Node c = new Node("C");
        Node d = new Node("D");
        Node e = new Node("E");

        a.addEdge(new Edge(100, 10, b));
        a.addEdge(new Edge(120, 5, c));
        b.addEdge(new Edge(110, 10, d));
        c.addEdge(new Edge(130, 10, e));
        d.addEdge(new Edge(120, 10, e));

        Graph graph = new Graph();
        graph.addNode(a);
        graph.addNode(b);
        graph.addNode(c);
        graph.addNode(d);
        graph.addNode(e);

        Dijkstra dijkstra = new Dijkstra();
        dijkstra.computeShortestPath(graph, 90, a, e);
    }

    private void computeShortestPath(Graph graph, int startTime, Node startNode, Node destinationNode) {
        // Map of node to pair of previous node and arrivaltime
        Map<Node, Pair<Node, Integer>> arrivals = new HashMap<>();
        Set<Node> unsettledNodes = new HashSet<>(graph.getNodes());

        graph.getNodes().forEach(node -> arrivals.put(node, Pair.of(startNode, Integer.MAX_VALUE)));
        arrivals.put(startNode, Pair.of(startNode, startTime));

        while (!unsettledNodes.isEmpty()) {
            Node node = getLowestDistanceNode(arrivals, unsettledNodes);
            int arrival = arrivals.get(node).getSecond();
            log.info(String.format("Visiting node %s with current arrival %s", node.getName(), arrival));

            int newArrival;

            for (Edge edge : node.getEdges()) {
                // Check if edge is possible
                if (edge.getTime() >= arrival) {
                    // Check if edge makes route shorter
                    newArrival = edge.getTime() + edge.getCost();

                    if (newArrival < arrivals.get(edge.getDestination()).getSecond()) {
                        // Yaj it is faster
                        arrivals.put(edge.getDestination(), Pair.of(node, newArrival));

                        log.info(String.format("Faster arrival to %s via %s at %d",
                                edge.getDestination().getName(),
                                node.getName(), newArrival));
                    }
                }
            }

            unsettledNodes.remove(node);
        }

        log.info(arrivals);
    }

    private Node getLowestDistanceNode(Map<Node, Pair<Node, Integer>> arrivals, Set<Node> unsettledNodes) {
        int minimumDistance = Integer.MAX_VALUE;
        Node result = null;

        for (Map.Entry<Node, Pair<Node, Integer>> entry : arrivals.entrySet()) {
            if (unsettledNodes.contains(entry.getKey()) && (result == null || entry.getValue().getSecond() < minimumDistance)) {
                result = entry.getKey();
                minimumDistance = entry.getValue().getSecond();
            }
        }

        return result;
    }
}
