package eu.railplanner.server.graph;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Graph {
    private List<Node> nodes = new ArrayList<>();

    public void addNode(Node node) {
        this.nodes.add(node);
    }
}
