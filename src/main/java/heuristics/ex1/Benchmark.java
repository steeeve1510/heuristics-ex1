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
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;


public class Benchmark {

    private static final int RUNS = 10;

    public static void main(String[] args) throws IOException {
        List<File> instances = getInstances();


//        Printer printer3 = new Printer("05a - localsearch 3opt best - new.csv");
//        try {
//            for (File instance : instances) {
//                Graph graph = new GraphBuilder().build(instance);
//
//                localSearch_best(new ThreeOptNeighborhoodNew(), instance, graph, printer3);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        printer3.close();
//        printer3 = null;
//
//
//
//
//        Printer printer2 = new Printer("05b - localsearch 3opt next - new.csv");
//        try {
//            for (File instance : instances) {
//                Graph graph = new GraphBuilder().build(instance);
//
//                localSearch_next(new ThreeOptNeighborhoodNew(),instance, graph, printer2);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        printer2.close();
//        printer2 = null;




        Printer printer = new Printer("08 - sa - new.csv");
        try {
            for (File instance : instances) {
                Graph graph = new GraphBuilder().build(instance);

                simulatedAnnealing(instance, graph, printer);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        printer.close();
        printer = null;

    }

    private static void greedyConstruction(File instance, Graph graph, Printer printer) {
        ConstructionHeuristic constructionHeuristic = new GreedyConstructionHeuristic();

        long initialTime = ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime();
        Solution solution = constructionHeuristic.solve(graph);
        long elapsedTime = (ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime() - initialTime) / (1000L * 1000L);


        String name = instance.getName().substring(0, instance.getName().length() - 4);
        boolean isInfeasible = isInfeasible(solution, graph);
        long objectiveValue = solution.getAbsoluteObjectiveValue();

        printer.println(name + "," + 1 + "," + isInfeasible + "," + solution.isTimedOut() + "," + elapsedTime + "," + objectiveValue + "," + solution);
    }

    private static void randomConstruction(File instance, Graph graph, Printer printer) {
        ConstructionHeuristic constructionHeuristic = new RandomConstructionHeuristic();

        for (int i = 1; i <= RUNS; i++) {
            long initialTime = ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime();
            Solution solution = constructionHeuristic.solve(graph);
            long elapsedTime = (ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime() - initialTime) / (1000L * 1000L);

            String name = instance.getName().substring(0, instance.getName().length() - 4);
            boolean isInfeasible = isInfeasible(solution, graph);
            long objectiveValue = solution.getAbsoluteObjectiveValue();

            printer.println(name + "," + i + "," + isInfeasible + "," + solution.isTimedOut() + "," + elapsedTime + "," + objectiveValue + "," + solution);
        }
    }


    private static void localSearch_best(Neighborhood neighborhood, File instance, Graph graph, Printer printer) {
        ConstructionHeuristic constructionHeuristic = new GreedyConstructionHeuristic();

        Solution solution = constructionHeuristic.solve(graph);

        LocalSearch localSearch = new LocalSearch(neighborhood, StepType.BEST_IMPOVEMENT);

        long initialTime = ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime();
        solution = localSearch.improve(solution, graph);
        long elapsedTime = (ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime() - initialTime) / (1000L * 1000L);

        String name = instance.getName().substring(0, instance.getName().length() - 4);
        boolean isInfeasible = isInfeasible(solution, graph);
        long objectiveValue = solution.getAbsoluteObjectiveValue();

        printer.println(name + "," + 1 + "," + isInfeasible + "," + solution.isTimedOut() + "," + elapsedTime + "," + objectiveValue + "," + solution);

        if (solution.isTimedOut()) {
            throw new RuntimeException("Time out");
        }
    }

    private static void localSearch_next(Neighborhood neighborhood, File instance, Graph graph, Printer printer) {
        ConstructionHeuristic constructionHeuristic = new GreedyConstructionHeuristic();

        Solution solution = constructionHeuristic.solve(graph);

        LocalSearch localSearch = new LocalSearch(neighborhood, StepType.NEXT_IMPROVEMENT);

        long initialTime = ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime();
        solution = localSearch.improve(solution, graph);
        long elapsedTime = (ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime() - initialTime) / (1000L * 1000L);

        String name = instance.getName().substring(0, instance.getName().length() - 4);
        boolean isInfeasible = isInfeasible(solution, graph);
        long objectiveValue = solution.getAbsoluteObjectiveValue();

        printer.println(name + "," + 1 + "," + isInfeasible + "," + solution.isTimedOut() + "," + elapsedTime + "," + objectiveValue + "," + solution);

        if (solution.isTimedOut()) {
            throw new RuntimeException("Time out");
        }
    }

    private static void localSearch_random(Neighborhood neighborhood, File instance, Graph graph, Printer printer) {
        ConstructionHeuristic constructionHeuristic = new GreedyConstructionHeuristic();

        Solution solution = constructionHeuristic.solve(graph);

        int timeOutCounter = 0;

        LocalSearch localSearch = new LocalSearch(neighborhood, StepType.RANDOM, 90);
        for (int i = 1; i <= RUNS; i++) {
            long initialTime = ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime();
            Solution improvedSolution = localSearch.improve(solution, graph);
            long elapsedTime = (ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime() - initialTime) / (1000L * 1000L);

            String name = instance.getName().substring(0, instance.getName().length() - 4);
            boolean isInfeasible = isInfeasible(improvedSolution, graph);
            long objectiveValue = improvedSolution.getAbsoluteObjectiveValue();

            printer.println(name + "," + i + "," + isInfeasible + "," + improvedSolution.isTimedOut() + "," + elapsedTime + "," + objectiveValue + "," + improvedSolution);

            if (solution.isTimedOut()) {
                timeOutCounter++;
            }

            if (timeOutCounter > RUNS / 2) {
                throw new RuntimeException("Time out");
            }
        }
    }


    private static void vnd(File instance, Graph graph, Printer printer) {
        ConstructionHeuristic constructionHeuristic = new GreedyConstructionHeuristic();

        Solution solution = constructionHeuristic.solve(graph);

        VND vnd = new VND();

        long initialTime = ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime();
        solution = vnd.improve(solution, graph);
        long elapsedTime = (ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime() - initialTime) / (1000L * 1000L);

        String name = instance.getName().substring(0, instance.getName().length() - 4);
        boolean isInfeasible = isInfeasible(solution, graph);
        long objectiveValue = solution.getAbsoluteObjectiveValue();

        printer.println(name + "," + 1 + "," + isInfeasible + "," + solution.isTimedOut() + "," + elapsedTime + "," + objectiveValue + "," + solution);

        if (solution.isTimedOut()) {
            throw new RuntimeException("Time out");
        }
    }

    private static void grasp(File instance, Graph graph, Printer printer) {
        GRASP grasp = new GRASP();
        String name = instance.getName().substring(0, instance.getName().length() - 4);
        long initialTime, elapsedTime;
        for (int i = 1; i <= RUNS; i++) {
            initialTime = ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime();
            Solution solution = grasp.solve(graph);
            elapsedTime = (ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime() - initialTime) / 1000000L;

            boolean isInfeasible = isInfeasible(solution, graph);
            long objectiveValue = solution.getAbsoluteObjectiveValue();
            printer.println(name + "," + i + "," + isInfeasible + "," + solution.isTimedOut() + "," + objectiveValue
                    + ","+ elapsedTime+"," + solution);
        }
    }

    private static void grasp2(File instance, Graph graph, Printer printer) {

        int timeOutCounter = 0;

        GRASP grasp = new GRASP(90);
        for (int i = 1; i <= RUNS; i++) {

            long initialTime = ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime();
            Solution improvedSolution = grasp.solve(graph);
            long elapsedTime = (ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime() - initialTime) / (1000L * 1000L);


            String name = instance.getName().substring(0, instance.getName().length() - 4);
            boolean isInfeasible = isInfeasible(improvedSolution, graph);
            long objectiveValue = improvedSolution.getAbsoluteObjectiveValue();

            printer.println(name + "," + i + "," + isInfeasible + "," + improvedSolution.isTimedOut() + "," + elapsedTime + "," + objectiveValue + "," + improvedSolution);

            if (improvedSolution.isTimedOut()) {
                timeOutCounter++;
            }

            if (timeOutCounter > RUNS / 2) {
                throw new RuntimeException("Time out");
            }
        }
    }
    private static void simulatedAnnealing(File instance, Graph graph, Printer printer) {

        int timeOutCounter = 0;

        SimulatedAnnealing simulatedAnnealing = new SimulatedAnnealing(90);
        for (int i = 1; i <= RUNS; i++) {

            long initialTime = ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime();
            Solution improvedSolution = simulatedAnnealing.solve(graph);
            long elapsedTime = (ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime() - initialTime) / (1000L * 1000L);


            String name = instance.getName().substring(0, instance.getName().length() - 4);
            boolean isInfeasible = isInfeasible(improvedSolution, graph);
            long objectiveValue = improvedSolution.getAbsoluteObjectiveValue();

            printer.println(name + "," + i + "," + isInfeasible + "," + improvedSolution.isTimedOut() + "," + elapsedTime + "," + objectiveValue + "," + improvedSolution);

            if (improvedSolution.isTimedOut()) {
                timeOutCounter++;
            }

            if (timeOutCounter > RUNS / 2) {
//                throw new RuntimeException("Time out");
            }
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
            this.println("name,run,isInfeasible,timedOut,time,objectiveValue,solution");
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


