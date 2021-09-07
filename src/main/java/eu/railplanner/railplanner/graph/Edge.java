package eu.railplanner.railplanner.graph;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Edge implements Comparable<Edge> {
    private int time;

    private int cost;

    private Node destination;

    @Override
    public int compareTo(Edge other) {
        return this.getTime() - other.getTime();
    }
}
