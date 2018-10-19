package heuristics.ex1;

import heuristics.ex1.build.GraphBuilder;
import heuristics.ex1.construction.ConstructionHeuristic;
import heuristics.ex1.construction.GreedyConstructionHeuristic;
import heuristics.ex1.dto.Graph;
import heuristics.ex1.dto.Solution;

import java.io.File;

public class App {

    public static void main(String[] args) {
        File file = new File("src/main/resources/0010.txt");


        ConstructionHeuristic constructionHeuristic = new GreedyConstructionHeuristic();


        Graph graph = new GraphBuilder().build(file);

        Solution solution = constructionHeuristic.solve(graph);


        System.out.println(solution);
    }
}
