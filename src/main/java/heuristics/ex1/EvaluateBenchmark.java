package heuristics.ex1;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EvaluateBenchmark {

    public static void main(String[] args) throws IOException {
        List<File> files = getFiles();

        List<Entry> entries = files.stream()
                .flatMap(f -> read(f).stream())
                .collect(Collectors.toList());

        storeSubmissionFiles(entries, "grasp");
    }

    private static void storeSubmissionFiles(List<Entry> entries, String folder) throws FileNotFoundException {

        Map<Integer, List<Entry>> entryPerInstance = entries.stream()
                .collect(Collectors.groupingBy(Entry::getInstance));

        for (int instance : entryPerInstance.keySet()) {
            List<Entry> entriesForInstance = entryPerInstance.get(instance);

            Entry minimum = entriesForInstance.stream()
                    .min(Comparator.comparingLong(Entry::getObjectiveValue))
                    .orElseThrow(() -> new IllegalArgumentException("Could't find element with minimum"));

            String outputFilename = String.format("%04d", minimum.getInstance());

            File outputFile = new File("submission/" + folder + "/" + outputFilename + ".txt");

            PrintWriter printWriter = new PrintWriter(outputFile);
            printWriter.write(minimum.getSolution());
            printWriter.close();
        }
    }

    private static List<Entry> read(File file) {
        try {
            return Files.lines(file.toPath())
                    .skip(1)
                    .map(EvaluateBenchmark::parseLine)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Dafuq", e);
        }
    }

    private static Entry parseLine(String line) {
        String[] parts = line.split(",");

        int instance = Integer.parseInt(parts[0]);
        int run = Integer.parseInt(parts[1]);
        boolean isInfeasible = "true".equalsIgnoreCase(parts[2]);
        boolean timedOut = "true".equalsIgnoreCase(parts[3]);
        long objectiveValue = Long.parseLong(parts[4]);
        String solution = parts[5];

        return new Entry(instance, run, isInfeasible, timedOut, objectiveValue, solution);
    }

    private static List<File> getFiles() {
        List<String> names = new LinkedList<>();
//        names.add("01 - greedy construction.csv");
//        names.add("02 - random construction.csv");
//        names.add("03a - localsearch 2opt best.csv");
//        names.add("03b - localsearch 2opt next.csv");
//        names.add("03c - localsearch 2opt random.csv");
//        names.add("04a - localsearch 2-5opt best.csv");
//        names.add("04b - localsearch 2-5opt next.csv");
//        names.add("04c - localsearch 2-5opt random.csv");
//        names.add("05a - localsearch 3opt best.csv");
//        names.add("05a - localsearch 3opt best_old.csv");
//        names.add("05a - localsearch 3opt best_old2.csv");
//        names.add("05b - localsearch 3opt next.csv");
//        names.add("05c - localsearch 3opt random.csv");
//        names.add("06 - vnd.csv");
//        names.add("06 - vnd_old.csv");
        names.add("07 - grasp.csv");

        return names.stream()
                .map(n -> new File("benchmarks/" + n))
                .collect(Collectors.toList());
    }

    @AllArgsConstructor
    @Getter
    @ToString
    private static class Entry {
        private int instance;
        private int run;
        private boolean isInfeasible;
        private boolean timedOut;
        private long objectiveValue;
        private String solution;
    }
}
