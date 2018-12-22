package heuristics.ex2.ga;

import heuristics.ex1.dto.Graph;
import heuristics.ex1.dto.Solution;
import heuristics.ex2.ga.util.*;

import java.util.*;


public class GeneticAlgorithm {

    private static final int MAX_NUM_GENERATIONS = 100;
    private static final int POPULATION_SIZE = 10;

    private Initializer initializer = new Initializer(POPULATION_SIZE);
    private Evaluator evaluator = new Evaluator();
    private Selector selector = new Selector(SelectionType.LINEAR_RANKING);
    private Recombinator recombinator = new Recombinator();
    private Mutator mutator = new Mutator();
    private Replacer replacer = new Replacer();

    public Solution solve(Graph graph) {
        int generation = 0;
        int parents = 4;
        Comparator<Solution> tourComparator = new SolutionComparator();
        SortedSet<Solution> ranking = new TreeSet<>(tourComparator);
        List<Solution> population = initializer.initialize(graph);
        ranking.addAll(population);
        Solution bestSolution = ranking.first();
        long bestCost = bestSolution.getAbsoluteObjectiveValue();

        //population = evaluator.evaluate(population);


        while (generation < MAX_NUM_GENERATIONS) {
            generation++;
            List<Solution> selectedParents = selector.select(new LinkedList<>(ranking), parents);

            List<Solution> offSpring = recombinator.recombine(selectedParents, graph);
            offSpring = mutator.mutate(offSpring, graph);
            population = replacer.replace(population, offSpring);

            //Next generation is the parents and the offspring generated
            //population = new LinkedList<>();
            //population.addAll(offSpring);
            //population.addAll(selectedParents);
            ranking.clear();
            ranking.addAll(offSpring);
            ranking.addAll(selectedParents);

            //Check if there is any solution better than any previous
            Solution currentBestSolution = ranking.first();
            if (currentBestSolution.getAbsoluteObjectiveValue() < bestCost){
                bestSolution = currentBestSolution;
                bestCost = currentBestSolution.getAbsoluteObjectiveValue();
            }
        }

        return bestSolution;
    }
}
