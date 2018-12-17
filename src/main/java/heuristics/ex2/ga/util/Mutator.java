package heuristics.ex2.ga.util;

import heuristics.ex1.dto.Graph;
import heuristics.ex1.dto.Solution;
import heuristics.ex1.localsearch.LocalSearch;
import heuristics.ex1.localsearch.neighborhood.Neighborhood;
import heuristics.ex1.localsearch.neighborhood.StepType;
import heuristics.ex1.localsearch.neighborhood.ThreeOptNeighborhoodNew;

import java.util.LinkedList;
import java.util.List;

public class Mutator {

    private Graph graph;

    public Mutator(Graph graph){
        this.graph = graph;
    }

    public List<Solution> mutate(List<Solution> population) {
        /*
        This class should take the new population from the recombinator and mutate them

        From slides (mutators for permutations:
        Reciprocal Exchange
        Insertion
        Displacement
        Inversion ¿?

        ¿Or just do random local search with first improvement?
         */

        Neighborhood neighborhood = new ThreeOptNeighborhoodNew();
        StepType stepType = StepType.RANDOM; //TODO try other steptype? First Improvement?
        LocalSearch localSearch = new LocalSearch(neighborhood, stepType);
        List<Solution> newPopulation = new LinkedList<>();

        for (Solution tour : population){
            tour = localSearch.improve(tour, graph);
            newPopulation.add(tour);
        }

        return newPopulation;
    }
}
