package ru.nsu.lebedev.primes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.junit.jupiter.api.Test;
import ru.nsu.lebedev.primes.clients.Client;
import ru.nsu.lebedev.primes.errors.ErrorMessageRecord;
import ru.nsu.lebedev.primes.errors.ErrorMessages;
import ru.nsu.lebedev.primes.errors.ErrorParsingJson;
import ru.nsu.lebedev.primes.errors.ErrorSocketStreamEnd;
import ru.nsu.lebedev.primes.errors.ErrorWorkerCalculation;
import ru.nsu.lebedev.primes.errors.ErrorWorkerCreation;
import ru.nsu.lebedev.primes.jobs.JobResultRecord;
import ru.nsu.lebedev.primes.json.JsonSerializable;
import ru.nsu.lebedev.primes.server.ServerEntryPoint;
import ru.nsu.lebedev.primes.socket.TcpSocket;
import ru.nsu.lebedev.primes.workers.WorkerPool;

/**
 * Integration tests for verifying system stability under various conditions and heavy loads.
 */
public class StabilityTest {

    private static final int BASE_PORT = 9000;
    private static final int LARGE_LIST_SIZE = 500000;
    private static final int PRIME_NUMBER = 999999937;

    @Test
    void concurrentRequestsTest() {
        int testPort = BASE_PORT + 1;
        int requestCount = 8;
        WorkerPool workerPool = initializeWorkerPool(testPort);
        workerPool.startWorkers(testPort + 1, requestCount);
        ServerEntryPoint server = new ServerEntryPoint(testPort, testPort, requestCount, 8);
        Thread serverThread = new Thread(server);
        serverThread.start();

        ArrayList<Callable<JsonSerializable>> tasks = new ArrayList<>();
        for (int i = 0; i < requestCount; i++) {
            ArrayList<Integer> numbers = new ArrayList<>();
            for (int j = 0; j < LARGE_LIST_SIZE; j++) {
                numbers.add(i * 2);
            }
            tasks.add(new Client(numbers, server.getHostName(), testPort));
        }

        ExecutorService executor = Executors.newFixedThreadPool(requestCount);
        try {
            List<Future<JsonSerializable>> results = executor.invokeAll(tasks);
            int expectedComposites = 7;
            int actualComposites = 0;
            for (Future<JsonSerializable> result : results) {
                if (((JobResultRecord) result.get()).result()) {
                    actualComposites++;
                }
            }
            assertEquals(expectedComposites, actualComposites);
            executor.shutdown();
            workerPool.terminateGracefully();
            pauseForCleanup();
            assertFalse(serverThread.isAlive());
        } catch (ExecutionException | InterruptedException e) {
            fail("Test failed: " + e.getMessage());
        }
    }

    @Test
    void testInvalidInputHandling() {
        int testPort = BASE_PORT + 2;
        ServerEntryPoint server = new ServerEntryPoint(testPort, testPort, 2, 7);
        Thread serverThread = new Thread(server);
        serverThread.start();
        WorkerPool workerPool = initializeWorkerPool(testPort);
        workerPool.startWorkers(testPort + 1, 10);

        try (Socket connection = new Socket(server.getHostName(), testPort)) {
            connection.getOutputStream().write(0);
            connection.getOutputStream().flush();
            try {
                ErrorMessageRecord expectedError = ErrorMessages.jobDataParsingErrorMessage;
                ErrorMessageRecord receivedError = TcpSocket.getJsonObject(connection,
                    ErrorMessageRecord.class);
                assertEquals(expectedError, receivedError);
            } catch (ErrorParsingJson | ErrorSocketStreamEnd e) {
                fail("Error during invalid data processing: " + e.getMessage());
            }
        } catch (IOException e) {
            fail("Connection error: " + e.getMessage());
        }

        ArrayList<Integer> numbers = new ArrayList<>();
        for (int i = 0; i < 600; i++) {
            numbers.add(PRIME_NUMBER);
        }
        FutureTask<JsonSerializable> task = new FutureTask<>(
            new Client(numbers, server.getHostName(), testPort)
        );
        new Thread(task).start();

        try {
            JobResultRecord expected = new JobResultRecord(false);
            assertEquals(expected, task.get());
        } catch (InterruptedException | ExecutionException e) {
            fail("Test failed: " + e.getMessage());
        } finally {
            workerPool.terminateGracefully();
            pauseForCleanup();
            assertFalse(serverThread.isAlive());
        }
    }

    @Test
    void noWorkersAvailableTest() {
        int testPort = BASE_PORT + 3;
        ServerEntryPoint server = new ServerEntryPoint(testPort, testPort, 1, 8);
        Thread serverThread = new Thread(server);
        serverThread.start();

        ArrayList<Integer> numbers = new ArrayList<>();
        for (int i = 0; i < 800; i++) {
            numbers.add(PRIME_NUMBER);
        }
        FutureTask<JsonSerializable> task = new FutureTask<>(
            new Client(numbers, server.getHostName(), testPort)
        );
        new Thread(task).start();

        try {
            ErrorMessageRecord expected = ErrorMessages.workerNotFoundMessage;
            assertEquals(expected, task.get());
            pauseForCleanup();
            assertFalse(serverThread.isAlive());
        } catch (InterruptedException | ExecutionException e) {
            fail("Test failed: " + e.getMessage());
        }
    }

    @Test
    void testWorkerDisconnectDuringTask() {
        int testPort = BASE_PORT + 4; // Unique port to avoid conflicts
        ServerEntryPoint server = new ServerEntryPoint(testPort, testPort, 1, 1);
        Thread serverThread = new Thread(server, "ServerThread");
        serverThread.start();

        WorkerPool workerPool = initializeWorkerPool(testPort);
        int workersStarted = workerPool.startWorkers(testPort + 1, 1);
        assertEquals(1, workersStarted, "Failed to start one worker");

        ArrayList<Integer> numbers = new ArrayList<>();
        for (int i = 0; i < LARGE_LIST_SIZE; i++) {
            numbers.add(PRIME_NUMBER);
        }
        FutureTask<JsonSerializable> task = new FutureTask<>(
            new Client(numbers, server.getHostName(), server.getPort())
        );
        Thread clientThread = new Thread(task);
        clientThread.start();

        try {
            // Wait to ensure the worker starts processing
            Thread.sleep(1000);
            System.out.println("Terminating worker pool to simulate disconnection");
            workerPool.terminateImmediately();

            JsonSerializable result = task.get(10, TimeUnit.SECONDS);
            assertTrue(result instanceof ErrorMessageRecord, "Expected error message");
            ErrorMessageRecord error = (ErrorMessageRecord) result;
            assertEquals(
                ErrorMessages.workerCalculationErrorMessage.message(),
                error.message(),
                "Expected worker computation error, got: " + error.message()
            );

            workerPool.terminateGracefully();
            server.shutdown();
            serverThread.join(5000);
            assertFalse(serverThread.isAlive(), "Server thread still alive after shutdown");
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            fail("Test failed: " + e.getMessage());
        }
    }

    @Test
    void testErrorWorkerCalculationDefaultConstructor() {
        ErrorWorkerCalculation exception = new ErrorWorkerCalculation();
        assertNotNull(exception, "Exception should be instantiated");
    }


    @Test
    void testErrorWorkerCreationDefaultConstructor() {
        ErrorWorkerCreation exception = new ErrorWorkerCreation();
        assertNotNull(exception, "Exception should be instantiated");
    }

    @Test
    void testErrorWorkerCreationWithCause() {
        Throwable cause = new IOException("Failed to create worker");
        ErrorWorkerCreation exception = new ErrorWorkerCreation(cause);
        assertEquals(cause.toString(), exception.getMessage(),
            "Message should match cause's toString");
    }


    private WorkerPool initializeWorkerPool(int multicastPort) {
        try {
            return new WorkerPool("231.1.1.1", multicastPort);
        } catch (IOException e) {
            System.err.println("Failed to initialize worker pool: " + e.getMessage());
            fail();
            return null;
        }
    }

    private void pauseForCleanup() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            fail("Interrupted during cleanup pause: " + e.getMessage());
        }
    }
}