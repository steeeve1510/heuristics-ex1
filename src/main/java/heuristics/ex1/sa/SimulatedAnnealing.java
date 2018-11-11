package heuristics.ex1.sa;

import heuristics.ex1.construction.ConstructionHeuristic;
import heuristics.ex1.construction.GreedyConstructionHeuristic;
import heuristics.ex1.dto.Graph;
import heuristics.ex1.dto.Solution;
import heuristics.ex1.localsearch.neighborhood.Neighborhood;
import heuristics.ex1.localsearch.neighborhood.StepType;
import heuristics.ex1.localsearch.neighborhood.TwoOptNeighborhood;

public class SimulatedAnnealing {

    private ConstructionHeuristic construction = new GreedyConstructionHeuristic();
    private Neighborhood neighborhood          = new TwoOptNeighborhood();


    private static double INITIAL_TEMPERATURE = 1000;
    private static double COOL_UNTIL = 1;
    private static double COOLING_FACTOR = 0.95;


    public Solution solve(Graph graph) {
        System.out.println("Starting simulated annealing");
        long size = graph.getMatrix().size();
        long maxIterations = size * (size-1);

        Solution solution = construction.solve(graph);

        long step = 0;
        double temperature = INITIAL_TEMPERATURE;

        do {
            long iterations = 0;
            do {
                Solution neighbor = neighborhood.get(solution, graph, StepType.RANDOM);
                if (neighbor.getAbsoluteObjectiveValue() < solution.getAbsoluteObjectiveValue()) {
                    System.out.println("Found a better solution: " + neighbor.getAbsoluteObjectiveValue());
                    solution = neighbor;
                } else if (metropolisCriterion(neighbor, solution, temperature)) {
                    System.out.println("Choosing a worse solution: " + neighbor.getAbsoluteObjectiveValue());
                    solution = neighbor;
                }
                step++;
                iterations++;
            } while (iterations < maxIterations);
            temperature = cool(temperature, step);
        } while (temperature > COOL_UNTIL);

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
