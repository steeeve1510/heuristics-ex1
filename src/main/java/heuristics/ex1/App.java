package heuristics.ex1;

import heuristics.ex1.build.ProblemBuilder;
import heuristics.ex1.dto.Problem;

import java.io.File;

public class App {

    public static void main(String[] args) {
        File file = new File("src/main/resources/0010.txt");

        Problem problem = new ProblemBuilder().build(file);

        System.out.println(problem);
    }
}
