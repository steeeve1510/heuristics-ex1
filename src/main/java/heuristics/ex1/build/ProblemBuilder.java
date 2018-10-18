package heuristics.ex1.build;

import heuristics.ex1.dto.Edge;
import heuristics.ex1.dto.Problem;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ProblemBuilder {

    public Problem build(File file) {
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

        return new Problem(edges);
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
}
