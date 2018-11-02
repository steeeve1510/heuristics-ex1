package heuristics.ex1.localsearch.neighborhood;

import heuristics.ex1.dto.Graph;
import heuristics.ex1.dto.Solution;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TwoOptNeighborhood implements Neighborhood {

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
            for (int j = i+2; j < size + i - 1; j++) {
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
            for (int j = i+2; j < size + i - 1; j++) {
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
        int node2Index = random.nextInt(size);

        if (node1Index == node2Index) {
            node2Index += 2;
        } else if (node1Index+1 == node2Index || (node1Index == size-1 && node2Index == 0)) {
            node2Index ++;
        } else if (node1Index-1 == node2Index || (node1Index == 0 && node2Index == size-1)) {
            node2Index --;
        }

        int node1 = solution.get(node1Index);
        int node2 = solution.get(Math.floorMod(node2Index, size));

        return move(node1, node2, solution, graph);
    }

    private Solution move(int node1, int node2, Solution solution, Graph graph) {
        int node1Successor = solution.getSuccessor(node1);
        int node2Successor = solution.getSuccessor(node2);

        List<Integer> neighbor = getNeighbor(node1Successor, node2Successor, solution);

        int objectiveValue = solution.getObjectiveValue();

        int newObjectiveValue = getNewObjectiveValue(objectiveValue, node1, node1Successor, node2, node2Successor, graph);

        return new Solution(neighbor, newObjectiveValue);
    }

    private int getNewObjectiveValue(int objectiveValue, int node1, int node1Successor, int node2, int node2Successor, Graph graph) {
        int oldWeight1 = graph.getWeight(node1, node1Successor);
        int oldWeight2 = graph.getWeight(node2, node2Successor);

        int newWeight1 = graph.getWeight(node1, node2);
        int newWeight2 = graph.getWeight(node1Successor, node2Successor);

        return objectiveValue - oldWeight1 - oldWeight2 + newWeight1 + newWeight2;
    }

    private List<Integer> getNeighbor(int node1Successor, int node2Successor, Solution solution) {
        List<Integer> part1 = solution.getPart(node2Successor, node1Successor);
        List<Integer> part2 = solution.getPart(node1Successor, node2Successor);

        List<Integer> partsReversed = new LinkedList<>();
        new LinkedList<>(part2)
             .descendingIterator()
             .forEachRemaining(partsReversed::add);

        return Stream.concat(
                        part1.stream(),
                        partsReversed.stream()
                    ).collect(Collectors.toList());
    }
}
