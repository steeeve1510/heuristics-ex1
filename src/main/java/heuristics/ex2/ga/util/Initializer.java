package heuristics.ex2.ga.util;

import heuristics.ex1.construction.ConstructionHeuristic;
import heuristics.ex1.construction.RandomizedGreedySearch;
import heuristics.ex1.dto.Graph;
import heuristics.ex1.dto.Solution;

import java.util.LinkedList;
import java.util.List;

public class Initializer {

    private ConstructionHeuristic construction = new RandomizedGreedySearch(10);

    private int populationSize;

    public Initializer(int populationSize) {
        this.populationSize = populationSize;
    }

    public List<Solution> initialize(Graph graph) {
        List<Solution> population = new LinkedList<>();

        for (int i = 0; i < populationSize; i++) {
            Solution solution = construction.solve(graph);
            population.add(solution);
        }

        return population;
    }
}
