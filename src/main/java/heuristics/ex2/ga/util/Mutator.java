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
    private int mutationsCounterFactor;

    /*
     * @param mutationFactor how many percent of the population should mutate. (0.0 - 1.0)
     * @param mutationsPerSolutionFactor this factor defines how many mutations per solution are applies
     *          (e.g. 90 = one mutation per 90 cities)
     */
    public Mutator(double mutationFactor, int mutationsPerSolutionFactor) {
        this.mutationFactor = mutationFactor;
        this.mutationsCounterFactor = mutationsPerSolutionFactor;
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
                solution = inversion(solution, graph);
            }
            mutatedPopulation.add(solution);
        }

        return mutatedPopulation;
    }

    private Solution inversion(Solution solution, Graph graph) {
        int size = solution.getSize();

        Neighborhood neighborhood = new ThreeOptNeighborhoodNew();

        Solution mutated = solution;
        for (int i = 0; i < size / mutationsCounterFactor + 1; i++) {
            mutated = neighborhood.get(mutated, graph, StepType.RANDOM);
        }
        return mutated;
    }
}
