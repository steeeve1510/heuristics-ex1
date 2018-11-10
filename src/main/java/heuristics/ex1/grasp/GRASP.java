package heuristics.ex1.grasp;

import heuristics.ex1.construction.ConstructionHeuristic;
import heuristics.ex1.construction.RandomConstructionHeuristic;
import heuristics.ex1.construction.RandomizedGreedySearch;
import heuristics.ex1.dto.Graph;
import heuristics.ex1.dto.Solution;
import heuristics.ex1.localsearch.LocalSearch;
import heuristics.ex1.localsearch.neighborhood.Neighborhood;
import heuristics.ex1.localsearch.neighborhood.StepType;
import heuristics.ex1.localsearch.neighborhood.TwoOptNeighborhood;

public class GRASP {

    private ConstructionHeuristic constructionHeuristic = new RandomConstructionHeuristic();
    private Neighborhood neighborhood                   = new TwoOptNeighborhood();
    private StepType stepType                           = StepType.NEXT_IMPROVEMENT;

    public Solution solve(Graph graph) {
        System.out.println("Starting GRASP");
        LocalSearch localSearch = new LocalSearch(neighborhood, stepType);

        int unsuccessfulImprovements = 0;

        Solution bestSolution = new Solution(null, Long.MAX_VALUE);

        do {
            Solution randomSolution = constructionHeuristic.solve(graph);
            Solution improvedSolution = localSearch.improve(randomSolution, graph);


            if (improvedSolution.getAbsoluteObjectiveValue() < bestSolution.getAbsoluteObjectiveValue()) {
                unsuccessfulImprovements = 0;
            } else {
                unsuccessfulImprovements++;
            }


            if (improvedSolution.getAbsoluteObjectiveValue() <= bestSolution.getAbsoluteObjectiveValue()) {
                bestSolution = improvedSolution;
                System.out.println("Found better solution: " + bestSolution.getObjectiveValue());
            }

            if (unsuccessfulImprovements % 10 == 0 && unsuccessfulImprovements != 0) {
                System.out.println(unsuccessfulImprovements + " unsuccessful improvements");
            }
        } while(unsuccessfulImprovements < 100);

        return bestSolution;
    }
}
