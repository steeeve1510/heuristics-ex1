package heuristics.ex1.construction;

import heuristics.ex1.dto.Graph;
import heuristics.ex1.dto.Solution;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;


public class RandomConstructionHeuristic implements ConstructionHeuristic {

    @Override
    public Solution solve(Graph graph) {
        Tour solution = new Tour(new LinkedList<Integer>(), 0);
        ArrayList<Integer> remainingCities = new ArrayList<Integer>(graph.getMatrix().keySet());

        //Initial tour, choose node at random, choose random neighbour
        int initial = (int) (Math.random() * graph.getMatrix().size());
        int second = (int) (Math.random() * graph.getMatrix().get(initial).size());
        solution.add(initial, 0);
        solution.add(second, graph.getMatrix().get(initial).get(second)*2);
        remainingCities.remove((Integer) initial);
        remainingCities.remove((Integer) second);

        int improvement, randomCity, bestCity, bestImprovement, oldPath, newPath;
        while(!remainingCities.isEmpty()){
            randomCity = remainingCities.get((int) (Math.random() * remainingCities.size()));
            bestCity = 0;
            bestImprovement = Integer.MIN_VALUE;

            for(int city=0; city < solution.getTour().size(); city++){
                oldPath = Math.abs(graph.getWeight(solution.getNode(city), solution.getNode(city+1)));
                newPath = Math.abs(graph.getWeight(solution.getNode(city), randomCity) +
                        graph.getWeight(randomCity, solution.getNode(city+1)));
                improvement = oldPath - newPath;
                if (improvement > bestImprovement){
                    bestCity = city;
                    bestImprovement = improvement;
                }
            }
            solution.add(randomCity, bestCity+1, solution.getCostValue() - bestImprovement);
            remainingCities.remove((Integer) (randomCity));
        }
        return new Solution(solution.getTour(), solution.getCostValue());
    }

    @Getter
    @Setter
    @AllArgsConstructor
    private class Tour {
        private LinkedList<Integer> tour;
        private int costValue;

        public boolean add(int node, int newCost){
            costValue = newCost;
            return tour.add(node);
        }

        public void add (int node, int position, int newCost){
            costValue = newCost;
            tour.add(position, node);
        }

        public int getNode(int index){
            if (index >= tour.size()) {
                return tour.get(index % tour.size());
            } else {
                return tour.get(index);
            }

        }
    }
}