package heuristics.ex2;

import heuristics.ex1.AppEx1;
import heuristics.ex1.build.GraphBuilder;
import heuristics.ex1.dto.Graph;
import heuristics.ex1.dto.Solution;
import heuristics.ex2.ga.GeneticAlgorithm;

import java.io.File;

public class AppEx2 {

    private static String instance = "0010";

    private static GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm();

    public static void main(String[] args) {

        File file = new File("src/main/resources/" + instance + ".txt");

        GraphBuilder graphBuilder = new GraphBuilder();
        Graph graph = graphBuilder.build(file);

        Solution solution = geneticAlgorithm.solve(graph);

        AppEx1.printResult(graph, solution);
    }
}
