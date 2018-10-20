package heuristics.ex1.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

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
}
