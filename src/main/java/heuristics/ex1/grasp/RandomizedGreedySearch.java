package heuristics.ex1.grasp;

import heuristics.ex1.dto.Graph;
import heuristics.ex1.dto.Solution;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;

class RandomizedGreedySearch {

    private static final int RESTRICTED_CANDIDATE_LIST_SIZE = 10;

    Solution solve(Graph graph) {
        LinkedList<Integer> solution = new LinkedList<>();
        int objective = 0;

        Set<Integer> nodes = graph.getMatrix().keySet();

        int firstNode = nodes.stream()
                             .findFirst()
                             .orElseThrow(() -> new RuntimeException("Graph has no nodes"));
        solution.add(firstNode);

        while (solution.size() < nodes.size()) {
            int lastNode = solution.get(solution.size()-1);

            List<Integer> candidateList = nodes.stream()
                                                .filter(n -> !solution.contains(n))
                                                .collect(Collectors.toList());

            List<Integer> restrictedCandidateList = restrict(candidateList, objective, lastNode, graph);

            Random random = new Random();
            int randomIndex =  random.nextInt(restrictedCandidateList.size());

            int node = restrictedCandidateList.get(randomIndex);
            int weight = graph.getWeight(lastNode, node);

            solution.add(node);
            objective += weight;
        }

        int lastNode = solution.getLast();
        objective += graph.getWeight(lastNode, firstNode);

        return new Solution(solution, objective);
    }

    private List<Integer> restrict(List<Integer> candidateList, int objective, int lastNode, Graph graph) {

        List<NodeWithObjective> nodeWithObjectives = candidateList.stream()
                                        .map(c -> new NodeWithObjective(c, Math.abs(objective + graph.getWeight(lastNode, c))))
                                        .collect(Collectors.toList());

        List<NodeWithObjective> sorted = nodeWithObjectives.stream()
                                        .sorted(Comparator.comparingInt(NodeWithObjective::getObjective))
                                        .collect(Collectors.toList());

        return sorted.stream()
                     .limit(RESTRICTED_CANDIDATE_LIST_SIZE)
                     .map(NodeWithObjective::getNode)
                     .collect(Collectors.toList());
    }

    @AllArgsConstructor
    @Getter
    private static class NodeWithObjective {
        private int node;
        private int objective;
    }
}
