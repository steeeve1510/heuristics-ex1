package heuristics.ex2.ga.util;

import heuristics.ex1.dto.Graph;
import heuristics.ex1.dto.Solution;
import heuristics.ex1.localsearch.LocalSearch;
import heuristics.ex1.localsearch.neighborhood.StepType;
import heuristics.ex1.localsearch.neighborhood.ThreeOptNeighborhoodNew;

import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;

public class HybridMutator{

        public SortedSet<Solution> mutate(SortedSet<Solution> population, Graph graph) {
        /*
        This class should take the new population from the recombinator and do local search to all of them
         */

            LocalSearch localSearch = new LocalSearch(new ThreeOptNeighborhoodNew(), StepType.RANDOM);
            //TODO Experiment with different StepTypes

            SortedSet<Solution> mutatedPopulation = new TreeSet<>(new SolutionComparator());
            Solution mutatedSolution;

            for (Solution solution : population) {
                mutatedSolution = localSearch.improve(solution, graph);
                mutatedPopulation.add(mutatedSolution);
            }

            return mutatedPopulation;
        }
}
