package eu.railplanner.server.graph;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.util.Assert;

@Data
public class Edge implements Comparable<Edge> {
    private Node start;

    private Node end;

    private long departure;

    private long duration;

    public void setDuration(long duration){
        Assert.isTrue(duration > 0, () -> String.format("In a Graph, duration of an Edge should always be positive (got: %d)", duration));
        this.duration = duration;
    }

    @Override
    public int compareTo(Edge other) {
        return (int) (this.getDeparture() - other.getDeparture());
    }

    @Override
    public String toString() {
        return String.format("%d->%d (dep=%d dur=%d)", start.getId(), end.getId(), departure, duration);
    }
}
