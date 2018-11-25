package heuristics.ex1.grasp;

import heuristics.ex1.construction.ConstructionHeuristic;
import heuristics.ex1.construction.RandomConstructionHeuristic;
import heuristics.ex1.construction.RandomizedGreedySearch;
import heuristics.ex1.dto.Graph;
import heuristics.ex1.dto.Solution;
import heuristics.ex1.localsearch.LocalSearch;
import heuristics.ex1.localsearch.neighborhood.Neighborhood;
import heuristics.ex1.localsearch.neighborhood.StepType;
import heuristics.ex1.localsearch.neighborhood.ThreeOptNeighborhoodNew;
import heuristics.ex1.localsearch.neighborhood.TwoOptNeighborhood;

import java.lang.management.ManagementFactory;

public class GRASP {

    private ConstructionHeuristic constructionHeuristic = new RandomConstructionHeuristic();
    private Neighborhood neighborhood                   = new ThreeOptNeighborhoodNew();
    private StepType stepType                           = StepType.NEXT_IMPROVEMENT;

    private int maxTimeInSeconds;

    public GRASP() {
        this(15 * 60);
    }

    public GRASP(int maxTimeInSeconds) {
        this.maxTimeInSeconds = maxTimeInSeconds;
    }

    public Solution solve(Graph graph) {
        LocalSearch localSearch = new LocalSearch(neighborhood, stepType, maxTimeInSeconds);

        long startTime = ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime();
        long endTime = startTime + 1000L * 1000L * 1000L * maxTimeInSeconds;

        int unsuccessfulImprovements = 0;

        Solution bestSolution = new Solution(null, Long.MAX_VALUE);

        boolean timedOut = false;
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
            }

            long currentTime = ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime();
            if (currentTime >= endTime) {
                timedOut = true;
                bestSolution.setTimedOut(true);
            }
        } while(unsuccessfulImprovements < 100 && !timedOut);

        return bestSolution;
    }
}
