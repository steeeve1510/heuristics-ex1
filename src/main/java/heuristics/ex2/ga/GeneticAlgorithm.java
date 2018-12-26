package heuristics.ex2.ga;

import heuristics.ex1.dto.Graph;
import heuristics.ex1.dto.Solution;
import heuristics.ex2.ga.util.*;

import java.util.SortedSet;


public class GeneticAlgorithm {

    private static final int MAX_NUM_GENERATIONS = 50;
    private static final int MAX_POPULATION_SIZE = 400;
    private static final double SELECTION_SIZE_FACTOR = 0.6;
    private static final double MUTATION_FACTOR = 0.25;

    private Initializer initializer = new Initializer(MAX_POPULATION_SIZE);
    private Selector selector = new Selector(SELECTION_SIZE_FACTOR, SelectionType.LINEAR_RANKING);
    private Recombinator recombinator = new Recombinator();
    private Mutator mutator = new Mutator(MUTATION_FACTOR);
    private Replacer replacer = new Replacer(MAX_POPULATION_SIZE);

    public Solution solve(Graph graph) {
        int generation = 0;

        SortedSet<Solution> population = initializer.initialize(graph);

        Solution best = population.first();

        while (generation < MAX_NUM_GENERATIONS) {
            generation++;
            SortedSet<Solution> parents = selector.select(population);
            SortedSet<Solution> offSpring = recombinator.recombine(parents, graph);
            offSpring = mutator.mutate(offSpring, graph);

            population = replacer.replace(parents, offSpring);

            System.out.println("Generation: " + generation + ", Population:  " + population.size());
            //Check if there is any solution better than any previous
            Solution bestOfNewPopulation = population.first();
            if (bestOfNewPopulation.getAbsoluteObjectiveValue() < best.getAbsoluteObjectiveValue()){
                System.out.println("Update best: " + bestOfNewPopulation.getAbsoluteObjectiveValue());
                best = bestOfNewPopulation;
            }
        }

        return best;
    }
}
