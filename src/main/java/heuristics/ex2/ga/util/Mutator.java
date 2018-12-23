package heuristics.ex2.ga.util;

import heuristics.ex1.dto.Graph;
import heuristics.ex1.dto.Solution;
import heuristics.ex1.localsearch.neighborhood.Neighborhood;
import heuristics.ex1.localsearch.neighborhood.StepType;
import heuristics.ex1.localsearch.neighborhood.ThreeOptNeighborhoodNew;

import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;

public class Mutator {

    private double mutationFactor;

    /*
     * @param mutationFactor how many percent of the population should mutate. (0.0 - 1.0)
     */
    public Mutator(double mutationFactor) {
        this.mutationFactor = mutationFactor;
    }

    public SortedSet<Solution> mutate(SortedSet<Solution> population, Graph graph) {
        /*
        This class should take the new population from the recombinator and mutate them

        From slides (mutators for permutations:
        Reciprocal Exchange
        Insertion
        Displacement
        Inversion
         */

        Random random = new Random();

        SortedSet<Solution> mutatedPopulation = new TreeSet<>(new SolutionComparator());

        for (Solution solution : population) {
            double randomNumber = random.nextDouble();
            if (randomNumber < mutationFactor) {
                solution = reciprocalExchange(solution, graph);
            }
            mutatedPopulation.add(solution);
        }

        return mutatedPopulation;
    }

    private Solution reciprocalExchange(Solution solution, Graph graph) {
        Neighborhood neighborhood = new ThreeOptNeighborhoodNew();

        return neighborhood.get(solution, graph, StepType.RANDOM);
    }
}
