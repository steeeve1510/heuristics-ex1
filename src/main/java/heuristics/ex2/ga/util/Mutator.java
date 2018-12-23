package heuristics.ex2.ga.util;

import heuristics.ex1.dto.Graph;
import heuristics.ex1.dto.Solution;
import heuristics.ex1.localsearch.neighborhood.Neighborhood;
import heuristics.ex1.localsearch.neighborhood.StepType;
import heuristics.ex1.localsearch.neighborhood.TwoOptNeighborhood;

import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;

public class Mutator {

    public SortedSet<Solution> mutate(SortedSet<Solution> population, Graph graph) {
        /*
        This class should take the new population from the recombinator and mutate them

        From slides (mutators for permutations:
        Reciprocal Exchange
        Insertion
        Displacement
        Inversion
         */

        double p = 1d / population.size();

        Random random = new Random();

        SortedSet<Solution> mutatedPopulation = new TreeSet<>(new SolutionComparator());

        for (Solution solution : population) {
            double randomNumber = random.nextDouble();
            if (randomNumber < p) {
                solution = reciprocalExchange(solution, graph);
            }
            mutatedPopulation.add(solution);
        }

        return mutatedPopulation;
    }

    private Solution reciprocalExchange(Solution solution, Graph graph) {
        Neighborhood neighborhood = new TwoOptNeighborhood();

        return neighborhood.get(solution, graph, StepType.RANDOM);
    }
}
