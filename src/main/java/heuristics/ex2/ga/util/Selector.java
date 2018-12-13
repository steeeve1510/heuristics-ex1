package heuristics.ex2.ga.util;

import heuristics.ex1.dto.Solution;
import org.apache.commons.math3.distribution.EnumeratedDistribution;
import org.apache.commons.math3.util.Pair;

import java.util.*;

import static heuristics.ex2.ga.util.SelectionType.K_TOURNAMENT;
import static heuristics.ex2.ga.util.SelectionType.LINEAR_RANKING;

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

    public Selector(SelectionType type){
        this.selectionType = type;
    }

    public List<Solution> select(List<Solution> population, int parents) {
        switch (selectionType) {
                    case LINEAR_RANKING:
                        return getLinearRanking(population, parents);
                    case K_TOURNAMENT:
                        return getKTournament(population, parents);
        //            case RANK:
        //                return getRankSelection((population, parents);
        }
        return null;
    }

    private List<Solution> getLinearRanking(List<Solution> population, int parents){

        Comparator tourComparator = new SolutionComparator();
        SortedSet ranking = new TreeSet(tourComparator);
        ranking.addAll(population);
        List<Pair<Solution,Double>> itemWeights = new ArrayList<>();
        double prob = 0.1;
        for (Object i : ranking) {
            itemWeights.add(new Pair(i, prob));
            prob += 0.1;
        }

        List<Solution> newPopulation = Arrays.asList((Solution[]) new EnumeratedDistribution(itemWeights).sample(parents));

        return newPopulation;
    }

    private List<Solution> getKTournament(List<Solution> population, int parents){
        //TODO (or not) implement
        return population;
    }

}
