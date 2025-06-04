package ru.nsu.lebedev.primes.workers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import ru.nsu.lebedev.primes.errors.ErrorWorkerCreation;
import ru.nsu.lebedev.primes.socket.MulticastSocketManager;

/**
 * Manages a pool of worker nodes.
 */
public final class WorkerPool {

    public static final int MAX_PORT_LIMIT = 32000;
    private static final int THREAD_POOL_SIZE = 5;
    private final MulticastSocketManager groupCastCoordinator;
    private final ExecutorService workerExecutor;
    private final ArrayList<Worker> activeWorkers = new ArrayList<>();
    private final ArrayList<Runnable> workerJobs = new ArrayList<>();

    /**
     * Constructor for WorkerPool.
     *
     * @param multicastAddress address for the multicast group
     * @param multicastPort    port for the multicast group
     * @throws IOException if multicast coordinator initialization fails
     */
    public WorkerPool(String multicastAddress, int multicastPort) throws IOException {
        this.groupCastCoordinator = new MulticastSocketManager(multicastAddress, multicastPort, 4);
        this.workerExecutor = Executors.newFixedThreadPool(
            THREAD_POOL_SIZE,
            r -> new Thread(r, "WorkerJob-" + System.nanoTime())
        );
    }

    /**
     * Creates and starts a worker node on an available port.
     *
     * @param initialPort the starting port to attempt worker creation
     * @return the next available port after successful creation, or -1 if creation fails
     */
    public int startWorker(int initialPort) {
        if (initialPort > MAX_PORT_LIMIT) {
            return -1;
        }
        int currentPort = initialPort;
        Worker worker = null;
        while (worker == null && currentPort <= MAX_PORT_LIMIT) {
            try {
                worker = new Worker(currentPort, groupCastCoordinator);
            } catch (ErrorWorkerCreation e) {
                System.err.println(
                    "Unable to initialize worker on port " + currentPort + ": " + e.getMessage()
                );
                currentPort++;
            }
        }
        if (worker == null) {
            return -1;
        }
        activeWorkers.add(worker);
        workerExecutor.submit(worker);
        workerJobs.add(worker);
        System.out.println("Worker on port " + currentPort + " initialized and started");
        return currentPort + 1;
    }

    /**
     * Creates and starts multiple worker nodes.
     *
     * @param initialPort the starting port for worker creation
     * @param workerCount the desired number of workers to create
     * @return the number of workers successfully created and started
     */
    public int startWorkers(int initialPort, int workerCount) {
        int currentPort = initialPort;
        int workersStarted = 0;
        for (; workersStarted < workerCount; workersStarted++) {
            currentPort = startWorker(currentPort);
            if (currentPort == -1) {
                break;
            }
        }
        return workersStarted;
    }

    /**
     * Gracefully shuts down the worker pool by requesting termination of all workers.
     */
    public void terminateGracefully() {
        for (Worker worker : activeWorkers) {
            worker.requestShutdown();
        }
        activeWorkers.clear();
        workerJobs.clear();
        groupCastCoordinator.terminate();
        workerExecutor.shutdown();
        try {
            if (!workerExecutor.awaitTermination(10, TimeUnit.SECONDS)) {
                workerExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            workerExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Forcefully shuts down the worker pool by stopping all worker jobs.
     */
    public void terminateImmediately() {
        workerExecutor.shutdownNow();
        activeWorkers.clear();
        workerJobs.clear();
        groupCastCoordinator.terminate();
    }
}