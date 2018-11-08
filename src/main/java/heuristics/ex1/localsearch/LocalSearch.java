package heuristics.ex1.localsearch;

import heuristics.ex1.dto.Graph;
import heuristics.ex1.dto.Solution;
import heuristics.ex1.localsearch.neighborhood.Neighborhood;
import heuristics.ex1.localsearch.neighborhood.StepType;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class LocalSearch {

    private Neighborhood neighborhood;
    private StepType stepType;

    public Solution improve(Solution solution, Graph graph) {

        int unsuccessfulImprovements = 0;

        do {
            Solution neighbor = neighborhood.get(solution, graph, stepType);
            if (neighbor == null) {
                break;
            }


            if (neighbor.getAbsoluteObjectiveValue() < solution.getAbsoluteObjectiveValue()) {
                unsuccessfulImprovements = 0;
            }
            if (neighbor.getAbsoluteObjectiveValue() >= solution.getAbsoluteObjectiveValue()) {
                unsuccessfulImprovements++;
            }


            if (neighbor.getAbsoluteObjectiveValue() <= solution.getAbsoluteObjectiveValue()) {
                solution = neighbor;
            }
        } while (unsuccessfulImprovements < 100000);

        return solution;
    }
}
