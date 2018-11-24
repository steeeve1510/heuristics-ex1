package heuristics.ex1.vnd;

import heuristics.ex1.dto.Graph;
import heuristics.ex1.dto.Solution;
import heuristics.ex1.localsearch.neighborhood.*;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;

public class VND {

    private int maxTimeInSeconds;
    private List<Neighborhood> neighborhoods = new ArrayList<>();

    public VND() {
        this(15 * 60);
    }

    public VND(int maxTimeInSeconds) {
        this.maxTimeInSeconds = maxTimeInSeconds;

        neighborhoods.add(new TwoOptNeighborhood());
        neighborhoods.add(new TwoFiveOptNeighborhood());
        neighborhoods.add(new ThreeOptNeighborhoodNew());
    }

    public Solution improve(Solution solution, Graph graph) {

        long startTime = ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime();
        long endTime = startTime + 1000L * 1000L * 1000L * maxTimeInSeconds;

        boolean timedOut = false;


        int l = 0;
        do {
            Neighborhood neighborhood = neighborhoods.get(l);

            Solution neighbor = neighborhood.get(solution, graph, StepType.NEXT_IMPROVEMENT);

            if (neighbor != null && neighbor.getAbsoluteObjectiveValue() < solution.getAbsoluteObjectiveValue()) {
                solution = neighbor;
                l = 0;
            } else {
                l++;
            }

            long currentTime = ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime();
            if (currentTime >= endTime) {
                timedOut = true;
                solution.setTimedOut(true);
            }
        } while (l < neighborhoods.size() && !timedOut);

        return solution;
    }
}
