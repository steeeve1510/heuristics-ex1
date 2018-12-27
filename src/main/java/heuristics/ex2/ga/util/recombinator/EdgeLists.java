package heuristics.ex2.ga.util.recombinator;

import heuristics.ex1.dto.Solution;

import java.util.*;

public class EdgeLists {

    private Map<Integer, Set<Integer>> edgeLists = new HashMap<>();
    private Map<Integer, List<Set<Integer>>> appearances = new HashMap<>();

    public Set<Integer> getCities() {
        return edgeLists.keySet();
    }

    public Set<Integer> getEdgeList(Integer city) {
        return edgeLists.get(city);
    }

    public void remove(Integer city) {
        edgeLists.remove(city);
    }

    public void addSolution(Solution solution) {
        for (Integer c : solution.getNodes()) {
            Set<Integer> edgeList = edgeLists.get(c);
            if (edgeList == null) {
                edgeList = new HashSet<>();
            }

            Integer predecessor = solution.getPredecessor(c);
            Integer successor = solution.getSuccessor(c);
            edgeList.add(predecessor);
            edgeList.add(successor);
            edgeLists.put(c, edgeList);
        }
    }

    public void generateAppearanceList() {
        appearances.clear();
        for (Set<Integer> edgeList : edgeLists.values()) {
            for (Integer city : edgeList) {
                List<Set<Integer>> appearance = appearances.get(city);
                if (appearance == null) {
                    appearance = new LinkedList<>();
                }
                appearance.add(edgeList);
                appearances.put(city, appearance);
            }
        }
    }

    public void removeCityFromEdgeLists(Integer city) {
        for (Set<Integer> edgeList : appearances.get(city)) {
            edgeList.remove(city);
        }
        appearances.remove(city);
    }
}
