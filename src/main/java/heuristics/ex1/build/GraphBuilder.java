package heuristics.ex1.build;

import heuristics.ex1.dto.Edge;
import heuristics.ex1.dto.Graph;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GraphBuilder {

    public Graph build(File file) {
        List<String> lines;
        try {
            lines = Files.lines(file.toPath()).collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Error loading file", e);
        }

        List<Edge> edges = lines.stream()
                            .skip(1)
                            .map(lineToEdge())
                            .collect(Collectors.toList());

        check(lines.get(0), edges);

        int bigM = getBigM(edges);

        Set<Integer> nodes = edges.stream()
                                .flatMap(e -> Stream.of(e.getNode1(), e.getNode2()))
                                .collect(Collectors.toSet());

        Map<Integer, Map<Integer, Integer>> matrix = new HashMap<>();
        for (int node1 : nodes) {
            Map<Integer, Integer> row = new HashMap<>();
            for (int node2 : nodes) {
                if (node1 != node2) {
                    row.put(node2, bigM);
                }
            }
            matrix.put(node1, row);
        }

        edges.forEach(e -> {
            matrix.get(e.getNode1()).put(e.getNode2(), e.getWeight());
            matrix.get(e.getNode2()).put(e.getNode1(), e.getWeight());
        });

        return new Graph(matrix);
    }

    private Function<String, Edge> lineToEdge() {
        return line -> {
            String[] parts = line.split(" ");
            if (parts.length != 3) {
                throw new RuntimeException("line has " + parts.length + " arguments");
            }
            int node1 = Integer.valueOf(parts[0]);
            int node2 = Integer.valueOf(parts[1]);
            int weight = Integer.valueOf(parts[2]);

            return new Edge(node1, node2, weight);
        };
    }

    private void check(String firstLine, List<Edge> edges) {

        String[] firstLineParts = firstLine.split(" ");
        int numberOfNodes = Integer.valueOf(firstLineParts[0]);
        int numberOfEdges = Integer.valueOf(firstLineParts[1]);

        int actualNumberOfNodes = edges.stream()
                .flatMap(e -> Stream.of(e.getNode1(), e.getNode2()))
                .collect(Collectors.toSet())
                .size();

        if (actualNumberOfNodes != numberOfNodes) {
            throw new RuntimeException("Loaded invalid number of nodes, expected: " + numberOfNodes + ", actual: " + actualNumberOfNodes);
        }
        if (edges.size() != numberOfEdges) {
            throw new RuntimeException("Loaded invalid number of edges, expected: " + numberOfEdges + ", actual: " + edges.size());
        }
    }

    private int getBigM(List<Edge> edges) {
        int positiveSum = edges.stream()
                            .filter(e -> e.getWeight() > 0)
                            .mapToInt(Edge::getWeight)
                            .sum();

        int negativeSum = edges.stream()
                            .filter(e -> e.getWeight() < 0)
                            .mapToInt(e -> Math.abs(e.getWeight()))
                            .sum();

        return Math.max(positiveSum, negativeSum) * 2 + 1;
    }
}
