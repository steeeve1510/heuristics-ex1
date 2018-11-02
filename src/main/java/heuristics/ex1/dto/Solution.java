package heuristics.ex1.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class Solution {

    private List<Integer> nodes;
    private int objectiveValue;

    public int getAbsoluteObjectiveValue() {
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

        List<Integer> part;
        if (fromIndex <= toIndex) {
            part = nodes.subList(fromIndex, toIndex).stream().map(Integer::new).collect(Collectors.toList());
        } else {
            part = nodes.subList(fromIndex, nodes.size()).stream().map(Integer::new).collect(Collectors.toList());;
            part.addAll(nodes.subList(0, toIndex).stream().map(Integer::new).collect(Collectors.toList()));
        }

        return part.stream()
                    .map(Integer::new)
                    .collect(Collectors.toList());
    }

    public int getSize() {
        return nodes.size();
    }
}
