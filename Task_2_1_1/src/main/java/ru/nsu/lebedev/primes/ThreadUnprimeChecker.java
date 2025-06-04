package ru.nsu.lebedev.primes;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Implementation of UnprimeChecker using multithreading.
 */
public class ThreadUnprimeChecker extends UnprimeChecker {
    private final int threadCount;

    /**
     * Constructor to initialize the number of threads.
     *
     * @param threadCount Number of threads to use for checking.
     */
    public ThreadUnprimeChecker(int threadCount) {
        this.threadCount = threadCount;
    }

    @Override
    public boolean isAnyUnprime() {
        final int numbersSize = numbers.size();
        AtomicBoolean unprimeFound = new AtomicBoolean(false);
        Thread[] threads = new Thread[threadCount];
        int basePartitionSize = numbersSize / threadCount;
        int extra = numbersSize % threadCount;
        int startIndex = 0;
        for (int i = 0; i < threadCount; ++i) {
            int partitionSize = basePartitionSize + (i < extra ? 1 : 0);
            threads[i] = new Thread(createWorker(unprimeFound, startIndex,
                    startIndex + partitionSize));
            threads[i].start();
            startIndex += partitionSize;
        }
        try {
            for (Thread thread : threads) {
                thread.join();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException("Thread execution interrupted.", e);
        }
        return unprimeFound.get();
    }

    /**
     * Creates a worker thread for checking a subset of numbers.
     *
     * @param unprimeFound Shared flag indicating if a non-prime number is found.
     * @param startIndex Starting index of the subset.
     * @param endIndex Ending index of the subset.
     * @return Runnable task for the thread.
     */
    private Runnable createWorker(AtomicBoolean unprimeFound, int startIndex, int endIndex) {
        return () -> {
            if (unprimeFound.get()) {
                return;
            }
            boolean result = numbers.subList(startIndex,
                    endIndex).stream().anyMatch(UnprimeChecker::isNumUnprime);
            if (result) {
                unprimeFound.set(true);
            }
        };
    }
}
