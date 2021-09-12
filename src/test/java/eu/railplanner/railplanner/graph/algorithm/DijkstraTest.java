package eu.railplanner.railplanner.graph.algorithm;

import eu.railplanner.railplanner.graph.Edge;
import eu.railplanner.railplanner.graph.Graph;
import eu.railplanner.railplanner.graph.Node;
import eu.railplanner.railplanner.graph.Route;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DijkstraTest {
    @Test
    public void testComputeShortestPath() {
        Node a = new Node("A");
        Node b = new Node("B");
        Node c = new Node("C");
        Node d = new Node("D");
        Node e = new Node("E");

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
        Route route = dijkstra.computeEarliestArrival(graph, 90, a, e);

        assertEquals(3, route.getPath().size());
        Edge first = route.getPath().get(0);
        Edge second = route.getPath().get(1);
        Edge third = route.getPath().get(2);

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