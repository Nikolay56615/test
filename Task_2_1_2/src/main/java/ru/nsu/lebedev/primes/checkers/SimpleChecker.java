package ru.nsu.lebedev.primes.checkers;

/**
 * Simple implementation of a checker for composite (non-prime) numbers.
 */
public class SimpleChecker extends UnprimeChecker {

    @Override
    public boolean isAnyUnprime() throws InterruptedException {
        if (numbers == null || numbers.isEmpty()) {
            return false;
        }
        for (Integer number : numbers) {
            if (Thread.currentThread().isInterrupted()) {
                throw new InterruptedException("Interrupted while checking for composite numbers");
            }
            if (isNumUnprime(number)) {
                return true;
            }
        }
        return false;
    }
}
