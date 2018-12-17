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
    private Selector selector = new Selector(SelectionType.LINEAR_RANKING);
    private Recombinator recombinator;
    private Mutator mutator = new Mutator();
    private Replacer replacer = new Replacer();

    public Solution solve(Graph graph) {
        //TODO pocket the best solution so far found
        int generation = 0;
        int parents = 4;
        List<Solution> population = initializer.initialize(graph);

        recombinator = new Recombinator(graph);
        //population = evaluator.evaluate(population);

        while (generation < MAX_NUM_GENERATIONS) {
            generation++;
            List<Solution> newPopulation = selector.select(population, parents);

            newPopulation = recombinator.recombine(newPopulation);
            newPopulation = mutator.mutate(newPopulation);
            population = replacer.replace(population, newPopulation);

        }

        return population.get(0);
    }
}
