package heuristics.ex2.ga.util;

import heuristics.ex1.dto.Graph;
import heuristics.ex1.dto.Solution;

import java.util.*;

public class Recombinator {

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
        }
        return newPopulation;
    }

    private Solution edgeRecombinationCrossover(Solution father, Solution mother, Graph graph) {
        Map<Integer, Set<Integer>> edgeLists = new HashMap<>();
        addSolutionToEdgeLists(father, edgeLists);
        addSolutionToEdgeLists(mother, edgeLists);

        List<Integer> cities = new LinkedList<>();
        long objectiveValue = 0;

        Integer city = father.getNodes().get(0);
        cities.add(city);
        removeCityFromAllLists(city, edgeLists);

        while (edgeLists.keySet().size() != 1) {
            Set<Integer> edgeList = edgeLists.get(city);
            edgeLists.remove(city);

            Integer bestNeighbor = getBestNeighbor(city, edgeList, graph);
            if (bestNeighbor == null) {
                Integer[] remainingCities = edgeLists.keySet().toArray(new Integer[]{});
                bestNeighbor = remainingCities[new Random().nextInt(remainingCities.length)];
            }

            removeCityFromAllLists(bestNeighbor, edgeLists);

            cities.add(bestNeighbor);
            objectiveValue += graph.getWeight(city, bestNeighbor);

            city = bestNeighbor;
        }

        objectiveValue += graph.getWeight(city, cities.get(0));

        return new Solution(cities, objectiveValue);
    }

    private void addSolutionToEdgeLists(Solution solution, Map<Integer, Set<Integer>> edgeLists) {
        solution.getNodes().forEach(c -> {
            Set<Integer> edgeList = edgeLists.get(c);
            if (edgeList == null) {
                edgeList = new HashSet<>();
            }
            edgeList.add(solution.getPredecessor(c));
            edgeList.add(solution.getSuccessor(c));
            edgeLists.put(c, edgeList);
        });
    }

    private Integer getBestNeighbor(Integer city, Set<Integer> neighbors, Graph graph) {
        List<Integer> closestNeighbors = new ArrayList<>();

        int cheapestEdge = 0;
        for(Integer neighbor : neighbors) {
            int edge = graph.getWeight(city, neighbor);

            if (closestNeighbors.isEmpty()) {
                closestNeighbors.add(neighbor);
                cheapestEdge = edge;
                continue;
            }

            if (edge < cheapestEdge) {
                closestNeighbors.clear();
                closestNeighbors.add(neighbor);
                cheapestEdge = edge;
            } else if (edge == cheapestEdge) {
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

    private void removeCityFromAllLists(Integer city, Map<Integer, Set<Integer>> edgeLists) {
        edgeLists.forEach((c, edgeList) -> {
            edgeList.remove(city);
        });
    }
}
