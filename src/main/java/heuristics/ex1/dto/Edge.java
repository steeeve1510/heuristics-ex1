package heuristics.ex1.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
@ToString
public class Edge {

    private int node1;
    private int node2;
    private int weight;
}
