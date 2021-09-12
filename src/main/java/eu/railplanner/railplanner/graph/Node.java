package eu.railplanner.railplanner.graph;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.SortedSet;
import java.util.TreeSet;

@Data
@EqualsAndHashCode(exclude = "edges")
public class Node {
    private final Long id;

    private SortedSet<Edge> edges = new TreeSet<>();

    public void addEdge(Edge edge){
        this.edges.add(edge);
    }
}
