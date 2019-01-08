package heuristics.ex2.ga.util;

import heuristics.ex1.dto.Solution;

import java.util.SortedSet;
import java.util.TreeSet;

/**
 * This class takes the old population and the offspring and defines the new population
 */
public class Replacer {

    private int initPopulationSize;

    public Replacer(int initPopulationSize) {
        this.initPopulationSize = initPopulationSize;
    }

    public SortedSet<Solution> replace(SortedSet<Solution> parents, SortedSet<Solution> offspring) {
        SortedSet<Solution> newPopulation = new TreeSet<>(new SolutionComparator());
        newPopulation.addAll(limit(parents, 20));

        int counter = newPopulation.size();
        for (Solution child : offspring) {
            if (counter >= initPopulationSize * 1.5) {
                break;
            }
            newPopulation.add(child);
            counter++;
        }

        return newPopulation;
    }

    private SortedSet<Solution> limit(SortedSet<Solution> population, long limit) {
        SortedSet<Solution> restricted = new TreeSet<>(new SolutionComparator());

        long counter = 0;
        for (Solution solution : population) {
            if (counter >= limit) {
                break;
            }
            restricted.add(solution);
            counter++;
        }

        return restricted;
    }
}
