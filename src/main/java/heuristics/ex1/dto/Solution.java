package heuristics.ex1.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class Solution {

    private List<Integer> nodes;
    private long objectiveValue;

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
        int index = nodes.indexOf(node);
        if (index < 0) {
            throw new IllegalArgumentException("Solution does not contain node " + node);
        }
        int indexOfSuccessor = (index+1) % nodes.size();
        return nodes.get(indexOfSuccessor);
    }

    public int getPredecessor(int node) {
        int index = nodes.indexOf(node);
        if (index < 0) {
            throw new IllegalArgumentException("Solution does not contain node " + node);
        }
        int indexOfSuccessor = Math.floorMod(index-1, nodes.size());
        return nodes.get(indexOfSuccessor);
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
