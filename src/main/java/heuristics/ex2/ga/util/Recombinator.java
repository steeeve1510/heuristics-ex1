package heuristics.ex2.ga.util;

import heuristics.ex1.dto.Graph;
import heuristics.ex1.dto.Solution;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Recombinator {

    private Graph graph;

    public Recombinator(Graph graph){
        this.graph = graph;
    }

    public List<Solution> recombine(List<Solution> parents) {
        /*
        This class should take the parents from selector and build a whole new generation from them.
        Should we use Edge Recombination Crossover? (Recommended in slides)
         */
        Solution combination;
        List<Solution> newPopulation = new LinkedList<>();
        for (Solution father: parents){
            parents.remove(father);
            for (Solution mother: parents){
                combination = edgeRecombinationCrossover(father, mother);
                newPopulation.add(combination);
            }
        }
        return newPopulation;
    }

    private Solution edgeRecombinationCrossover(Solution father, Solution mother) {
        List<Integer> cityEdgeList;
        List<List<Integer>> citiesEdgeList = new LinkedList<>();
        for (int i = 0; i < father.getSize(); i++){
            cityEdgeList = new LinkedList<>();
            cityEdgeList.add(father.getPredecessor(i));
            cityEdgeList.add(father.getSuccessor(i));
            cityEdgeList.add(mother.getPredecessor(i));
            cityEdgeList.add(mother.getSuccessor(i));
            citiesEdgeList.add(cityEdgeList);
        }

        Random RNG = new Random();
        List<Integer> combination = new LinkedList<>();
        int currentCity = RNG.nextInt(citiesEdgeList.size());
        int initialCity = currentCity;
        List<Integer> currentCityEdgeList;
        Long newObjectiveValue = 0L;
        combination.add(currentCity);
        for (int i = 0; i < father.getSize(); i++){
            combination.add(currentCity);
            currentCityEdgeList = citiesEdgeList.get(currentCity);
            if (!currentCityEdgeList.isEmpty()) {
                //citiesEdgeList.remove(currentCity);       // This would fuck up the list structure, indexes will no longer equal city name
                removeCityFromEdgeList(currentCity, citiesEdgeList); //TODO testing. Does this method work or do I have to build a list again
                int selectedCity = currentCityEdgeList.get(0);
                int selCityEdges = citiesEdgeList.get(selectedCity).size();
                for (int j = 1; j < currentCityEdgeList.size(); i++) {
                    if (selCityEdges > citiesEdgeList.get(currentCityEdgeList.get(i)).size()) {
                        selectedCity = currentCityEdgeList.get(i);
                        selCityEdges = citiesEdgeList.get(currentCityEdgeList.get(i)).size();
                    }
                }
                newObjectiveValue += graph.getWeight(currentCity, selectedCity);
            } else {
                //last city, edge list is empty
                newObjectiveValue += graph.getWeight(currentCity, initialCity);
            }
        }
        return new Solution(combination, newObjectiveValue);
    }

    private void removeCityFromEdgeList(int currentCity, List<List<Integer>> citiesEdgeList) {
        List<Integer> currentCityEdgeList = citiesEdgeList.get(currentCity);
        for (int i = 0; i< currentCityEdgeList.size(); i++){
            citiesEdgeList.get(currentCityEdgeList.get(i)).remove((Integer) currentCity);
        }
    }

}
