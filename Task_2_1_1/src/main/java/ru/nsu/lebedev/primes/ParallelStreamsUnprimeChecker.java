package ru.nsu.lebedev.primes;

/**
 * Implementation of an unprime checker using a parallel Stream API.
 */
public class ParallelStreamsUnprimeChecker extends UnprimeChecker {
    @Override
    public boolean isAnyUnprime() {
        return numbers.parallelStream().anyMatch(UnprimeChecker::isNumUnprime);
    }
}