package heuristics.ex2.ga.util;

import heuristics.ex1.dto.Solution;

import java.util.SortedSet;
import java.util.TreeSet;

/**
 * This class takes the old population and the offspring and defines the new population
 */
public class Replacer {

    public SortedSet<Solution> replace(SortedSet<Solution> parents, SortedSet<Solution> offspring) {
        SortedSet<Solution> newPopulation = new TreeSet<>(new SolutionComparator());
        newPopulation.addAll(parents);
        newPopulation.addAll(offspring);

        return newPopulation;
    }
}
