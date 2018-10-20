package heuristics.ex1.construction;

import heuristics.ex1.dto.Graph;
import heuristics.ex1.dto.Solution;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.*;

public class GreedyConstructionHeuristic implements ConstructionHeuristic {

    @Override
    public Solution solve(Graph graph) {

        Extension firstEdge = getFirstEdge(graph);

        int firstNode = firstEdge.getFrom();
        int lastNode = firstEdge.getTo();
        int objectiveValue = firstEdge.getNewObjectiveValue();

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


            Extension bestExtension = new Extension(0, 0, Integer.MAX_VALUE);
            int bestAbsObjectiveValue = bestExtension.getAbsNewObjectiveValue();

            for (int neighbor : firstNeighbors) {
                int weight = graph.getWeight(firstNode, neighbor);
                if (Math.abs(objectiveValue + weight) < bestAbsObjectiveValue) {
                    bestExtension = new Extension(firstNode, neighbor, objectiveValue + weight);;
                    bestAbsObjectiveValue = bestExtension.getAbsNewObjectiveValue();
                }
            }

            for (int neighbor : lastNeighbors) {
                int weight = graph.getWeight(lastNode, neighbor);
                if (Math.abs(objectiveValue + weight) < bestAbsObjectiveValue) {
                    bestExtension = new Extension(lastNode, neighbor, objectiveValue + weight);;
                    bestAbsObjectiveValue = bestExtension.getAbsNewObjectiveValue();
                }
            }


            int from = bestExtension.getFrom();
            if (from == firstNode) {
                selectedNodes.addFirst(bestExtension.getTo());
                firstNode = bestExtension.getTo();
            } else {
                selectedNodes.addLast(bestExtension.getTo());
                lastNode = bestExtension.getTo();
            }
            objectiveValue = bestExtension.getNewObjectiveValue();
        }

        objectiveValue += graph.getWeight(selectedNodes.getFirst(), selectedNodes.getLast());

        return new Solution(new LinkedList<>(selectedNodes), objectiveValue);
    }

    private Extension getFirstEdge(Graph graph) {
        int firstNode = graph.getMatrix().keySet().iterator().next();
        int lastNode = graph.getAdjacentNodes(firstNode).keySet().iterator().next();
        int objectiveValue = graph.getWeight(firstNode, lastNode);

        Integer[] nodes = graph.getMatrix().keySet().toArray(new Integer[0]);

        for (Integer iN1 = 0; iN1 < nodes.length; iN1++) {
            for (Integer iN2 = iN1+1; iN2 < nodes.length; iN2++) {
                int n1 = nodes[iN1];
                int n2 = nodes[iN2];
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

        return new Extension(firstNode, lastNode, objectiveValue);
    }

    @Getter
    @AllArgsConstructor
    private static class Extension {
        private int from;
        private int to;
        private int newObjectiveValue;

        public int getAbsNewObjectiveValue() {
            return Math.abs(newObjectiveValue);
        }
    }
}
