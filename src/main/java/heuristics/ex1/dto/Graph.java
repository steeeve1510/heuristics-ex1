package heuristics.ex1.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

@ToString
@Getter
@AllArgsConstructor
public class Graph {

    private Map<Integer, Map<Integer, Integer>> matrix;

    public Map<Integer, Integer> getAdjacentNodes(int node) {
        return matrix.get(node);
    }

    public Integer getWeight(int node1, int node2) {
        return matrix.get(node1).get(node2);
    }
}
