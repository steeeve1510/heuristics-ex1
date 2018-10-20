package heuristics.ex1.localsearch.step;

import heuristics.ex1.dto.Solution;

import java.util.List;
import java.util.Random;

public class RandomStep implements Step {
    @Override
    public Solution get(List<Solution> neighbors, Solution solution) {
        if (neighbors == null || neighbors.isEmpty()) {
            return null;
        }

        Random random = new Random();
        int randomNeighbor = random.nextInt(neighbors.size());

        return neighbors.get(randomNeighbor);
    }
}
