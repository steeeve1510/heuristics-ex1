package heuristics.ex1.localsearch.step;

import heuristics.ex1.dto.Solution;

import java.util.List;

public interface Step {

    Solution get(List<Solution> neighbors, Solution solution);
}
