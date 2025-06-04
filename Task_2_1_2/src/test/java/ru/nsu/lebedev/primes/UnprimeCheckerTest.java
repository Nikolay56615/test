package ru.nsu.lebedev.primes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import ru.nsu.lebedev.primes.checkers.SimpleChecker;
import ru.nsu.lebedev.primes.checkers.UnprimeChecker;
import ru.nsu.lebedev.primes.jobs.JobDataRecord;
import ru.nsu.lebedev.primes.jobs.JobManager;

/**
 * Tests for UnprimeChecker and etc.
 */
public class UnprimeCheckerTest {

    private UnprimeChecker createChecker() {
        return new SimpleChecker();
    }

    @Test
    void exampleTest1() {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(6);
        list.add(8);
        list.add(7);
        list.add(13);
        list.add(5);
        list.add(9);
        list.add(4);
        UnprimeChecker checker = createChecker().setNumbers(list);
        try {
            assertTrue(checker.isAnyUnprime());
        } catch (InterruptedException e) {
            fail("Interrupted");
        }
    }

    @Test
    void exampleTest2() {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(20319251);
        list.add(6997901);
        list.add(6997927);
        list.add(6997937);
        list.add(17858849);
        list.add(6997967);
        list.add(6998009);
        list.add(6998029);
        list.add(6998039);
        list.add(20165149);
        list.add(6998051);
        list.add(6998053);
        UnprimeChecker checker = createChecker().setNumbers(list);
        try {
            assertFalse(checker.isAnyUnprime());
        } catch (InterruptedException e) {
            fail("Interrupted");
        }
    }

    @Test
    void simplePrimesTest() {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(2);
        list.add(3);
        list.add(5);
        list.add(7);
        UnprimeChecker checker = createChecker().setNumbers(list);
        try {
            assertFalse(checker.isAnyUnprime());
        } catch (InterruptedException e) {
            fail("Interrupted");
        }
    }

    @Test
    void simpleUnprimeTest() {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        list.add(7);
        UnprimeChecker checker = createChecker().setNumbers(list);
        try {
            assertTrue(checker.isAnyUnprime());
        } catch (InterruptedException e) {
            fail("Interrupted");
        }
    }

    @Test
    void onePrimeTest() {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(5);
        UnprimeChecker checker = createChecker().setNumbers(list);
        try {
            assertFalse(checker.isAnyUnprime());
        } catch (InterruptedException e) {
            fail("Interrupted");
        }
    }

    @Test
    void oneUnprimeTest() {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(4);
        UnprimeChecker checker = createChecker().setNumbers(list);
        try {
            assertTrue(checker.isAnyUnprime());
        } catch (InterruptedException e) {
            fail("Interrupted");
        }
    }

    @Test
    void oneAsUnprimeTest() {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(1);
        UnprimeChecker checker = createChecker().setNumbers(list);
        try {
            assertTrue(checker.isAnyUnprime());
        } catch (InterruptedException e) {
            fail("Interrupted");
        }
    }

    @Test
    void negativeAsUnprimeTest() {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(-2);
        list.add(-3);
        list.add(-4);
        list.add(-5);
        list.add(-7);
        UnprimeChecker checker = createChecker().setNumbers(list);
        try {
            assertTrue(checker.isAnyUnprime());
        } catch (InterruptedException e) {
            fail("Interrupted");
        }
    }

    @Test
    void emptyListTest() {
        ArrayList<Integer> list = new ArrayList<>();
        UnprimeChecker checker = createChecker().setNumbers(list);
        try {
            assertFalse(checker.isAnyUnprime());
        } catch (InterruptedException e) {
            fail("Interrupted");
        }
    }

    @Test
    void splitDataZeroRest() {
        final ArrayList<Integer> numbers = new ArrayList<>();
        numbers.add(6);
        numbers.add(8);
        numbers.add(7);
        numbers.add(13);
        numbers.add(5);
        numbers.add(9);
        numbers.add(4);
        numbers.add(8);
        final JobDataRecord originTaskData = new JobDataRecord(numbers);
        final ArrayList<JobDataRecord> expectedTasks = new ArrayList<>();
        ArrayList<Integer> sublist1 = new ArrayList<>();
        sublist1.add(6);
        sublist1.add(8);
        sublist1.add(7);
        sublist1.add(13);
        expectedTasks.add(new JobDataRecord(sublist1));
        ArrayList<Integer> sublist2 = new ArrayList<>();
        sublist2.add(5);
        sublist2.add(9);
        sublist2.add(4);
        sublist2.add(8);
        expectedTasks.add(new JobDataRecord(sublist2));
        assertEquals(
            expectedTasks, JobManager.divideJobData(originTaskData, 2)
        );
    }

    @Test
    void splitDataNonZeroRest() {
        final ArrayList<Integer> numbers = new ArrayList<>();
        numbers.add(6);
        numbers.add(8);
        numbers.add(7);
        numbers.add(13);
        numbers.add(5);
        numbers.add(9);
        numbers.add(4);
        final JobDataRecord originTaskData = new JobDataRecord(numbers);
        final ArrayList<JobDataRecord> expectedTasks = new ArrayList<>();
        ArrayList<Integer> sublist1 = new ArrayList<>();
        sublist1.add(6);
        sublist1.add(8);
        expectedTasks.add(new JobDataRecord(sublist1));
        ArrayList<Integer> sublist2 = new ArrayList<>();
        sublist2.add(7);
        sublist2.add(13);
        expectedTasks.add(new JobDataRecord(sublist2));
        ArrayList<Integer> sublist3 = new ArrayList<>();
        sublist3.add(5);
        sublist3.add(9);
        expectedTasks.add(new JobDataRecord(sublist3));
        ArrayList<Integer> sublist4 = new ArrayList<>();
        sublist4.add(4);
        expectedTasks.add(new JobDataRecord(sublist4));
        assertEquals(
            expectedTasks, JobManager.divideJobData(originTaskData, 4)
        );
    }
}