package heuristics.ex1;

import heuristics.ex1.build.GraphBuilder;
import heuristics.ex1.construction.ConstructionHeuristic;
import heuristics.ex1.construction.GreedyConstructionHeuristic;
import heuristics.ex1.construction.RandomConstructionHeuristic;
import heuristics.ex1.dto.Graph;
import heuristics.ex1.dto.Solution;
import heuristics.ex1.localsearch.LocalSearch;
import heuristics.ex1.localsearch.neighborhood.Neighborhood;
import heuristics.ex1.localsearch.neighborhood.TwoOptNeighborhood;
import heuristics.ex1.localsearch.step.RandomStep;
import heuristics.ex1.localsearch.step.Step;

import java.io.File;
import java.io.IOException;

public class App {

    private static String instance = "0500";

    private static ConstructionHeuristic constructionHeuristic = new GreedyConstructionHeuristic();
    private static ConstructionHeuristic constructionHeuristic2 = new RandomConstructionHeuristic();

    private static Neighborhood neighborhood = new TwoOptNeighborhood();
    private static Step step = new RandomStep();

    public static void main(String[] args) throws IOException {

        File file = new File("src/main/resources/" + instance + ".txt");

        GraphBuilder graphBuilder = new GraphBuilder();
        LocalSearch localSearch = new LocalSearch(neighborhood, step);


        Graph graph = graphBuilder.build(file);
        Solution solution = constructionHeuristic.solve(graph);
        solution = localSearch.improve(solution, graph);

        System.out.println(solution);

        solution = constructionHeuristic2.solve(graph);
        solution = localSearch.improve(solution, graph);

        System.out.println(solution);
    }
}
