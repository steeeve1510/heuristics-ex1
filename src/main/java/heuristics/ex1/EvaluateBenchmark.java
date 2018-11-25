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

        createLatexStatistics(entries);
    }

    private static void createLatexStatistics(List<Entry> entries) throws FileNotFoundException {

        Map<Integer, List<Entry>> entryPerInstance = entries.stream()
                .collect(Collectors.groupingBy(Entry::getInstance));

        List<Integer> instances = entryPerInstance.keySet().stream()
                .sorted()
                .collect(Collectors.toList());

        for (int instance : instances) {
            List<Entry> entriesForInstance = entryPerInstance.get(instance);

            Entry minimum = entriesForInstance.stream()
                    .min(Comparator.comparingLong(Entry::getObjectiveValue))
                    .orElseThrow(() -> new IllegalArgumentException("Could't find element with minimum"));

            String instanceString = String.format("%04d", minimum.getInstance());

            long bestObjectiveValue = minimum.getObjectiveValue();
            boolean bestIsInfeasible = minimum.isInfeasible();

            String line;

            if (entriesForInstance.size() > 1) {
                List<Entry> filteredEntriesForInstance = entriesForInstance.stream()
                        .filter(e -> !e.isInfeasible())
                        .collect(Collectors.toList());

                int numberOfFeasible = filteredEntriesForInstance.size();
                int numberOfInfeasible = entriesForInstance.size()  - filteredEntriesForInstance.size();

                if (filteredEntriesForInstance.size() > 0) {
                    double meanObjectiveValue = filteredEntriesForInstance.stream()
                            .mapToLong(Entry::getObjectiveValue)
                            .average()
                            .orElseThrow(() -> new RuntimeException("Dafuq"));
                    double sdObjectiveValue = calculateSD(
                            filteredEntriesForInstance.stream()
                                    .map(Entry::getObjectiveValue)
                                    .collect(Collectors.toList())
                    );

                    double meanTime = filteredEntriesForInstance.stream()
                            .mapToLong(Entry::getTime)
                            .filter(n -> n>0)
                            .average()
                            .orElse(-1);
                    double sdTime = calculateSD(
                            filteredEntriesForInstance.stream()
                                    .map(Entry::getTime)
                                    .filter(n -> n>0)
                                    .collect(Collectors.toList())
                    );

                    if (new Double(meanTime).longValue() > -1) {
                        line = instanceString + " & " +
                                bestObjectiveValue + " & " +
                                bestIsInfeasible + " & " +
                                String.format("%.1f", meanObjectiveValue) + " & " +
                                String.format("%.1f", sdObjectiveValue) + " & " +
                                String.format("%.1f", meanTime) + "ms & " +
                                String.format("%.1f", sdTime) + "ms & " +
                                numberOfFeasible + "/10 \\\\";
                    } else {
                        line = instanceString + " & " +
                                bestObjectiveValue + " & " +
                                bestIsInfeasible + " & " +
                                String.format("%.1f", meanObjectiveValue) + " & " +
                                String.format("%.1f", sdObjectiveValue) + " & " +
                                "timeout" + " & " +
                                "timeout" + " & " +
                                numberOfFeasible + "/10 \\\\";
                    }

                } else {
                    line = instanceString + " & " +
                            bestObjectiveValue + " & " +
                            bestIsInfeasible + " & " +
                            "-" + " & " +
                            "-" + " & " +
                            "-" + " & " +
                            "-" + " & " +
                            "0/10" + " \\\\";
                }


            } else {
                long bestTime = minimum.getTime();
                String bestTimeString = bestTime + "ms";
                if (bestTime == -1) {
                    bestTimeString = "timeout";
                }

                line = instanceString + " & " +
                        bestObjectiveValue + " & " +
                        bestIsInfeasible + " & " +
                        bestTimeString + " \\\\";
            }

            System.out.println(line);
        }
    }


    public static double calculateSD(List<Long> numbers) {
        double standardDeviation = 0L;
        int length = numbers.size();

        double mean = numbers.stream()
                .mapToLong(n -> n)
                .average()
                .orElseThrow(() -> new RuntimeException("could not calculate mean"));

        for(long num : numbers) {
            standardDeviation += Math.pow(num - mean, 2);
        }

        return Math.sqrt(standardDeviation/length);
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
        long time = Long.parseLong(parts[4]);
        long objectiveValue = Long.parseLong(parts[5]);
        String solution = parts[6];

        if (timedOut) {
            time = -1;
        }

        return new Entry(instance, run, isInfeasible, timedOut, time, objectiveValue, solution);
    }

    private static List<File> getFiles() {
        List<String> names = new LinkedList<>();
//        names.add("01 - greedy construction - new.csv");
//        names.add("02 - random construction - new.csv");

//        names.add("03a - localsearch 2opt best - new.csv");
//        names.add("03b - localsearch 2opt next - new.csv");
//        names.add("03c - localsearch 2opt random - new.csv");

//        names.add("04a - localsearch 2-5opt best - new.csv");
//        names.add("04b - localsearch 2-5opt next - new.csv");
//        names.add("04c - localsearch 2-5opt random - new.csv");
//
//        names.add("05a - localsearch 3opt best - new.csv");
//        names.add("05b - localsearch 3opt next - new.csv");
//        names.add("05c - localsearch 3opt random - new.csv");

//        names.add("06 - vnd - new.csv");
//        names.add("07 - grasp - new.csv");
        names.add("08 - sa - new.csv");

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
        private long time;
        private long objectiveValue;
        private String solution;
    }
}
