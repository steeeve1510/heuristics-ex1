package heuristics.ex1.construction;

import heuristics.ex1.dto.Graph;
import heuristics.ex1.dto.Solution;

public interface ConstructionHeuristic {

    Solution solve(Graph graph);
}
