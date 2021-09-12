package eu.railplanner.railplanner.graph;

import lombok.Builder;
import lombok.Data;

import java.util.LinkedList;

@Data
public class Route {
    private final LinkedList<Edge> path;
}
