package heuristics.ex1.localsearch.step;

import heuristics.ex1.dto.Solution;

import java.util.List;

public class NextImprovementStep implements Step {
    @Override
    public Solution get(List<Solution> neighbors, Solution solution) {
        int currentAbsoluteObjectiveValue = solution.getAbsoluteObjectiveValue();

        return neighbors.stream()
                        .filter(n -> n.getAbsoluteObjectiveValue() < currentAbsoluteObjectiveValue)
                        .findFirst()
                        .orElse(null);
    }
}
