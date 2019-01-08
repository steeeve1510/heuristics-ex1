package heuristics.ex2.ga.util;

import heuristics.ex1.dto.Graph;
import heuristics.ex1.dto.Solution;
import heuristics.ex2.ga.util.recombinator.EdgeLists;

import java.util.*;

public class Recombinator {

    private int initPopulationSize;

    public Recombinator(int initPopulationSize) {
        this.initPopulationSize = initPopulationSize;
    }

    public SortedSet<Solution> recombine(SortedSet<Solution> parents, Graph graph) {
        SortedSet<Solution> newPopulation = new TreeSet<>(new SolutionComparator());
        for (Solution father : parents) {
            SortedSet<Solution> otherParents = parents.tailSet(father);
            for (Solution mother : otherParents) {
                if (father == mother) {
                    continue;
                }
                Solution offspring = edgeRecombinationCrossover(father, mother, graph);
                newPopulation.add(offspring);
            }
            newPopulation = limit(newPopulation, initPopulationSize * 100);
        }
        return newPopulation;
    }

    private Solution edgeRecombinationCrossover(Solution father, Solution mother, Graph graph) {
        EdgeLists edgeLists = new EdgeLists();
        edgeLists.addSolution(father);
        edgeLists.addSolution(mother);
        edgeLists.generateAppearanceList();

        List<Integer> cities = new LinkedList<>();
        long objectiveValue = 0;

        Integer city = father.getNodes().get(0);
        cities.add(city);
        edgeLists.removeCityFromEdgeLists(city);

        while (edgeLists.getCities().size() != 1) {
            Set<Integer> edgeList = edgeLists.getEdgeList(city);
            edgeLists.remove(city);

            Integer bestNeighbor = getBestNeighbor(city, edgeList, graph, objectiveValue);
            if (bestNeighbor == null) {
                Integer[] remainingCities = edgeLists.getCities().toArray(new Integer[]{});
                bestNeighbor = remainingCities[new Random().nextInt(remainingCities.length)];
            }

            edgeLists.removeCityFromEdgeLists(bestNeighbor);

            cities.add(bestNeighbor);
            objectiveValue += graph.getWeight(city, bestNeighbor);

            city = bestNeighbor;
        }

        objectiveValue += graph.getWeight(city, cities.get(0));

        return new Solution(cities, objectiveValue);
    }

    private Integer getBestNeighbor(Integer city, Set<Integer> neighbors, Graph graph, long objectiveValueSoFar) {
        List<Integer> closestNeighbors = new ArrayList<>();

        long cheapestObjectiveValue = 0;
        for (Integer neighbor : neighbors) {
            int edge = graph.getWeight(city, neighbor);

            if (closestNeighbors.isEmpty()) {
                closestNeighbors.add(neighbor);
                cheapestObjectiveValue = objectiveValueSoFar + edge;
                continue;
            }

            long currentObjectiveValue = objectiveValueSoFar + edge;
            if (Math.abs(currentObjectiveValue) < Math.abs(cheapestObjectiveValue)) {
                closestNeighbors.clear();
                closestNeighbors.add(neighbor);
                cheapestObjectiveValue = objectiveValueSoFar + edge;
            } else if (currentObjectiveValue == cheapestObjectiveValue) {
                closestNeighbors.add(neighbor);
            }
        }

        Integer bestNeighbor;
        if (closestNeighbors.isEmpty()) {
            bestNeighbor = null;
        } else if (closestNeighbors.size() == 1) {
            bestNeighbor = closestNeighbors.get(0);
        } else {
            Random random = new Random();
            int randomNeighbor = random.nextInt(closestNeighbors.size());
            bestNeighbor = closestNeighbors.get(randomNeighbor);
        }
        return bestNeighbor;
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
