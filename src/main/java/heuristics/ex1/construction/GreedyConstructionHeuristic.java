package heuristics.ex1.construction;

import heuristics.ex1.dto.Graph;
import heuristics.ex1.dto.Solution;

import java.util.LinkedList;
import java.util.List;

public class GreedyConstructionHeuristic implements ConstructionHeuristic {

    @Override
    public Solution solve(Graph graph) {

        int node1 = graph.getMatrix().keySet().iterator().next();
        int node2 = graph.getAdjacentNodes(node1).keySet().iterator().next();
        int objectiveValue = graph.getWeight(node1, node2);

        // find first edge
        for (Integer n1 : graph.getMatrix().keySet()) {
            for (Integer n2 : graph.getAdjacentNodes(n1).keySet()) {
                int weight = graph.getWeight(n1, n2);
                if (Math.abs(weight) < Math.abs(objectiveValue)) {
                    node1 = n1;
                    node2 = n2;
                    objectiveValue = weight;
                }
                if (objectiveValue == 0) {
                    break;
                }
            }
            if (objectiveValue == 0) {
                break;
            }
        }


        List<Integer> selectedNodes = new LinkedList<>();


        return new Solution(selectedNodes);
    }
}
