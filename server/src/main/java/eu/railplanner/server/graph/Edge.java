package eu.railplanner.server.graph;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Edge implements Comparable<Edge> {
    private Node start;

    private Node end;

    private int departure;

    private int duration;

    @Override
    public int compareTo(Edge other) {
        return this.getDeparture() - other.getDeparture();
    }

    @Override
    public String toString() {
        return String.format("%d->%d (dep=%d dur=%d)", start.getId(), end.getId(), departure, duration);
    }
}
