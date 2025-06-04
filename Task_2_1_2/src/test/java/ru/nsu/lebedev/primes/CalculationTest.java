package ru.nsu.lebedev.primes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.nsu.lebedev.primes.clients.Client;
import ru.nsu.lebedev.primes.jobs.JobResultRecord;
import ru.nsu.lebedev.primes.json.JsonSerializable;
import ru.nsu.lebedev.primes.server.ServerEntryPoint;
import ru.nsu.lebedev.primes.workers.Worker;
import ru.nsu.lebedev.primes.workers.WorkerPool;

/**
 * Test class for verifying the calculation functionality of the prime number checking system.
 */
public class CalculationTest {

    private static final int SERVICE_PORT = 7070;
    private static final int TASK_WORKERS = 8;
    private static final int TOTAL_WORKERS = 8;
    private static final int MAX_TESTS = 6;
    private static WorkerPool workerGroup;
    private static ServerEntryPoint serverNode;
    private static Thread serverProcess;
    private static List<FutureTask<JsonSerializable>> activeTasks = new ArrayList<>();

    @BeforeAll
    static void setupServerAndWorkers() {
        try {
            workerGroup = new WorkerPool("231.1.1.1", SERVICE_PORT);
        } catch (IOException e) {
            System.err.println("Error creating worker pool: " + e.getMessage());
            fail();
            return;
        }
        serverNode = new ServerEntryPoint(
            SERVICE_PORT, SERVICE_PORT, MAX_TESTS, TASK_WORKERS
        );
        serverProcess = new Thread(serverNode);
        serverProcess.start();
        int activeWorkers = workerGroup.startWorkers(SERVICE_PORT + 1, TOTAL_WORKERS);
        assertTrue(activeWorkers >= TASK_WORKERS);
    }

    @AfterAll
    static void cleanupWorkers() {
        for (FutureTask<JsonSerializable> task : activeTasks) {
            try {
                task.get();
            } catch (ExecutionException | InterruptedException e) {
                fail("Error completing task: " + e.getMessage());
            }
        }
        workerGroup.terminateGracefully();
        try {
            Thread.sleep((Worker.WORKER_TIMEOUT + ServerEntryPoint.CONNECTION_TIMEOUT) * 2);
        } catch (InterruptedException e) {
            fail("Interrupted while waiting for completion: " + e.getMessage());
        }
        assertFalse(serverProcess.isAlive());
    }

    @Test
    void checkEmptyInput() {
        ArrayList<Integer> numbers = new ArrayList<>();
        runTest(numbers, false);
    }

    @Test
    void checkSinglePrime() {
        ArrayList<Integer> numbers = new ArrayList<>();
        numbers.add(5);
        runTest(numbers, false);
    }

    @Test
    void checkSingleComposite() {
        ArrayList<Integer> numbers = new ArrayList<>();
        numbers.add(6);
        runTest(numbers, true);
    }

    @Test
    void checkManyPrimes() {
        ArrayList<Integer> numbers = new ArrayList<>();
        for (int i = 0; i < 200000; i++) {
            numbers.add(999999929);
        }
        runTest(numbers, false);
    }

    @Test
    void checkManyComposites() {
        ArrayList<Integer> numbers = new ArrayList<>();
        for (int i = 0; i < 200000; i++) {
            numbers.add(100000014);
        }
        runTest(numbers, true);
    }

    @Test
    void checkCompositeAtBeginning() {
        ArrayList<Integer> numbers = new ArrayList<>();
        numbers.add(10);
        for (int i = 0; i < 200000; i++) {
            numbers.add(999999929);
        }
        runTest(numbers, true);
    }

    @Test
    void checkCompositeAtFinish() {
        ArrayList<Integer> numbers = new ArrayList<>();
        for (int i = 0; i < 200000; i++) {
            numbers.add(999999929);
        }
        numbers.add(12);
        runTest(numbers, true);
    }

    private void runTest(ArrayList<Integer> numbers, boolean expected) {
        FutureTask<JsonSerializable> task = new FutureTask<>(
            new Client(numbers, serverNode.getHostName(), SERVICE_PORT)
        );
        activeTasks.add(task);
        new Thread(task).start();
        try {
            JsonSerializable result = task.get();
            assertEquals(expected, ((JobResultRecord) result).result());
        } catch (ExecutionException | InterruptedException e) {
            fail("Test execution error: " + e.getMessage());
        }
    }
}