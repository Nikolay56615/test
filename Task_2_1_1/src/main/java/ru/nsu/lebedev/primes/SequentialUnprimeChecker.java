package ru.nsu.lebedev.primes;

/**
 * Implementation of an unprime checker using a sequential Stream API.
 */
public class SequentialUnprimeChecker extends UnprimeChecker {
    @Override
    public boolean isAnyUnprime() {
        return numbers.stream().anyMatch(UnprimeChecker::isNumUnprime);
    }
}