package heuristics.ex1.localsearch;

import heuristics.ex1.dto.Graph;
import heuristics.ex1.dto.Solution;
import heuristics.ex1.localsearch.neighborhood.Neighborhood;
import heuristics.ex1.localsearch.neighborhood.StepType;
import lombok.AllArgsConstructor;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

public class LocalSearch {

    private Neighborhood neighborhood;
    private StepType stepType;
    private int maxTimeInSeconds;

    public LocalSearch(Neighborhood neighborhood, StepType stepType) {
        this(neighborhood, stepType, 15 * 60);
    }

    public LocalSearch(Neighborhood neighborhood, StepType stepType, int maxTimeInSeconds) {
        this.neighborhood = neighborhood;
        this.stepType = stepType;
        this.maxTimeInSeconds = maxTimeInSeconds;
    }

    public Solution improve(Solution solution, Graph graph) {

        long size = solution.getSize();
        long maxUnsuccessfulImprovements = size * 250;

        long startTime = ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime();
        long endTime = startTime + 1000L * 1000L * 1000L * maxTimeInSeconds;

        boolean timedOut = false;
        int unsuccessfulImprovements = 0;
        do {
            Solution neighbor = neighborhood.get(solution, graph, stepType);
            if (neighbor == null) {
                break;
            }


            if (neighbor.getAbsoluteObjectiveValue() < solution.getAbsoluteObjectiveValue()) {
                unsuccessfulImprovements = 0;
            } else {
                unsuccessfulImprovements++;
            }


            if (neighbor.getAbsoluteObjectiveValue() <= solution.getAbsoluteObjectiveValue()) {
                solution = neighbor;
            }

            long currentTime = ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime();
            if (currentTime >= endTime) {
                timedOut = true;
                solution.setTimedOut(true);
            }
        } while (unsuccessfulImprovements < maxUnsuccessfulImprovements && !timedOut);

        return solution;
    }
}
