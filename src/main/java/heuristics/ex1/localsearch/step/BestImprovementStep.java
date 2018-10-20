package heuristics.ex1.localsearch.step;

import heuristics.ex1.dto.Solution;

import java.util.Comparator;
import java.util.List;

public class BestImprovementStep implements Step {
    @Override
    public Solution get(List<Solution> neighbors, Solution solution) {
        return neighbors.stream()
                        .min(Comparator.comparingInt(Solution::getAbsoluteObjectiveValue))
                        .orElse(null);
    }
}
