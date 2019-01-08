package heuristics.ex2.ga;

import heuristics.ex1.dto.Graph;
import heuristics.ex1.dto.Solution;
import heuristics.ex2.ga.util.*;

import java.util.SortedSet;


public class GeneticAlgorithm {

    private static final int MAX_NUM_GENERATIONS = 100;
    private static final int INIT_POPULATION_SIZE = 150;
    private static final double CROSSOVER_FACTOR_OVER_POPULATION = 0.8;
    private static final double MUTATION_FACTOR_ON_POPULATION = 0.1;
    private static final int MUTATION_RATE = 40;

    private Initializer initializer = new Initializer(INIT_POPULATION_SIZE);
    private Selector selector = new Selector(CROSSOVER_FACTOR_OVER_POPULATION, SelectionType.LINEAR_RANKING);
    private Recombinator recombinator = new Recombinator(INIT_POPULATION_SIZE);
    private Mutator mutator = new Mutator(MUTATION_FACTOR_ON_POPULATION, MUTATION_RATE);
    private Replacer replacer = new Replacer(INIT_POPULATION_SIZE);

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
