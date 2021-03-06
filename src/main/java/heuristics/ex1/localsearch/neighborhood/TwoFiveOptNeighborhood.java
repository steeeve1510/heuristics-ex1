package heuristics.ex1.localsearch.neighborhood;

import heuristics.ex1.dto.Graph;
import heuristics.ex1.dto.Solution;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
This class implements the Node Insertion Move  neighbourhood, commonly referred as the 2.5 Opt
 It selects a node and a non-adjacent edge. It removes the chosen node from its original position and places it between
 the end points of the selected edge.
 As described in: On the Neighborhood Structure of the Traveling Salesman Problem Generated by Local Search Moves
*/


public class TwoFiveOptNeighborhood implements Neighborhood {

    @Override
    public Solution get(Solution solution, Graph graph, StepType stepType) {
        switch (stepType) {
            case RANDOM:
                return getRandomNeighbor(solution, graph);
            case NEXT_IMPROVEMENT:
                return getNextImprovement(solution, graph);
            case BEST_IMPOVEMENT:
                return getBestImprovement(solution, graph);
        }
        return null;
    }

    private Solution getBestImprovement(Solution solution, Graph graph) {
        int size = solution.getSize();

        Solution bestSolution = solution;

        for (int i = 0; i < size; i++) {
            for (int j = i+1; j < size + i - 1; j++) {
                int node1 = solution.get(i);
                int node2 = solution.get(Math.floorMod(j, size));

                Solution neighbor = move(node1, node2, solution, graph);
                if (neighbor.getAbsoluteObjectiveValue() < bestSolution.getAbsoluteObjectiveValue()) {
                    bestSolution = neighbor;
                }
            }
        }

        if (bestSolution == solution) {
            return null;
        }
        return bestSolution;
    }

    private Solution getNextImprovement(Solution solution, Graph graph) {
        int size = solution.getSize();
        for (int i = 0; i < size; i++) {
            for (int j = i+1; j < size + i - 1; j++) {
                int node1 = solution.get(i);
                int node2 = solution.get(Math.floorMod(j, size));

                Solution neighbor = move(node1, node2, solution, graph);
                if (neighbor.getAbsoluteObjectiveValue() < solution.getAbsoluteObjectiveValue()) {
                    return neighbor;
                }
            }
        }
        return null;
    }

    private Solution getRandomNeighbor(Solution solution, Graph graph) {
        int size = solution.getSize();

        Random random = new Random();
        int node1Index = random.nextInt(size);
        int node2Index = random.nextInt(size); // node 1 will be inserted after this

        if (node1Index == node2Index) {
            node2Index ++;
        } else if (node1Index == node2Index+1 || (node1Index == 0 && node2Index == size-1)) {
            node2Index --;
        }

        int node1 = solution.get(node1Index);
        int node2 = solution.get(Math.floorMod(node2Index, size));

        return move(node1, node2, solution, graph);
    }

    private Solution move(int node1, int node2, Solution solution, Graph graph) {
        int node1Successor = solution.getSuccessor(node1);
        int node1Predecessor = solution.getPredecessor(node1);
        int node2Successor = solution.getSuccessor(node2);

        List<Integer> neighbor = getNeighbor(node1, node2, solution);

        long objectiveValue = solution.getObjectiveValue();

        long newObjectiveValue = getNewObjectiveValue(objectiveValue, node1, node1Successor, node1Predecessor, node2, node2Successor, graph);

        return new Solution(neighbor, newObjectiveValue);
    }

    private long getNewObjectiveValue(long objectiveValue, int node1, int node1Successor, int node1Predecessor, int node2, int node2Successor, Graph graph) {
        long oldWeight1 = graph.getWeight(node1, node1Successor);
        long oldWeight2 = graph.getWeight(node1, node1Predecessor);
        long oldWeight3 = graph.getWeight(node2, node2Successor);

        long newWeight2 = graph.getWeight(node1Predecessor, node1Successor);
        long newWeight1 = graph.getWeight(node2, node1);
        long newWeight3 = graph.getWeight(node1, node2Successor);

        return objectiveValue - oldWeight1 - oldWeight2 - oldWeight3 + newWeight1 + newWeight2 + newWeight3;
    }

    private List<Integer> getNeighbor(int node1, int node2, Solution solution) {
        //remove node 1, insert after node 2
        LinkedList<Integer> neighbour = new LinkedList<>(solution.getNodes());
        neighbour.remove((Integer) node1);
        int node2Index = neighbour.indexOf((Integer) node2);
        if (node2Index == neighbour.size()-1){
            neighbour.add(node1);
        } else {
            neighbour.add(node2Index+1, node1);
        }
        return neighbour;
    }
}
