package heuristics.ex1.dto;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.ArrayUtils;

import java.util.*;
import java.util.stream.Collectors;

@Getter
public class Solution {

    private List<Integer> nodes;
    private Integer[] nodesAsArray;
    private Map<Integer, Integer> indexLookUp = new HashMap<>();

    private long objectiveValue;

    @Getter
    @Setter
    private boolean timedOut = false;

    @Getter
    @Setter
    private long fitness;

    public Solution(List<Integer> nodes, long objectiveValue) {
        this.nodes = nodes;
        this.nodesAsArray = nodes.toArray(new Integer[]{});
        for (int i = 0; i < nodesAsArray.length; i++) {
            int city = nodesAsArray[i];
            indexLookUp.put(city, i);
        }

        this.objectiveValue = objectiveValue;
    }

    public long getAbsoluteObjectiveValue() {
        return Math.abs(objectiveValue);
    }

    @Override
    public String toString() {
        return nodes.stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(" "));
    }

    public int get(int index) {
        return nodes.get(index);
    }

    public int getSuccessor(int node) {
        int index = indexLookUp.get(node);
        if (index < 0) {
            throw new IllegalArgumentException("Solution does not contain node " + node);
        }
        int indexOfSuccessor = (index+1) % nodes.size();
        return nodesAsArray[indexOfSuccessor];
    }

    public int getPredecessor(int node) {
        int index = indexLookUp.get(node);
        if (index < 0) {
            throw new IllegalArgumentException("Solution does not contain node " + node);
        }
        int indexOfPredecessor = Math.floorMod(index-1, nodes.size());
        return nodesAsArray[indexOfPredecessor];
    }

    public List<Integer> getPart(int from, int to) {
        int fromIndex = nodes.indexOf(from);
        int toIndex = nodes.indexOf(to);

        if (fromIndex < 0 || toIndex < 0) {
            throw new IllegalArgumentException("Cannot get part from " + from + " to " + to);
        }

        Integer[] nodesAsArray = nodes.toArray(new Integer[]{});

        Integer[] partAsArray;
        if (fromIndex <= toIndex) {
            partAsArray = Arrays.copyOfRange(nodesAsArray, fromIndex, toIndex, Integer[].class);
        } else {
            partAsArray = Arrays.copyOfRange(nodesAsArray, fromIndex, nodesAsArray.length, Integer[].class);
            Integer[] secondPartAsArray = Arrays.copyOfRange(nodesAsArray, 0, toIndex, Integer[].class);

            partAsArray = ArrayUtils.addAll(partAsArray, secondPartAsArray);
        }
        return new ArrayList<>(Arrays.asList(partAsArray));
    }

    public int getSize() {
        return nodes.size();
    }
}
