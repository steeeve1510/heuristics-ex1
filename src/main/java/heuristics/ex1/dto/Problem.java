package heuristics.ex1.dto;

import lombok.Getter;
import lombok.ToString;

import java.util.*;

@Getter
@ToString
public class Problem {

    private List<Edge> edges;
    private Map<Integer, Set<Edge>> edgesByNodes;

    private int numberOfEdges;
    private int numberOfNodes;

    public Problem(List<Edge> edges) {
        this.edges = edges;
        this.edgesByNodes = getEdgesByNodes(edges);

        this.numberOfEdges = edges.size();
        this.numberOfNodes = edgesByNodes.keySet().size();
    }

    private Map<Integer, Set<Edge>> getEdgesByNodes(List<Edge> edges) {
        Map<Integer, Set<Edge>> edgesByNodes = new HashMap<>();

        for(Edge edge : edges) {
            edgesByNodes.putIfAbsent(edge.getNode1(), new HashSet<>());
            edgesByNodes.get(edge.getNode1()).add(edge);

            edgesByNodes.putIfAbsent(edge.getNode2(), new HashSet<>());
            edgesByNodes.get(edge.getNode2()).add(edge);
        }

        return edgesByNodes;
    }
}
