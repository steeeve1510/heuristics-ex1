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
    private double selectionSizeFactor;
    private SelectionType selectionType;

    /**
     *
     * @param selectionSizeFactor how many percent of the population should be chosen as parents (0.0 - 1.0)
     * @param type which selection algorithm should be chosen
     */
    public Selector(double selectionSizeFactor, SelectionType type) {
        this.selectionSizeFactor = selectionSizeFactor;
        this.selectionType = type;
    }

    public SortedSet<Solution> select(SortedSet<Solution> population) {
        switch (selectionType) {
            case LINEAR_RANKING:
                return getLinearRanking(population);
            case K_TOURNAMENT:
                return getKTournament(population);
        }
        return null;
    }

    private SortedSet<Solution> getLinearRanking(SortedSet<Solution> population) {
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

        return sample((int) Math.ceil(population.size() * selectionSizeFactor), probabilityMassFunction);
    }

    private SortedSet<Solution> getKTournament(SortedSet<Solution> population) {
        int n = population.size();

        int k = 10;

        List<Pair<Solution, Double>> probabilityMassFunction = new ArrayList<>();
        int i = 0;
        for (Solution s : population) {
            double p = ( Math.pow(n-i, k) - Math.pow(n-i-1, k) ) / Math.pow(n, k);
            probabilityMassFunction.add(new Pair<>(s, p));
            i++;
        }

        return sample((int) Math.ceil(population.size() * selectionSizeFactor), probabilityMassFunction);
    }

    private SortedSet<Solution> sample(int amount, List<Pair<Solution, Double>> probabilityMassFunction) {
        if (amount >= probabilityMassFunction.size()) {
            throw new RuntimeException("Population is too small, required more than: " + amount + ", actually: " + probabilityMassFunction.size());
        }

        EnumeratedDistribution<Solution> distribution = new EnumeratedDistribution<>(probabilityMassFunction);
        SortedSet<Solution> selected = new TreeSet<>(new SolutionComparator());
        while (selected.size() < amount) {
            Solution sample = distribution.sample();
            selected.add(sample);
        }
        return selected;
    }
}
