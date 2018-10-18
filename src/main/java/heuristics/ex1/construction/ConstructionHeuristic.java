package heuristics.ex1.construction;

import heuristics.ex1.dto.Problem;
import heuristics.ex1.dto.Solution;

public interface ConstructionHeuristic {

    Solution solve(Problem problem);
}
