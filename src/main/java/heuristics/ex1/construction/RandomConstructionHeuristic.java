package heuristics.ex1.construction;

import heuristics.ex1.dto.Graph;
import heuristics.ex1.dto.Solution;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.LinkedList;


public class RandomConstructionHeuristic implements ConstructionHeuristic {

    @Override
    public Solution solve(Graph graph) {
        Tour solution = new Tour(new LinkedList<>(), 0);
        ArrayList<Integer> remainingCities = new ArrayList<>(graph.getMatrix().keySet());

        //Initial tour, choose node at random, choose random neighbour
        int initial = (int) (Math.random() * graph.getMatrix().size());
        int second = (int) (Math.random() * graph.getMatrix().get(initial).size());
        if (initial == second) {
            second = (second + 1) % graph.getMatrix().get(initial).size();
        }
        solution.add(initial, 0);
        solution.add(second, graph.getMatrix().get(initial).get(second)*2);
        remainingCities.remove((Integer) initial);
        remainingCities.remove((Integer) second);

        int randomCity, bestCity;
        long bestDifference, oldPath, newPath;
        while(!remainingCities.isEmpty()){
            randomCity = remainingCities.get((int) (Math.random() * remainingCities.size()));
            bestCity = 0;
            bestDifference = Long.MIN_VALUE;

            for(int city=0; city < solution.getTour().size(); city++){
                oldPath = graph.getWeight(solution.getNode(city), solution.getNode(city+1));
                newPath = graph.getWeight(solution.getNode(city), randomCity) +
                        graph.getWeight(randomCity, solution.getNode(city+1));
                long difference = newPath - oldPath;

                if (Math.abs(solution.getCostValue() + difference) < Math.abs(solution.getCostValue() + bestDifference)){
                    bestCity = city;
                    bestDifference = difference;
                }
            }
            solution.add(randomCity, bestCity+1, solution.getCostValue() + bestDifference);
            remainingCities.remove((Integer) (randomCity));
        }
        return new Solution(solution.getTour(), solution.getCostValue());
    }

    @Getter
    @Setter
    @AllArgsConstructor
    private class Tour {
        private LinkedList<Integer> tour;
        private long costValue;

        void add(int node, long newCost){
            costValue = newCost;
            tour.add(node);
        }

        void add(int node, int position, long newCost){
            costValue = newCost;
            tour.add(position, node);
        }

        int getNode(int index){
            if (index >= tour.size()) {
                return tour.get(index % tour.size());
            } else {
                return tour.get(index);
            }

        }
    }
}