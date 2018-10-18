package heuristics.ex1;

import heuristics.ex1.build.ProblemBuilder;
import heuristics.ex1.construction.ConstructionHeuristic;
import heuristics.ex1.construction.GreedyConstructionHeuristic;
import heuristics.ex1.dto.Problem;
import heuristics.ex1.dto.Solution;

import java.io.File;

public class App {

    public static void main(String[] args) {
        File file = new File("src/main/resources/0010.txt");


        ConstructionHeuristic constructionHeuristic = new GreedyConstructionHeuristic();


        Problem problem = new ProblemBuilder().build(file);

        Solution solution = constructionHeuristic.solve(problem);


        System.out.println(solution);
    }
}
