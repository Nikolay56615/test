package ru.nsu.lebedev.primes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import ru.nsu.lebedev.primes.clients.Client;
import ru.nsu.lebedev.primes.json.JsonSerializable;
import ru.nsu.lebedev.primes.server.ServerEntryPoint;
import ru.nsu.lebedev.primes.workers.Worker;
import ru.nsu.lebedev.primes.workers.WorkerPool;

/**
 * Main class.
 */
public class Main {

    public static final int SERVER_PORT = 8080;

    /**
     * Main entry point.
     *
     * @param args cmd's args
     */
    public static void main(String[] args) {
        ServerEntryPoint entry = new ServerEntryPoint(
            SERVER_PORT, SERVER_PORT, 2, 1
        );
        Thread serverThread = new Thread(entry, "ServerThread");
        serverThread.start();
        WorkerPool workersPool;
        try {
            workersPool = new WorkerPool("230.1.1.1", SERVER_PORT);
        } catch (IOException e) {
            System.err.println("Couldn't create workers' pool: " + e.getMessage());
            return;
        }
        System.out.println(workersPool.startWorkers(
            SERVER_PORT + 1, 100
        ));

        final int billionPrime = 17858849;
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < 100000; i++) {
            list.add(billionPrime);
        }
        FutureTask<JsonSerializable> task = new FutureTask<>(new Client(
            list, entry.getHostName(), entry.getPort()
        ));
        var clientThread = new Thread(task);
        clientThread.start();

        ArrayList<Integer> nunPrimeList = new ArrayList<>(list);
        nunPrimeList.add(4);

        FutureTask<JsonSerializable> secondTask = new FutureTask<>(new Client(
            nunPrimeList, entry.getHostName(), entry.getPort()
        ));
        new Thread(secondTask).start();

        try {
            System.out.println(task.get());
            System.out.println(secondTask.get());
        } catch (InterruptedException | ExecutionException ignored) {
            System.err.println("Interrupted while waiting for client task");
        }
        workersPool.terminateGracefully();
        try {
            Thread.sleep((Worker.WORKER_TIMEOUT + ServerEntryPoint.CONNECTION_TIMEOUT) * 2);
            entry.shutdown();
            if (serverThread.isAlive()) {
                System.err.println("Server thread still alive after shutdown timeout");
                serverThread.interrupt();
            }
        } catch (InterruptedException e) {
            System.err.println("Interrupted during cleanup: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}
