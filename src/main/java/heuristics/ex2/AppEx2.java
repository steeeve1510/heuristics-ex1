package heuristics.ex2;

import org.apache.commons.math3.distribution.EnumeratedDistribution;
import org.apache.commons.math3.util.Pair;

import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public class AppEx2 {
    public static void main(String[] args) {
        SortedSet<Integer> set = new TreeSet<>();

        set.add(1);
        set.add(100);
        set.add(20);
        set.add(9999);
        set.add(15);

        List<Pair<Integer, Double>> weights = new LinkedList<>();
        for(Integer i : set) {
            weights.add(new Pair<>(i, 1d));
        }

        EnumeratedDistribution<Integer> distribution = new EnumeratedDistribution<>(weights);
        System.out.println(distribution.getPmf().get(0).getValue());
    }
}
