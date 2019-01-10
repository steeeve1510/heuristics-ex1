package heuristics.ex2.ga;

import com.sun.istack.internal.FinalArrayList;
import heuristics.ex1.dto.Graph;
import heuristics.ex1.dto.Solution;
import heuristics.ex1.localsearch.LocalSearch;
import heuristics.ex1.localsearch.neighborhood.StepType;
import heuristics.ex1.localsearch.neighborhood.ThreeOptNeighborhoodNew;
import heuristics.ex2.ga.util.*;

import java.util.SortedSet;

public class HybridAlgorithm {

    private static final int MAX_NUM_GENERATIONS = 100;
    private static final int INIT_POPULATION_SIZE = 150;
    private static final double CROSSOVER_FACTOR_OVER_POPULATION = 0.8;
    private static final double MUTATION_FACTOR_ON_POPULATION = 0.1;
    private static final int MUTATION_RATE = 40;
    private static final int INITIALIZATION_TIME_SECONDS = 180;
    private static final int IMPROVEMENT_TIME_SECONDS = 180;

    private HybridInitializer hybridInitializer = new HybridInitializer(INIT_POPULATION_SIZE, INITIALIZATION_TIME_SECONDS);
    private Selector selector = new Selector(CROSSOVER_FACTOR_OVER_POPULATION, SelectionType.LINEAR_RANKING);
    private Recombinator recombinator = new Recombinator(INIT_POPULATION_SIZE);
    private Mutator mutator = new Mutator(MUTATION_FACTOR_ON_POPULATION, MUTATION_RATE);
    private Replacer replacer = new Replacer(INIT_POPULATION_SIZE);


    public Solution solve(Graph graph) {
        int generation = 0;

        SortedSet<Solution> population = hybridInitializer.initialize(graph);

        Solution best = population.first();
        System.out.println("Initial best: " + best.getAbsoluteObjectiveValue());

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

        LocalSearch finalImprover = new LocalSearch(new ThreeOptNeighborhoodNew(), StepType.NEXT_IMPROVEMENT, IMPROVEMENT_TIME_SECONDS);
        best = finalImprover.improve(best, graph);

        return best;
    }
}
