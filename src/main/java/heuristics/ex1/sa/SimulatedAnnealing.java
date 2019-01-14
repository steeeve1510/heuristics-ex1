package heuristics.ex1.sa;

import heuristics.ex1.construction.ConstructionHeuristic;
import heuristics.ex1.construction.GreedyConstructionHeuristic;
import heuristics.ex1.dto.Graph;
import heuristics.ex1.dto.Solution;
import heuristics.ex1.localsearch.neighborhood.Neighborhood;
import heuristics.ex1.localsearch.neighborhood.StepType;
import heuristics.ex1.localsearch.neighborhood.ThreeOptNeighborhoodNew;

import java.lang.management.ManagementFactory;

public class SimulatedAnnealing {

    private ConstructionHeuristic construction = new GreedyConstructionHeuristic();
    private Neighborhood neighborhood          = new ThreeOptNeighborhoodNew();
    private int maxTimeInSeconds;

    public SimulatedAnnealing() {
        this(15 * 60);
    }

    public SimulatedAnnealing(int maxTimeInSeconds) {
        this.maxTimeInSeconds = maxTimeInSeconds;
    }

    private static double INITIAL_TEMPERATURE = 1000;
    private static double COOL_UNTIL = 1;
    private static double COOLING_FACTOR = 0.95;


    public Solution solve(Graph graph) {
        long size = graph.getMatrix().size();
        long maxIterations = size * (size-1);

        Solution solution = construction.solve(graph);

        long step = 0;
        double temperature = INITIAL_TEMPERATURE;
        boolean timedOut = false;
        long startTime = ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime();
        long endTime = startTime + 1000L * 1000L * 1000L * maxTimeInSeconds;

        do {
            long iterations = 0;
            do {
                Solution neighbor = neighborhood.get(solution, graph, StepType.RANDOM);
                if (neighbor.getAbsoluteObjectiveValue() < solution.getAbsoluteObjectiveValue()) {
                    solution = neighbor;
                } else if (metropolisCriterion(neighbor, solution, temperature)) {
                    solution = neighbor;
                }
                step++;
                iterations++;
            } while (iterations < maxIterations);
            temperature = cool(temperature, step);
            //Method might take longer than 15 min bc it only checks time after every temp level is finished
            if (ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime() > endTime){
                timedOut = true;
                solution.setTimedOut(true);
            }
        } while (temperature > COOL_UNTIL && !timedOut );

        return solution;
    }

    public Solution solve(Graph graph, Solution solution) {
        long size = graph.getMatrix().size();
        long maxIterations = size * (size-1);

        long step = 0;
        double temperature = INITIAL_TEMPERATURE;
        boolean timedOut = false;
        long startTime = ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime();
        long endTime = startTime + 1000L * 1000L * 1000L * maxTimeInSeconds;

        do {
            long iterations = 0;
            do {
                Solution neighbor = neighborhood.get(solution, graph, StepType.RANDOM);
                if (neighbor.getAbsoluteObjectiveValue() < solution.getAbsoluteObjectiveValue()) {
                    solution = neighbor;
                } else if (metropolisCriterion(neighbor, solution, temperature)) {
                    solution = neighbor;
                }
                step++;
                iterations++;
            } while (iterations < maxIterations);
            temperature = cool(temperature, step);
            //Method might take longer than 15 min bc it only checks time after every temp level is finished
            if (ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime() > endTime){
                timedOut = true;
                solution.setTimedOut(true);
            }
        } while (temperature > COOL_UNTIL && !timedOut );

        return solution;
    }

    private boolean metropolisCriterion(Solution neigbhor, Solution solution, double temperature) {
        double p = Math.random();

        double bound = Math.exp( (-Math.abs(neigbhor.getAbsoluteObjectiveValue() - solution.getAbsoluteObjectiveValue())) / temperature );

        return p < bound;
    }

    private double cool(double temperature, long step) {
        return temperature * COOLING_FACTOR;
    }
}
