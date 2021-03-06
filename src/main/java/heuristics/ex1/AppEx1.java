package heuristics.ex1;

import heuristics.ex1.build.GraphBuilder;
import heuristics.ex1.construction.ConstructionHeuristic;
import heuristics.ex1.construction.GreedyConstructionHeuristic;
import heuristics.ex1.construction.RandomConstructionHeuristic;
import heuristics.ex1.dto.Graph;
import heuristics.ex1.dto.Solution;
import heuristics.ex1.grasp.GRASP;
import heuristics.ex1.localsearch.LocalSearch;
import heuristics.ex1.localsearch.neighborhood.*;
import heuristics.ex1.sa.SimulatedAnnealing;
import heuristics.ex1.vnd.VND;

import java.io.File;
import java.io.IOException;

public class AppEx1 {

    private static String instance = "0150";

    private static ConstructionHeuristic constructionHeuristic = new GreedyConstructionHeuristic();
    private static ConstructionHeuristic constructionHeuristic2 = new RandomConstructionHeuristic();

    private static Neighborhood neighborhood = new ThreeOptNeighborhoodNew();
    private static StepType stepType = StepType.BEST_IMPOVEMENT;

    public static void main(String[] args) throws IOException {

        File file = new File("src/main/resources/" + instance + ".txt");

        GraphBuilder graphBuilder = new GraphBuilder();
        Graph graph = graphBuilder.build(file);

        LocalSearch localSearch = new LocalSearch(neighborhood, stepType);

        Solution solution = constructionHeuristic.solve(graph);

//        solution = localSearch.improve(solution, graph);
//
//        System.out.println("Improved:");
//        System.out.println(solution);
//        System.out.println(solution.getObjectiveValue());
//
//        Solution solution = constructionHeuristic2.solve(graph);
//        solution = localSearch.improve(solution, graph);
//
//        System.out.println(solution);


//        GRASP grasp = new GRASP();
//        Solution solution = grasp.solve(graph);
//
        VND vnd = new VND();
        solution = vnd.improve(solution, graph);

//        SimulatedAnnealing sa = new SimulatedAnnealing();
//        Solution solution = sa.solve(graph);

        printResult(graph, solution);
    }

    public static void printResult(Graph graph, Solution solution) {
        System.out.println();
        if (solution.getObjectiveValue() != getObjectiveValue(solution,graph)) {
            System.out.println("#########################################");
            System.out.println("Solution is not correct");
            System.out.println("Objective value:              " + solution.getObjectiveValue());
            System.out.println("Recalculated objective value: " + getObjectiveValue(solution,graph));
            System.out.println("#########################################");
        }
        if (solution.getSize() != graph.getMatrix().keySet().size()) {
            System.out.println("#########################################");
            System.out.println("Solution is not correct");
            System.out.println("Solution size: " + solution.getSize());
            System.out.println("Matrix size  : " + graph.getMatrix().keySet().size());
            System.out.println("#########################################");
        }
        System.out.println("Objective value: " + solution.getObjectiveValue());
        boolean isInfeasible = isInfeasible(solution, graph);
        System.out.println("The solution is " + (isInfeasible ? "INFEASIBLE!!" : "feasible"));
        System.out.println();
        System.out.println(solution);
    }

    public static long getObjectiveValue(Solution solution, Graph graph) {
        long value = 0;
        for (int i = 0; i < solution.getSize(); i++) {
            int j = Math.floorMod(i+1, solution.getSize());

            int x = solution.get(i);
            int y = solution.get(j);

            int weight = graph.getWeight(x, y);

            value += weight;
        }
        return value;
    }

    public static boolean isInfeasible(Solution solution, Graph graph) {
        int bigM = graph.getBigM();

        boolean infeasible = false;
        for (int i = 0; i < solution.getSize(); i++) {
            int j = Math.floorMod(i+1, solution.getSize());

            int x = solution.get(i);
            int y = solution.get(j);

            int weight = graph.getWeight(x, y);

            if (weight == bigM) {
                System.out.println("Edge from " + x + " to " + y + " is infeasible");
                infeasible = true;
            }
        }
        return infeasible;
    }
}
