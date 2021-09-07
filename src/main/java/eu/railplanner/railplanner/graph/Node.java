package eu.railplanner.railplanner.graph;

import lombok.Data;

import java.util.SortedSet;
import java.util.TreeSet;

@Data
public class Node {
    private final String name;

    private SortedSet<Edge> edges = new TreeSet<>();

    public void addEdge(Edge edge){
        this.edges.add(edge);
    }
}
