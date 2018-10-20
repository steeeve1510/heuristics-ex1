package heuristics.ex1.construction;

import heuristics.ex1.dto.Graph;
import heuristics.ex1.dto.Solution;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.*;

public class GreedyConstructionHeuristic implements ConstructionHeuristic {

    @Override
    public Solution solve(Graph graph) {

        int firstNode = graph.getMatrix().keySet().iterator().next();
        int lastNode = graph.getAdjacentNodes(firstNode).keySet().iterator().next();
        int objectiveValue = graph.getWeight(firstNode, lastNode);

        // find first edge
        for (Integer n1 : graph.getMatrix().keySet()) {
            for (Integer n2 : graph.getAdjacentNodes(n1).keySet()) {
                int weight = graph.getWeight(n1, n2);
                if (Math.abs(weight) < Math.abs(objectiveValue)) {
                    firstNode = n1;
                    lastNode = n2;
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


        Deque<Integer> selectedNodes = new LinkedList<>();
        selectedNodes.addFirst(firstNode);
        selectedNodes.addLast(lastNode);


        while (true) {
            Set<Integer> firstNeighbors = graph.getAdjacentNodes(firstNode).keySet();
            Set<Integer> lastNeighbors = graph.getAdjacentNodes(lastNode).keySet();

            firstNeighbors = new HashSet<>(firstNeighbors);
            lastNeighbors = new HashSet<>(lastNeighbors);

            firstNeighbors.removeAll(selectedNodes);
            lastNeighbors.removeAll(selectedNodes);

            if (firstNeighbors.isEmpty()) {
                break;
            }

            List<Extension> extensions = new LinkedList<>();

            firstNode = selectedNodes.getFirst();
            for (int neighbor : firstNeighbors) {
                int weight = graph.getWeight(firstNode, neighbor);
                extensions.add(new Extension(firstNode, neighbor, objectiveValue + weight));
            }
            lastNode = selectedNodes.getLast();
            for (int neighbor : lastNeighbors) {
                int weight = graph.getWeight(lastNode, neighbor);
                extensions.add(new Extension(lastNode, neighbor, objectiveValue + weight));
            }
            extensions.sort(Comparator.comparingInt(o -> Math.abs(o.getNewObjectiveValue())));


            Extension best = ((LinkedList<Extension>) extensions).getFirst();
            int from = best.getFrom();
            if (from == firstNode) {
                selectedNodes.addFirst(best.getTo());
                firstNode = best.getTo();
            } else {
                selectedNodes.addLast(best.getTo());
                lastNode = best.getTo();
            }
            objectiveValue = best.getNewObjectiveValue();
        }

        objectiveValue +=graph.getWeight(selectedNodes.getFirst(), selectedNodes.getLast());
        return new Solution(new LinkedList<>(selectedNodes), objectiveValue);
    }

    @Getter
    @AllArgsConstructor
    private static class Extension {
        private int from;
        private int to;
        private int newObjectiveValue;
    }
}
