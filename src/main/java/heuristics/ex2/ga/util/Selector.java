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

        Comparator<Solution> tourComparator = new SolutionComparator();
        SortedSet<Solution> ranking = new TreeSet<>(tourComparator);
        ranking.addAll(population);
        List<Pair<Solution,Double>> itemWeights = new ArrayList<>();
        double prob = 0.1;
        for (Solution i : ranking) {
            itemWeights.add(new Pair<>(i, prob));
            prob += 0.1;
        }

        return Arrays.asList(new EnumeratedDistribution<>(itemWeights).sample(parents, new Solution[]{}));
    }

    private List<Solution> getKTournament(List<Solution> population, int parents){
        int tournamentSize = population.size() / parents;

        Comparator<Solution> tourComparator = new SolutionComparator();
        SortedSet<Solution> tournament = new TreeSet<>(tourComparator);
        Random RNG = new Random();

        List<Solution> newPopulation = new LinkedList<>();
        for (int i = 0;  i < parents; i++) {
            tournament.clear();
            if (i == parents-1 ){
                // Adds the last remaining solutions in case there is a remainder in size / parents
                for (int j = 0;  j< (tournamentSize + (population.size() % parents)); j++) {
                    int randomSolution = RNG.nextInt(population.size());
                    tournament.add(population.get(randomSolution));
                    population.remove(randomSolution);
                }
            } else {
                for (int j = 0;  j< tournamentSize; j++) {
                    int randomSolution = RNG.nextInt(population.size());
                    tournament.add(population.get(randomSolution));
                    population.remove(randomSolution);
                }
            }
            newPopulation.add(tournament.first());
        }
        return newPopulation;
    }

}
