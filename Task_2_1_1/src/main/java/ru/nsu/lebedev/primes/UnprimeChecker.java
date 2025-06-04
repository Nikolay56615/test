package ru.nsu.lebedev.primes;

import java.util.ArrayList;

/**
 * Abstract class for checking if a list contains non-prime numbers.
 */
public abstract class UnprimeChecker {
    protected ArrayList<Integer> numbers = new ArrayList<>();

    /**
     * Sets the numbers list.
     *
     * @param numbers List of numbers.
     * @return Reference to the instance for convenient chain usage.
     */
    public UnprimeChecker setNumbers(ArrayList<Integer> numbers) {
        this.numbers = numbers;
        return this;
    }

    /**
     * Checks if a number is not prime.
     * 0, 1, and negative numbers are not prime.
     * 2 and 3 are prime.
     * Exclude even numbers and multiples of 3.
     *
     * @param number The number to check.
     * @return true if the number is not prime.
     */
    protected static boolean isNumUnprime(int number) {
        if (number < 2) {
            return true;
        }
        if (number == 2 || number == 3) {
            return false;
        }
        if (number % 2 == 0 || number % 3 == 0) {
            return true;
        }
        for (int i = 5; i * i <= number; i += 6) {
            if (number % i == 0 || number % (i + 2) == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Abstract method to check if any number in the list is non-prime.
     *
     * @return true if there is at least one non-prime number.
     */
    public abstract boolean isAnyUnprime();
}