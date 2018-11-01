package heuristics.ex1.localsearch.neighborhood;

import heuristics.ex1.dto.Graph;
import heuristics.ex1.dto.Solution;

import java.util.List;

public interface Neighborhood {

    Solution get(Solution solution, Graph graph, StepType stepType);
}
