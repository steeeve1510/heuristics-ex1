package heuristics.ex1.localsearch;

import heuristics.ex1.dto.Graph;
import heuristics.ex1.dto.Solution;
import heuristics.ex1.localsearch.neighborhood.Neighborhood;
import heuristics.ex1.localsearch.step.Step;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class LocalSearch {

    private Neighborhood neighborhood;
    private Step step;

    public Solution improve(Solution solution, Graph graph) {

        int unsuccessfulImprovements = 0;

        do {
            List<Solution> neighbors = neighborhood.get(solution, graph);
            Solution neighbor = step.get(neighbors, solution);
            if (neighbor == null) {
                break;
            }

            if (neighbor.getAbsoluteObjectiveValue() >= solution.getAbsoluteObjectiveValue()) {
                unsuccessfulImprovements++;
            }

            if (neighbor.getAbsoluteObjectiveValue() <= solution.getAbsoluteObjectiveValue()) {
                solution = neighbor;
            }
        } while (unsuccessfulImprovements < 100);

        return solution;
    }
}
