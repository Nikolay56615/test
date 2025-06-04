package ru.nsu.lebedev.primes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

/**
 * Class for testing unprime checkers.
 */
public class UnprimeCheckersTest {
    static class UnprimeCheckersProvider implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(
                Arguments.of(new SequentialUnprimeChecker()),
                Arguments.of(new ParallelStreamsUnprimeChecker()),
                Arguments.of(new ThreadUnprimeChecker(1)),
                Arguments.of(new ThreadUnprimeChecker(2)),
                Arguments.of(new ThreadUnprimeChecker(4)),
                Arguments.of(new ThreadUnprimeChecker(8)),
                Arguments.of(new ThreadUnprimeChecker(16))
            );
        }
    }

    @ParameterizedTest
    @ArgumentsSource(UnprimeCheckersProvider.class)
    void exampleTest1(UnprimeChecker checker) {
        Integer[] array = {6, 8, 7, 13, 5, 9, 4};
        ArrayList<Integer> list = new ArrayList<>(Arrays.asList(array));
        checker.setNumbers(list);
        Assertions.assertTrue(checker.isAnyUnprime());
    }

    @ParameterizedTest
    @ArgumentsSource(UnprimeCheckersProvider.class)
    void exampleTest2(UnprimeChecker checker) {
        Integer[] array = {20319251, 6997901, 6997927, 6997937, 17858849, 6997967,
            6998009, 6998029, 6998039, 20165149, 6998051, 6998053};
        ArrayList<Integer> list = new ArrayList<>(Arrays.asList(array));
        checker.setNumbers(list);
        Assertions.assertFalse(checker.isAnyUnprime());
    }

    @ParameterizedTest
    @ArgumentsSource(UnprimeCheckersProvider.class)
    void simplePrimesTest(UnprimeChecker checker) {
        Integer[] array = {2, 3, 5, 7};
        ArrayList<Integer> list = new ArrayList<>(Arrays.asList(array));
        checker.setNumbers(list);
        Assertions.assertFalse(checker.isAnyUnprime());
    }

    @ParameterizedTest
    @ArgumentsSource(UnprimeCheckersProvider.class)
    void simpleUnprimeTest(UnprimeChecker checker) {
        Integer[] array = {2, 3, 4, 5, 7};
        ArrayList<Integer> list = new ArrayList<>(Arrays.asList(array));
        checker.setNumbers(list);
        Assertions.assertTrue(checker.isAnyUnprime());
    }

    @ParameterizedTest
    @ArgumentsSource(UnprimeCheckersProvider.class)
    void onePrimeTest(UnprimeChecker checker) {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(5);
        checker.setNumbers(list);
        Assertions.assertFalse(checker.isAnyUnprime());
    }

    @ParameterizedTest
    @ArgumentsSource(UnprimeCheckersProvider.class)
    void oneUnprimeTest(UnprimeChecker checker) {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(4);
        checker.setNumbers(list);
        Assertions.assertTrue(checker.isAnyUnprime());
    }

    @ParameterizedTest
    @ArgumentsSource(UnprimeCheckersProvider.class)
    void oneAsUnprimeTest(UnprimeChecker checker) {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(1);
        checker.setNumbers(list);
        Assertions.assertTrue(checker.isAnyUnprime());
    }

    @ParameterizedTest
    @ArgumentsSource(UnprimeCheckersProvider.class)
    void negativeAsUnprimeTest(UnprimeChecker checker) {
        Integer[] array = {-2, -3, -4, -5, -7};
        ArrayList<Integer> list = new ArrayList<>(Arrays.asList(array));
        checker.setNumbers(list);
        Assertions.assertTrue(checker.isAnyUnprime());
    }

    @ParameterizedTest
    @ArgumentsSource(UnprimeCheckersProvider.class)
    void emptyListTest(UnprimeChecker checker) {
        ArrayList<Integer> list = new ArrayList<>();
        checker.setNumbers(list);
        Assertions.assertFalse(checker.isAnyUnprime());
    }
}
