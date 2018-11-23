package heuristics.ex1;

import heuristics.ex1.build.GraphBuilder;
import heuristics.ex1.construction.ConstructionHeuristic;
import heuristics.ex1.construction.GreedyConstructionHeuristic;
import heuristics.ex1.construction.RandomConstructionHeuristic;
import heuristics.ex1.dto.Graph;
import heuristics.ex1.dto.Solution;
import heuristics.ex1.localsearch.LocalSearch;
import heuristics.ex1.localsearch.neighborhood.StepType;
import heuristics.ex1.localsearch.neighborhood.TwoOptNeighborhood;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Benchmark {

    private static final int RUNS = 20;

    public static void main(String[] args) throws IOException {
        List<File> instances = getInstances();

        Printer printer = new Printer("03a - localsearch 2opt best.csv");

        printer.println("name,run,isInfeasible,timedOut,objectiveValue,solution");
        for (File instance : instances) {
            Graph graph = new GraphBuilder().build(instance);

//            greedyConstruction(instance, graph, printer);
//            randomConstruction(instance, graph, printer);

            localSearch_2opt_best(instance, graph, printer);
//            localSearch_2opt_next(instance, graph, printer);
//            localSearch_2opt_random(instance, graph, printer);
        }

        printer.close();
    }

    private static void greedyConstruction(File instance, Graph graph, Printer printer) {
        ConstructionHeuristic constructionHeuristic = new GreedyConstructionHeuristic();

        Solution solution = constructionHeuristic.solve(graph);

        String name = instance.getName().substring(0, instance.getName().length() - 4);
        boolean isInfeasible = isInfeasible(solution, graph);
        long objectiveValue = solution.getAbsoluteObjectiveValue();

        printer.println(name + "," + 1 + "," + isInfeasible + "," + objectiveValue + "," + solution);
    }

    private static void randomConstruction(File instance, Graph graph, Printer printer) {
        ConstructionHeuristic constructionHeuristic = new RandomConstructionHeuristic();

        for (int i = 1; i <= RUNS; i++) {
            Solution solution = constructionHeuristic.solve(graph);

            String name = instance.getName().substring(0, instance.getName().length() - 4);
            boolean isInfeasible = isInfeasible(solution, graph);
            long objectiveValue = solution.getAbsoluteObjectiveValue();

            printer.println(name + "," + i + "," + isInfeasible + "," + solution.isTimedOut() + "," + objectiveValue + "," + solution);
        }
    }

    private static void localSearch_2opt_best(File instance, Graph graph, Printer printer) {
        ConstructionHeuristic constructionHeuristic = new GreedyConstructionHeuristic();

        Solution solution = constructionHeuristic.solve(graph);

        LocalSearch localSearch = new LocalSearch(new TwoOptNeighborhood(), StepType.BEST_IMPOVEMENT);
        solution = localSearch.improve(solution, graph);

        String name = instance.getName().substring(0, instance.getName().length() - 4);
        boolean isInfeasible = isInfeasible(solution, graph);
        long objectiveValue = solution.getAbsoluteObjectiveValue();

        printer.println(name + "," + 1 + "," + isInfeasible + "," + solution.isTimedOut() + "," + objectiveValue + "," + solution);
    }

    private static void localSearch_2opt_next(File instance, Graph graph, Printer printer) {
        ConstructionHeuristic constructionHeuristic = new GreedyConstructionHeuristic();

        Solution solution = constructionHeuristic.solve(graph);

        LocalSearch localSearch = new LocalSearch(new TwoOptNeighborhood(), StepType.NEXT_IMPROVEMENT);
        solution = localSearch.improve(solution, graph);

        String name = instance.getName().substring(0, instance.getName().length() - 4);
        boolean isInfeasible = isInfeasible(solution, graph);
        long objectiveValue = solution.getAbsoluteObjectiveValue();

        printer.println(name + "," + 1 + "," + isInfeasible + "," + solution.isTimedOut() + "," + objectiveValue + "," + solution);
    }

    private static void localSearch_2opt_random(File instance, Graph graph, Printer printer) {
        ConstructionHeuristic constructionHeuristic = new GreedyConstructionHeuristic();

        Solution solution = constructionHeuristic.solve(graph);

        // 10000 unsuccessfulImprovements
        LocalSearch localSearch = new LocalSearch(new TwoOptNeighborhood(), StepType.RANDOM);
        for (int i = 1; i <= RUNS; i++) {
            Solution improvedSolution = localSearch.improve(solution, graph);

            String name = instance.getName().substring(0, instance.getName().length() - 4);
            boolean isInfeasible = isInfeasible(improvedSolution, graph);
            long objectiveValue = improvedSolution.getAbsoluteObjectiveValue();

            printer.println(name + "," + i + "," + isInfeasible + "," + solution.isTimedOut() + "," + objectiveValue + "," + improvedSolution);
        }
    }






    private static boolean isInfeasible(Solution solution, Graph graph) {
        int bigM = graph.getBigM();

        boolean infeasible = false;
        for (int i = 0; i < solution.getSize(); i++) {
            int j = Math.floorMod(i+1, solution.getSize());

            int x = solution.get(i);
            int y = solution.get(j);

            int weight = graph.getWeight(x, y);

            if (weight == bigM) {
                infeasible = true;
            }
        }
        return infeasible;
    }

    private static List<File> getInstances() {
        List<String> names = new LinkedList<>();
        names.add("0010");
        names.add("0015");
        names.add("0020");
        names.add("0025");
        names.add("0030");
        names.add("0040");
        names.add("0050");
        names.add("0060");
        names.add("0070");
        names.add("0080");
        names.add("0090");
        names.add("0100");
        names.add("0150");
        names.add("0200");
        names.add("0250");
        names.add("0300");
        names.add("0400");
        names.add("0500");
        names.add("0600");
        names.add("0700");
        names.add("0800");
        names.add("0900");
        names.add("1000");
        names.add("1500");
        names.add("2000");
        names.add("2500");
        names.add("3000");

        return names.stream()
                    .map(n -> new File("src/main/resources/" + n + ".txt"))
                    .collect(Collectors.toList());
    }

    private static class Printer {
        PrintWriter printWriter;

        Printer(String name) throws RuntimeException {
            File output = new File("benchmarks/" + name);
            if (output.exists()) {
                throw new IllegalArgumentException("Output file already exists");
            }
            try {
                output.createNewFile();
                this.printWriter = new PrintWriter(output);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        void println(String line) {
            System.out.println(line);
            printWriter.println(line);
            printWriter.flush();
        }

        void close() {
            printWriter.close();
        }
    }
}

