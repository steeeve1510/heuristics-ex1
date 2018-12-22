package heuristics.ex2.ga.util;

import heuristics.ex1.construction.ConstructionHeuristic;
import heuristics.ex1.construction.RandomizedGreedySearch;
import heuristics.ex1.dto.Graph;
import heuristics.ex1.dto.Solution;

import java.util.SortedSet;
import java.util.TreeSet;

public class Initializer {

    private ConstructionHeuristic construction = new RandomizedGreedySearch(1);

    private int populationSize;

    public Initializer(int populationSize) {
        this.populationSize = populationSize;
    }

    public SortedSet<Solution> initialize(Graph graph) {
        SortedSet<Solution> population = new TreeSet<>(new SolutionComparator());

        for (int i = 0; i < populationSize; i++) {
            Solution solution = construction.solve(graph);
            population.add(solution);
        }

        return population;
    }
}
