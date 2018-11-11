package heuristics.ex1.localsearch.neighborhood;

import heuristics.ex1.dto.Graph;
import heuristics.ex1.dto.Solution;

import java.util.*;

public class ThreeOptNeighborhood implements Neighborhood {

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
            for (int j = i+1; j < size - 2; j++) {
                for (int k = j+1; k < size - 1; k++) {
                    Solution neighbor = move(i, j, k, solution, graph);
                    if (neighbor.getAbsoluteObjectiveValue() < bestSolution.getAbsoluteObjectiveValue()) {
                        bestSolution = neighbor;
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
            for (int j = i+1; j < size - 2; j++) {
                for (int k = j+1; k < size - 1; k++) {
                    Solution neighbor = move(i, j, k, solution, graph);
                    if (neighbor.getAbsoluteObjectiveValue() < solution.getAbsoluteObjectiveValue()) {
                        return neighbor;
                    }
                }
            }
        }
        return null;
    }

    private Solution getRandomNeighbor(Solution solution, Graph graph) {
        int size = solution.getSize();
        int[] nodeIndexes = new int[3];

        Random random = new Random();
        nodeIndexes[0] = random.nextInt(size);
        nodeIndexes[1] = random.nextInt(size);
        while (nodeIndexes[1] == nodeIndexes[0]){
            nodeIndexes[1] = random.nextInt(size);
        }
        nodeIndexes[2]  = random.nextInt(size);
        while (nodeIndexes[2] == nodeIndexes[0] || nodeIndexes[2] == nodeIndexes[1]){
            nodeIndexes[2] = random.nextInt(size);
        }
        Arrays.sort(nodeIndexes);
        return move(nodeIndexes[0], nodeIndexes[1], nodeIndexes[2], solution, graph);
    }

    private Solution move(int node1Index, int node2Index, int node3Index, Solution solution, Graph graph) {
        long objectiveValue = solution.getAbsoluteObjectiveValue();
        int size = solution.getSize();
        int node1Successor = solution.getSuccessor(solution.get(node1Index));
        int node2Successor = solution.getSuccessor(solution.get(node2Index));
        int node3Successor = solution.getSuccessor(solution.get(node3Index));

        int part2size = node2Index - node1Index;
        int part3size = node3Index - node2Index;
        int part1size = size - (node3Index + 1) + node1Index+1 ;
        // Avoid part1 having only one node
        if (part1size == 1){
            if (part2size == 1){
                part3size = part1size;
                int aux = node1Index;
                node1Index = node3Index;
                node3Index = node2Index;
                node2Index = aux;
            } else {
                part2size = part3size;
                part3size = 1;
                int aux = node1Index;
                node1Index = node2Index;
                node2Index = node3Index;
                node3Index = aux;
            }
        }
        //Should we avoid the moves equivalent to 2Opt moves?
        ArrayList<Integer> possibilities = new ArrayList<>();
        if (part2size==1 && part3size==1){
            possibilities.add(1); //part1 + part3 + part2, == 2Opt move
        } else if (part2size==1){
            possibilities.add(1); //part1 + part3 + part2
            possibilities.add(3); //part1 + part3rev + part2 == 2Opt move
            possibilities.add(4); //part1 + part2 + part3rev == 2Opt move
        } else if (part3size==1){
            possibilities.add(1); //part1 + part3 + part2,
            possibilities.add(5); //part1 + part2rev + part3 == 2Opt move
            possibilities.add(2); //part1 + part3 + part2rev == 2Opt move
        } else {
            possibilities.add(0);
            possibilities.add(1);
            possibilities.add(2);
            possibilities.add(3);
            possibilities.add(4); //== 2Opt move
            possibilities.add(5); //== 2Opt move
            possibilities.add(6); //== 2Opt move
        }
        long newObjectiveValue, bestNewObjectiveValue;
        bestNewObjectiveValue = Math.abs(getNewObjectiveValue(objectiveValue, solution.get(node1Index), node1Successor,
                solution.get(node2Index), node2Successor, solution.get(node3Index), node3Successor, graph, 1));
        List<Integer> neighbor = getNeighbor( solution.get(node1Index),  solution.get(node2Index),
                solution.get(node3Index), solution, 1);

        for (Integer move : possibilities){
            newObjectiveValue = Math.abs(getNewObjectiveValue(objectiveValue, solution.get(node1Index), node1Successor,
                    solution.get(node2Index), node2Successor, solution.get(node3Index), node3Successor, graph, move));
            if (newObjectiveValue < bestNewObjectiveValue){
                bestNewObjectiveValue = newObjectiveValue;
                neighbor = getNeighbor( solution.get(node1Index),  solution.get(node2Index),
                        solution.get(node3Index), solution,  move);
            }
        }
        return new Solution(neighbor, bestNewObjectiveValue);
    }

    private long getNewObjectiveValue(long objectiveValue, int node1, int node1Successor, int node2, int node2Successor,
                                     int node3, int node3Successor, Graph graph, int possibility) {
        int oldWeight1 = graph.getWeight(node1, node1Successor);
        int oldWeight2 = graph.getWeight(node2, node2Successor);
        int oldWeight3 = graph.getWeight(node3, node3Successor);
        int newWeight1, newWeight2, newWeight3;
        switch (possibility){
            case 0: //part1 + part2 reversed + part 3 reversed
                newWeight1 = graph.getWeight(node1, node2);
                newWeight2 = graph.getWeight(node1Successor, node3);
                newWeight3 = graph.getWeight(node2Successor, node3Successor);
                return objectiveValue - oldWeight1 - oldWeight2 - oldWeight3 + newWeight1 + newWeight2 + newWeight3;
            case 1: //part1 + part3 + part2
                newWeight1 = graph.getWeight(node1, node2Successor);
                newWeight2 = graph.getWeight(node3, node1Successor);
                newWeight3 = graph.getWeight(node2, node3Successor);
                return objectiveValue - oldWeight1 - oldWeight2 - oldWeight3 + newWeight1 + newWeight2 + newWeight3;
            case 2: //part1 + part3 + part2reversed
                newWeight1 = graph.getWeight(node1, node2Successor);
                newWeight2 = graph.getWeight(node3, node2);
                newWeight3 = graph.getWeight(node1Successor, node3Successor);
                return objectiveValue - oldWeight1 - oldWeight2 - oldWeight3 + newWeight1 + newWeight2 + newWeight3;
            case 3: //part1 + part3reversed + part2
                newWeight1 = graph.getWeight(node1, node3);
                newWeight2 = graph.getWeight(node2Successor, node1Successor);
                newWeight3 = graph.getWeight(node2, node3Successor);
                return objectiveValue - oldWeight1 - oldWeight2 - oldWeight3 + newWeight1 + newWeight2 + newWeight3;
            case 4: //part1 + part2 + part3reversed ; 2 cuts
                newWeight1 = graph.getWeight(node1, node1Successor);
                newWeight2 = graph.getWeight(node2, node3);
                newWeight3 = graph.getWeight(node2Successor, node3Successor);
                return objectiveValue - oldWeight1 - oldWeight2 - oldWeight3 + newWeight1 + newWeight2 + newWeight3;
            case 5: //part1 + part2reversed + part3 ; 2 cuts
                newWeight1 = graph.getWeight(node1, node2);
                newWeight2 = graph.getWeight(node1Successor, node2Successor);
                newWeight3 = graph.getWeight(node3, node3Successor);
                return objectiveValue - oldWeight1 - oldWeight2 - oldWeight3 + newWeight1 + newWeight2 + newWeight3;
            case 6: //part1 + part3reversed + part2reversed ; 2 cuts
                newWeight1 = graph.getWeight(node1, node3);
                newWeight2 = graph.getWeight(node2Successor, node2);
                newWeight3 = graph.getWeight(node1Successor, node3Successor);
                return objectiveValue - oldWeight1 - oldWeight2 - oldWeight3 + newWeight1 + newWeight2 + newWeight3;
        }
        return objectiveValue;
    }

    private List<Integer> getNeighbor(int node1, int node2, int node3, Solution solution, int possibility) {
        /*
        node1
        possibility specifies which of the 7 different possible moves to return.
         */
        int node1Successor = solution.get(node1);
        int node2Successor = solution.get(node2);
        int node3Successor = solution.get(node3);
        List<Integer> part1 = solution.getPart(node3Successor, node1);
        List<Integer> part2 = solution.getPart(node1Successor, node2);
        List<Integer> part3 = solution.getPart(node2Successor, node3);

        switch (possibility){
            case 0: //part1 + part2 reversed + part 3 reversed
                Collections.reverse(part2);
                part1.addAll(part2);
                Collections.reverse(part3);
                part1.addAll(part3);
                return part1;
            case 1: //part1 + part3 + part2
                part1.addAll(part3);
                part1.addAll(part2);
                return part1;
            case 2: //part1 + part3 + part2reversed
                part1.addAll(part3);
                Collections.reverse(part2);
                part1.addAll(part2);
                return part1;
            case 3: //part1 + part3reversed + part2
                Collections.reverse(part3);
                part1.addAll(part3);
                part1.addAll(part2);
                return part1;
            case 4: //part1 + part2 + part3reversed ; 2 cuts
                part1.addAll(part2);
                Collections.reverse(part3);
                part1.addAll(part3);
                return part1;
            case 5: //part1 + part2reversed + part3 ; 2 cuts
                Collections.reverse(part2);
                part1.addAll(part2);
                part1.addAll(part3);
                return part1;
            case 6: //part1 + part3reversed + part2reversed ; 2 cuts
                Collections.reverse(part3);
                part1.addAll(part3);
                Collections.reverse(part2);
                part1.addAll(part2);
                return part1;
        }
        return null;
    }
}
