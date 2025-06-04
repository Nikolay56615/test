package ru.nsu.lebedev.primes;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * Main class with comparing types of calculations.
 */
public class Main {
    private static final int TEST_SIZE = 10_000_000;
    private static final int LARGE_COMPOSITE_NUMBER = 17858849;

    /**
     * Main function with comparing types of calculations.
     */
    public static void main(String[] args) {
        ArrayList<Integer> tests = new ArrayList<>();
        for (int i = 0; i < TEST_SIZE; i++) {
            tests.add(LARGE_COMPOSITE_NUMBER);
        }
        UnprimeChecker[] checkers = new UnprimeChecker[]{
            new SequentialUnprimeChecker(),
            new ParallelStreamsUnprimeChecker(),
            new ThreadUnprimeChecker(1),
            new ThreadUnprimeChecker(2),
            new ThreadUnprimeChecker(4),
            new ThreadUnprimeChecker(8),
            new ThreadUnprimeChecker(16)
        };
        try (FileOutputStream fos = new FileOutputStream("result.csv");
             OutputStreamWriter writer = new OutputStreamWriter(fos)) {
            for (UnprimeChecker checker : checkers) {
                long time = measureExecutionTime(() -> checker.setNumbers(tests).isAnyUnprime());
                writer.write(time + "\n");
            }
        } catch (IOException e) {
            System.err.println("File writing error: " + e.getMessage());
        }
    }

    /**
     * Measures execution time of a given task in milliseconds.
     *
     * @param task The task to measure.
     * @return Execution time in milliseconds.
     */
    private static long measureExecutionTime(Runnable task) {
        long start = System.nanoTime();
        task.run();
        return (System.nanoTime() - start) / 1_000_000;
    }
}
