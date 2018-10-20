package heuristics.ex1.localsearch.neighborhood;

import heuristics.ex1.dto.Graph;
import heuristics.ex1.dto.Solution;

import java.util.Collections;
import java.util.List;

public class TwoOptNeighborhood implements Neighborhood {

    @Override
    public List<Solution> get(Solution solution, Graph graph) {
        return Collections.singletonList(solution);
    }
}
