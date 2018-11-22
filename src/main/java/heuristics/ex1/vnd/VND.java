package heuristics.ex1.vnd;

import heuristics.ex1.dto.Graph;
import heuristics.ex1.dto.Solution;
import heuristics.ex1.localsearch.neighborhood.*;

import java.util.ArrayList;
import java.util.List;

public class VND {

    private List<Neighborhood> neighborhoods = new ArrayList<>();

    public VND() {
        neighborhoods.add(new TwoOptNeighborhood());
        neighborhoods.add(new TwoFiveOptNeighborhood());
        neighborhoods.add(new ThreeOptNeighborhoodNew());
    }

    public Solution improve(Solution solution, Graph graph) {
        System.out.println("Starting VND");
        int l = 0;

        do {
            Neighborhood neighborhood = neighborhoods.get(l);

            Solution neighbor = neighborhood.get(solution, graph, StepType.BEST_IMPOVEMENT);

            if (neighbor != null && neighbor.getAbsoluteObjectiveValue() < solution.getAbsoluteObjectiveValue()) {
                System.out.println("Found improvement: " + neighbor.getAbsoluteObjectiveValue());
                solution = neighbor;
                l = 0;
            } else {
                l++;
            }
        } while (l < neighborhoods.size());

        return solution;
    }
}
