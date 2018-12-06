package heuristics.ex2.ga;

import heuristics.ex1.dto.Graph;
import heuristics.ex1.dto.Solution;
import heuristics.ex2.ga.util.*;

import java.util.List;

public class GeneticAlgorithm {

    private static final int MAX_NUM_GENERATIONS = 100;
    private static final int POPULATION_SIZE = 10;

    private Initializer initializer = new Initializer(POPULATION_SIZE);
    private Evaluator evaluator = new Evaluator();
    private Selector selector = new Selector();
    private Recombinator recombinator = new Recombinator();
    private Mutator mutator = new Mutator();
    private Replacer replacer = new Replacer();

    public Solution solve(Graph graph) {

        int generation = 0;
        List<Solution> population = initializer.initialize(graph);
        population = evaluator.evaluate(population);

        while (generation < MAX_NUM_GENERATIONS) {
            generation++;
            List<Solution> newPopulation = selector.select(population);

            newPopulation = recombinator.recombine(newPopulation);
            newPopulation = mutator.mutate(newPopulation);
            newPopulation = replacer.replace(population, newPopulation);

            population = evaluator.evaluate(newPopulation);
        }

        return population.get(0);
    }
}
