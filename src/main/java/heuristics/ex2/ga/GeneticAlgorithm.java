package heuristics.ex2.ga;

import heuristics.ex1.dto.Graph;
import heuristics.ex1.dto.Solution;
import heuristics.ex2.ga.util.*;

import java.util.SortedSet;


public class GeneticAlgorithm {

    private static final int MAX_NUM_GENERATIONS = 200;
    private static final int POPULATION_SIZE = 150;
    private static final int SELECTION_SIZE = 50;

    private Initializer initializer = new Initializer(POPULATION_SIZE);
    private Evaluator evaluator = new Evaluator();
    private Selector selector = new Selector(SelectionType.LINEAR_RANKING);
    private Recombinator recombinator = new Recombinator();
    private Mutator mutator = new Mutator();
    private Replacer replacer = new Replacer();

    public Solution solve(Graph graph) {
        int generation = 0;

        SortedSet<Solution> population = initializer.initialize(graph);

        Solution best = population.first();

        //population = evaluator.evaluate(population);

        while (generation < MAX_NUM_GENERATIONS) {
            generation++;
            SortedSet<Solution> parents = selector.select(population, SELECTION_SIZE);
            SortedSet<Solution> offSpring = recombinator.recombine(parents, graph);
//            offSpring = mutator.mutate(new LinkedList<>(offSpring), graph);

            population = replacer.replace(parents, offSpring);

            //Check if there is any solution better than any previous
            Solution bestOfNewPopulation = population.first();
            if (bestOfNewPopulation.getAbsoluteObjectiveValue() < best.getAbsoluteObjectiveValue()){
                best = bestOfNewPopulation;
            }
        }

        return best;
    }
}
