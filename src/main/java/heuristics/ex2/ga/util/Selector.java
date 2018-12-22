package heuristics.ex2.ga.util;

import heuristics.ex1.dto.Solution;
import org.apache.commons.math3.distribution.EnumeratedDistribution;
import org.apache.commons.math3.util.Pair;

import java.util.*;

public class Selector {
    /*
        This class should select the parents that will be used for the next generation
        Questions / Decicions:
        How to select?
            Rank Selection
            Linear ranking
            K-tournament (another extra parameter)
        Select how many?
     */
    private SelectionType selectionType;

    public Selector(SelectionType type) {
        this.selectionType = type;
    }

    public SortedSet<Solution> select(SortedSet<Solution> population, int selectionSize) {
        switch (selectionType) {
            case LINEAR_RANKING:
                return getLinearRanking(population, selectionSize);
            case K_TOURNAMENT:
                return getKTournament(population, selectionSize);
        }
        return null;
    }

    private SortedSet<Solution> getLinearRanking(SortedSet<Solution> population, int selectionSize) {
        int n = population.size();

        double alpha = 2d;
        double beta = 0d;

        List<Pair<Solution, Double>> probabilityMassFunction = new ArrayList<>();
        int i = 0;
        for (Solution s : population) {
            double p = (alpha + i * (beta - alpha) / (n - 1d)) / n;
            probabilityMassFunction.add(new Pair<>(s, p));
            i++;
        }

        return sample(selectionSize, probabilityMassFunction);
    }

    private SortedSet<Solution> getKTournament(SortedSet<Solution> population, int selectionSize) {
        int n = population.size();

        int k = 10;

        List<Pair<Solution, Double>> probabilityMassFunction = new ArrayList<>();
        int i = 0;
        for (Solution s : population) {
            double p = ( Math.pow(n-i, k) - Math.pow(n-i-1, k) ) / Math.pow(n, k);
            probabilityMassFunction.add(new Pair<>(s, p));
            i++;
        }

        return sample(selectionSize, probabilityMassFunction);
    }

    private SortedSet<Solution> sample(int amount, List<Pair<Solution, Double>> probabilityMassFunction) {
        EnumeratedDistribution<Solution> distribution = new EnumeratedDistribution<>(probabilityMassFunction);

        SortedSet<Solution> selected = new TreeSet<>(new SolutionComparator());
        Solution[] sample = distribution.sample(amount, new Solution[]{});
        Collections.addAll(selected, sample);
        return selected;
    }
}
