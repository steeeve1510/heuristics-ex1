package heuristics.ex1.localsearch.neighborhood;

import heuristics.ex1.dto.Graph;
import heuristics.ex1.dto.Solution;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ThreeOptNeighborhoodNew implements Neighborhood {

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
            for (int j = i+2; j < size + i - 3; j++) {
                for (int k = j+2; k < size + i - 1; k++) {

                    int node1 = solution.get(i);
                    int node2 = solution.get(Math.floorMod(j, size));
                    int node3 = solution.get(Math.floorMod(k, size));

                    List<Solution> neighbors = move(node1, node2, node3, solution, graph);
                    for (Solution neighbor : neighbors) {
                        if (neighbor.getAbsoluteObjectiveValue() < bestSolution.getAbsoluteObjectiveValue()) {
                            bestSolution = neighbor;
                        }
                    }
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
            for (int j = i+2; j < size + i - 3; j++) {
                for (int k = j+2; k < size + i - 1; k++) {

                    int node1 = solution.get(i);
                    int node2 = solution.get(Math.floorMod(j, size));
                    int node3 = solution.get(Math.floorMod(k, size));

                    List<Solution> neighbors = move(node1, node2, node3, solution, graph);
                    for (Solution neighbor : neighbors) {
                        if (neighbor.getAbsoluteObjectiveValue() < solution.getAbsoluteObjectiveValue()) {
                            return neighbor;
                        }
                    }
                }
            }
        }
        return null;
    }

    private Solution getRandomNeighbor(Solution solution, Graph graph) {
        int size = solution.getSize();

        Random random = new Random();
        int node1Index = random.nextInt(size);
        int addForNode2 = random.nextInt(size);
        int addForNode3 = random.nextInt(size);

        if (addForNode2 < 2) {
            addForNode2 = 2;
        } else if (addForNode2 >= size - 3) {
            addForNode2 = size - 4;
        };

        if (addForNode3 < 2) {
            addForNode3 = 2;
        } else if (addForNode3 + addForNode2 >= size - 1) {
            addForNode3 = size - addForNode2 - 2;
        }

        int node1 = solution.get(node1Index);
        int node2 = solution.get(Math.floorMod(node1Index + addForNode2, size));
        int node3 = solution.get(Math.floorMod(node1Index + addForNode2 + addForNode3, size));

        List<Solution> solutions = move(node1, node2, node3, solution, graph);

        int randomSolution = random.nextInt(solutions.size());

        return solutions.get(randomSolution);
    }

    private List<Solution> move(int node1, int node2, int node3, Solution solution, Graph graph) {
        int node1Successor = solution.getSuccessor(node1);
        int node2Successor = solution.getSuccessor(node2);
        int node3Successor = solution.getSuccessor(node3);

        List<Integer> part1To2 = solution.getPart(node1Successor, node2Successor);
        List<Integer> part2To3 = solution.getPart(node2Successor, node3Successor);
        List<Integer> part3To1 = solution.getPart(node3Successor, node1Successor);

        long oldWeight1 = graph.getWeight(node1, node1Successor);
        long oldWeight2 = graph.getWeight(node2, node2Successor);
        long oldWeight3 = graph.getWeight(node3, node3Successor);

        long clearedObjectiveValue = solution.getObjectiveValue() - oldWeight1 - oldWeight2 - oldWeight3;

        List<Solution> solutions = new ArrayList<>();

        Solution neighbor4 = getNeighborCase4(
                node1, node1Successor,
                node2, node2Successor,
                node3, node3Successor,
                part1To2, part2To3, part3To1,
                clearedObjectiveValue,
                graph
        );

        Solution neighbor5 = getNeighborCase5(
                node1, node1Successor,
                node2, node2Successor,
                node3, node3Successor,
                part1To2, part2To3, part3To1,
                clearedObjectiveValue,
                graph
        );

        Solution neighbor6 = getNeighborCase6(
                node1, node1Successor,
                node2, node2Successor,
                node3, node3Successor,
                part1To2, part2To3, part3To1,
                clearedObjectiveValue,
                graph
        );

        Solution neighbor7 = getNeighborCase7(
                node1, node1Successor,
                node2, node2Successor,
                node3, node3Successor,
                part1To2, part2To3, part3To1,
                clearedObjectiveValue,
                graph
        );

        solutions.add(neighbor4);
        solutions.add(neighbor5);
        solutions.add(neighbor6);
        solutions.add(neighbor7);

        return solutions;
    }

    private Solution getNeighborCase4(int node1, int node1Successor,
                                      int node2, int node2Successor,
                                      int node3, int node3Successor,
                                      List<Integer> part1To2, List<Integer> part2To3, List<Integer> part3To1,
                                      long clearedObjectiveValue,
                                      Graph graph) {

        Integer[] p1To2 = part1To2.toArray(new Integer[]{});
        Integer[] p1To3 = part3To1.toArray(new Integer[]{});
        ArrayUtils.reverse(p1To3);
        Integer[] p2To3 = part2To3.toArray(new Integer[]{});
        Integer[] neighborNodes = ArrayUtils.addAll(p1To2, ArrayUtils.addAll(p1To3, p2To3));

        long newWeight1 = graph.getWeight(node2, node1);
        long newWeight2 = graph.getWeight(node3Successor, node2Successor);
        long newWeight3 = graph.getWeight(node3, node1Successor);

        long newObjectiveValue = clearedObjectiveValue + newWeight1 + newWeight2 + newWeight3;

        return new Solution(Arrays.asList(neighborNodes), newObjectiveValue);
    }

    private Solution getNeighborCase5(int node1, int node1Successor,
                                      int node2, int node2Successor,
                                      int node3, int node3Successor,
                                      List<Integer> part1To2, List<Integer> part2To3, List<Integer> part3To1,
                                      long clearedObjectiveValue,
                                      Graph graph) {

        Integer[] p1To2 = part1To2.toArray(new Integer[]{});
        Integer[] p3To1 = part3To1.toArray(new Integer[]{});
        Integer[] p3To2 = part2To3.toArray(new Integer[]{});
        ArrayUtils.reverse(p3To2);
        Integer[] neighborNodes = ArrayUtils.addAll(p1To2, ArrayUtils.addAll(p3To1, p3To2));

        long newWeight1 = graph.getWeight(node2, node3Successor);
        long newWeight2 = graph.getWeight(node1, node3);
        long newWeight3 = graph.getWeight(node2Successor, node1Successor);

        long newObjectiveValue = clearedObjectiveValue + newWeight1 + newWeight2 + newWeight3;

        return new Solution(Arrays.asList(neighborNodes), newObjectiveValue);
    }

    private Solution getNeighborCase6(int node1, int node1Successor,
                                      int node2, int node2Successor,
                                      int node3, int node3Successor,
                                      List<Integer> part1To2, List<Integer> part2To3, List<Integer> part3To1,
                                      long clearedObjectiveValue,
                                      Graph graph) {

        Integer[] p1To2 = part1To2.toArray(new Integer[]{});
        Integer[] p3To2 = part2To3.toArray(new Integer[]{});
        ArrayUtils.reverse(p3To2);
        Integer[] p1To3 = part3To1.toArray(new Integer[]{});
        ArrayUtils.reverse(p1To3);
        Integer[] neighborNodes = ArrayUtils.addAll(p1To2, ArrayUtils.addAll(p3To2, p1To3));

        long newWeight1 = graph.getWeight(node2, node3);
        long newWeight2 = graph.getWeight(node2Successor, node1);
        long newWeight3 = graph.getWeight(node3Successor, node1Successor);

        long newObjectiveValue = clearedObjectiveValue + newWeight1 + newWeight2 + newWeight3;

        return new Solution(Arrays.asList(neighborNodes), newObjectiveValue);
    }

    private Solution getNeighborCase7(int node1, int node1Successor,
                                      int node2, int node2Successor,
                                      int node3, int node3Successor,
                                      List<Integer> part1To2, List<Integer> part2To3, List<Integer> part3To1,
                                      long clearedObjectiveValue,
                                      Graph graph) {

        Integer[] p1To2 = part1To2.toArray(new Integer[]{});
        Integer[] p3To1 = part3To1.toArray(new Integer[]{});
        Integer[] p2To3 = part2To3.toArray(new Integer[]{});
        Integer[] neighborNodes = ArrayUtils.addAll(p1To2, ArrayUtils.addAll(p3To1, p2To3));

        long newWeight1 = graph.getWeight(node2, node3Successor);
        long newWeight2 = graph.getWeight(node1, node2Successor);
        long newWeight3 = graph.getWeight(node3, node1Successor);

        long newObjectiveValue = clearedObjectiveValue + newWeight1 + newWeight2 + newWeight3;

        return new Solution(Arrays.asList(neighborNodes), newObjectiveValue);
    }

}
