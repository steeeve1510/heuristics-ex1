package heuristics.ex2.ga.util;

import heuristics.ex1.dto.Solution;
import java.util.Comparator;

public class SolutionComparator implements Comparator<Solution> {
    // Compares solutions by their Abs Obj Value
    @Override
    public int compare(Solution o1, Solution o2) {
        if (o1.getAbsoluteObjectiveValue() < o2.getAbsoluteObjectiveValue()){
            return -1;
        } else if (o1.getAbsoluteObjectiveValue() > o2.getAbsoluteObjectiveValue()){
            return 1;
        }
        return 0;
    }
}
