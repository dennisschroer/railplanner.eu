package eu.railplanner.railplanner.graph.algorithm;

import eu.railplanner.railplanner.graph.Edge;
import eu.railplanner.railplanner.graph.Graph;
import eu.railplanner.railplanner.graph.Node;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DijkstraTest {
    @Test
    public void testComputeShortestPath() {
        Node a = new Node(1L);
        Node b = new Node(2L);
        Node c = new Node(3L);
        Node d = new Node(4L);
        Node e = new Node(5L);

        a.addEdge(new Edge(a, b, 100, 10));
        a.addEdge(new Edge(a, c, 120, 5));
        b.addEdge(new Edge(b, d, 110, 10));
        c.addEdge(new Edge(c, e, 130, 10));
        d.addEdge(new Edge(d, e, 120, 10));

        Graph graph = new Graph();
        graph.addNode(a);
        graph.addNode(b);
        graph.addNode(c);
        graph.addNode(d);
        graph.addNode(e);

        Dijkstra dijkstra = new Dijkstra();
        LinkedList<Edge> path = dijkstra.computeEarliestArrival(graph, 90, a, e);

        assertEquals(3, path.size());
        Edge first = path.get(0);
        Edge second = path.get(1);
        Edge third = path.get(2);

        assertEquals(a, first.getStart());
        assertEquals(b, first.getEnd());
        assertEquals(100, first.getDeparture());
        assertEquals(10, first.getDuration());
        assertEquals(b, second.getStart());
        assertEquals(d, second.getEnd());
        assertEquals(110, second.getDeparture());
        assertEquals(10, second.getDuration());
        assertEquals(d, third.getStart());
        assertEquals(e, third.getEnd());
        assertEquals(120, third.getDeparture());
        assertEquals(10, third.getDuration());
    }
}