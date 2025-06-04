package ru.nsu.lebedev.primes.workers;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.MulticastSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import ru.nsu.lebedev.primes.checkers.SimpleChecker;
import ru.nsu.lebedev.primes.checkers.UnprimeChecker;
import ru.nsu.lebedev.primes.errors.ErrorParsingJson;
import ru.nsu.lebedev.primes.errors.ErrorSocketStreamEnd;
import ru.nsu.lebedev.primes.errors.ErrorWorkerCreation;
import ru.nsu.lebedev.primes.jobs.JobDataRecord;
import ru.nsu.lebedev.primes.jobs.JobResultRecord;
import ru.nsu.lebedev.primes.socket.DatagramUtils;
import ru.nsu.lebedev.primes.socket.MulticastContract;
import ru.nsu.lebedev.primes.socket.MulticastSocketManager;
import ru.nsu.lebedev.primes.socket.TcpSocket;

/**
 * Worker node for processing jobs received via TCP connections.
 */
public class Worker implements Runnable {

    public static final int WORKER_TIMEOUT = 1200;
    private static final int THREAD_POOL_SIZE = 4;
    private final Integer workerPort;
    private final AtomicBoolean isAvailable = new AtomicBoolean(true);
    private volatile boolean isShutdownRequested = false;
    private ServerSocket workerServerSocket = null;
    private MulticastSocket groupCastSocket = null;
    private final ExecutorService jobPool;

    /**
     * Constructor for Worker.
     *
     * @param workerPort          port for the TCP server socket
     * @param groupCastController controller for registering multicast packet processor
     * @throws ErrorWorkerCreation if an error occurs during worker initialization
     */
    public Worker(Integer workerPort, MulticastSocketManager groupCastController)
        throws ErrorWorkerCreation {
        this.workerPort = workerPort;
        this.jobPool = Executors.newFixedThreadPool(
            THREAD_POOL_SIZE,
            r -> new Thread(r, "WorkerProcessor-" + System.currentTimeMillis())
        );
        try {
            workerServerSocket = new ServerSocket(workerPort);
            workerServerSocket.setSoTimeout(WORKER_TIMEOUT);
            groupCastSocket = groupCastController.addPacketProcessor(getWorkerPacketProcessor());
        } catch (IOException e) {
            jobPool.shutdown();
            throw new ErrorWorkerCreation(e);
        }
    }

    /**
     * Creates a packet processor for handling multicast requests.
     *
     * @return a MulticastContract that responds to multicast requests with the worker port
     */
    private MulticastContract getWorkerPacketProcessor() {
        return (DatagramPacket packet) -> {
            if (!isAvailable.get()) {
                return;
            }
            try {
                String request = DatagramUtils.getPacketContent(packet);
                int responsePort;
                try {
                    responsePort = Integer.parseInt(request.trim());
                } catch (NumberFormatException e) {
                    System.err.println("Invalid port in multicast request: " + request);
                    return;
                }
                try (DatagramSocket socket = new DatagramSocket(workerPort)) {
                    DatagramPacket response = DatagramUtils.buildSendablePacket(
                        workerPort + "\n", packet.getAddress(), responsePort
                    );
                    socket.send(response);
                }
            } catch (IOException e) {
                System.err.println("Error responding to multicast request: " + e.getMessage());
            }
        };
    }

    /**
     * Main loop for accepting TCP connections and processing jobs.
     * Runs until shutdown is requested or an error occurs.
     */
    @Override
    public void run() {
        Socket clientSocket = null;
        try {
            while (!isShutdownRequested) {
                try {
                    clientSocket = workerServerSocket.accept();
                } catch (SocketTimeoutException e) {
                    continue;
                }
                isAvailable.set(false);
                System.out.println("Accepted connection from "
                    + clientSocket.getRemoteSocketAddress());
                Socket finalClientSocket = clientSocket;
                jobPool.submit(() -> {
                    try {
                        processJob(finalClientSocket);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        System.err.println("Job was interrupted: " + e.getMessage());
                    }
                });
                isAvailable.set(true);
            }
        } catch (IOException e) {
            System.err.println("Worker interrupted during job processing: " + e.getMessage());
        } finally {
            shutdown();
            try {
                workerServerSocket.close();
                if (groupCastSocket != null) {
                    groupCastSocket.close();
                }
                if (clientSocket != null && !clientSocket.isClosed()) {
                    clientSocket.close();
                }
            } catch (IOException e) {
                System.err.println("Failed to close worker sockets: " + e.getMessage());
            }
            System.out.println("Worker terminated");
        }
    }

    /**
     * Requests the worker to shut down.
     */
    public void requestShutdown() {
        isShutdownRequested = true;
    }

    /**
     * Processes a job received from a client socket.
     *
     * @param socket the socket for receiving job data and sending results
     * @throws InterruptedException if the job processing is interrupted
     */
    private void processJob(Socket socket) throws InterruptedException {
        JobDataRecord jobData = new JobDataRecord(new ArrayList<>());
        try {
            jobData = TcpSocket.getJsonObject(socket, JobDataRecord.class);
        } catch (IOException | ErrorSocketStreamEnd e) {
            System.err.println(
                "Failed to receive job data on port " + workerPort
                    + " from " + socket.getRemoteSocketAddress() + ": " + e.getMessage()
            );
            return;
        } catch (ErrorParsingJson e) {
            System.err.println(
                "Failed to parse job data on port " + workerPort
                    + " from " + socket.getRemoteSocketAddress() + ": " + e.getMessage()
            );
            return;
        }
        UnprimeChecker checker = new SimpleChecker().setNumbers(jobData.numbers());
        JobResultRecord result = new JobResultRecord(checker.isAnyUnprime());
        try {
            TcpSocket.postJsonObject(socket, result);
            System.out.println("Sent result to " + socket.getRemoteSocketAddress());
        } catch (IOException e) {
            System.err.println(
                "Failed to send result from worker on port " + workerPort
                    + " to " + socket.getRemoteSocketAddress() + ": " + e.getMessage()
            );
        }
    }

    /**
     * Shuts down the thread pool and releases resources.
     */
    private void shutdown() {
        jobPool.shutdown();
    }
}