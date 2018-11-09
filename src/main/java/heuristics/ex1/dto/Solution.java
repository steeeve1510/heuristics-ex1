package heuristics.ex1.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public List<Integer> getPart(int from, int to) {
        int fromIndex = nodes.indexOf(from);
        int toIndex = nodes.indexOf(to);

        if (fromIndex < 0 || toIndex < 0) {
            throw new IllegalArgumentException("Cannot get part from " + from + " to " + to);
        }

        Integer[] nodesAsArray = nodes.toArray(new Integer[]{});


        List<Integer> part;
        Integer[] partAsArray;
        if (fromIndex <= toIndex) {
            partAsArray = Arrays.copyOfRange(nodesAsArray, fromIndex, toIndex, Integer[].class);

            part = Arrays.asList(partAsArray);
        } else {
            partAsArray = Arrays.copyOfRange(nodesAsArray, fromIndex, nodesAsArray.length, Integer[].class);
            Integer[] secondPartAsArray = Arrays.copyOfRange(nodesAsArray, 0, toIndex, Integer[].class);

            part = Stream.concat(
                    Arrays.stream(partAsArray),
                    Arrays.stream(secondPartAsArray)
            ).collect(Collectors.toList());
        }
        return part;
    }

    public int getSize() {
        return nodes.size();
    }
}
