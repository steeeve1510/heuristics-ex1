package heuristics.ex2.ga.util;

import heuristics.ex1.construction.ConstructionHeuristic;
import heuristics.ex1.construction.RandomizedGreedySearch;
import heuristics.ex1.dto.Graph;
import heuristics.ex1.dto.Solution;
import heuristics.ex1.localsearch.LocalSearch;
import heuristics.ex1.localsearch.neighborhood.StepType;
import heuristics.ex1.localsearch.neighborhood.ThreeOptNeighborhoodNew;
import heuristics.ex1.localsearch.neighborhood.TwoOptNeighborhood;

import java.util.SortedSet;
import java.util.TreeSet;

public class HybridInitializer {

    private ConstructionHeuristic construction = new RandomizedGreedySearch(1);

    private int populationSize;
    private int timeLimitSeconds;

    public HybridInitializer(int populationSize, int timeLimitSeconds) {
        this.populationSize = populationSize;
        this.timeLimitSeconds = timeLimitSeconds;
    }

    public SortedSet<Solution> initialize(Graph graph) {
        SortedSet<Solution> population = new TreeSet<>(new SolutionComparator());
        LocalSearch localSearch = new LocalSearch(new ThreeOptNeighborhoodNew(), StepType.NEXT_IMPROVEMENT,
                (int) Math.ceil(timeLimitSeconds / populationSize));

        for (int i = 0; i < populationSize; i++) {
            Solution solution = construction.solve(graph);
            solution = localSearch.improve(solution, graph);
            population.add(solution);
        }

        return population;
    }
}
