package ru.nsu.lebedev.primes.clients;

import java.io.IOException;
import java.net.Socket;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import ru.nsu.lebedev.primes.errors.ErrorMessageRecord;
import ru.nsu.lebedev.primes.errors.ErrorMessages;
import ru.nsu.lebedev.primes.errors.ErrorParsingJson;
import ru.nsu.lebedev.primes.errors.ErrorSocketStreamEnd;
import ru.nsu.lebedev.primes.errors.ErrorWorkerCalculation;
import ru.nsu.lebedev.primes.errors.ErrorWorkerNotFound;
import ru.nsu.lebedev.primes.jobs.JobDataRecord;
import ru.nsu.lebedev.primes.jobs.JobManager;
import ru.nsu.lebedev.primes.jobs.JobResultRecord;
import ru.nsu.lebedev.primes.socket.TcpSocket;

/**
 * Coordinator for managing client sessions and job processing with error handling.
 */
public class ClientManager implements Runnable {

    private static final int THREAD_POOL_SIZE = 5;
    private final int multicastPort;
    private final Socket sessionSocket;
    private final int workersPerJob;
    private final ExecutorService jobPool;

    /**
     * Constructor for ClientManager.
     * Initializes the session with client socket and thread pool for job processing.
     *
     * @param sessionSocket the TCP socket for client communication
     * @param multicastPort the port for multicast communication with workers
     * @param workersPerJob the number of workers required per job
     */
    public ClientManager(
        Socket sessionSocket, int multicastPort, int workersPerJob
    ) {
        this.sessionSocket = sessionSocket;
        this.multicastPort = multicastPort;
        this.workersPerJob = workersPerJob;
        this.jobPool = Executors.newFixedThreadPool(
            THREAD_POOL_SIZE,
            r -> new Thread(r, "SessionJob-" + System.currentTimeMillis())
        );
    }

    /**
     * Main execution method for handling client requests.
     * Continuously fetches job data and processes valid jobs in the thread pool.
     */
    @Override
    public void run() {
        try {
            while (true) {
                fetchJobData()
                    .ifPresent(jobData ->
                        jobPool.submit(createJobHandler(jobData))
                    );
            }
        } catch (ErrorSocketStreamEnd e) {
            System.out.println("Client disconnected: " + sessionSocket.getRemoteSocketAddress());
        } catch (IllegalArgumentException e) {
            System.err.println("Session error: " + e.getMessage());
        } finally {
            shutdown();
        }
    }

    /**
     * Shuts down the thread pool gracefully.
     */
    public void shutdown() {
        jobPool.shutdown();
    }

    /**
     * Fetches and validates job data from the client socket.
     * Sends an error message to the client if validation fails.
     *
     * @return an Optional containing JobData if valid, or empty if invalid
     * @throws ErrorSocketStreamEnd if the client disconnects
     */
    private Optional<JobDataRecord> fetchJobData() throws ErrorSocketStreamEnd {
        try {
            JobDataRecord jobData = TcpSocket.getJsonObject(
                sessionSocket, JobDataRecord.class
            );
            return Optional.of(jobData);
        } catch (ErrorParsingJson e) {
            System.err.println("Failed to parse job data: " + e.getMessage());
            sendErrorMessage(ErrorMessages.jobDataParsingErrorMessage,
                "Job parsing failed");
        } catch (IOException e) {
            System.err.println("Socket communication error: " + e.getMessage());
        }
        return Optional.empty();
    }

    /**
     * Creates a job handler for processing validated job data.
     *
     * @param jobData the validated job data
     * @return a Runnable for processing the job and sending results to the client
     */
    private Runnable createJobHandler(JobDataRecord jobData) {
        return () -> {
            try {
                Optional<JobResultRecord> result = new JobManager(
                    jobData, workersPerJob, multicastPort, sessionSocket.getPort()
                ).executeJob();
                result.ifPresent(jobResult -> {
                    try {
                        TcpSocket.postJsonObject(sessionSocket, jobResult);
                        System.out.println(
                            "Sent result to client: " + sessionSocket.getRemoteSocketAddress());
                    } catch (IOException e) {
                        System.err.println(
                            "Failed to send result to " + sessionSocket.getRemoteSocketAddress()
                                + ": " + e.getMessage()
                        );
                    }
                });
            } catch (ErrorWorkerNotFound e) {
                System.err.println("No workers found: " + e.getMessage());
                sendErrorMessage(ErrorMessages.workerNotFoundMessage, "Worker unavailable");
            } catch (ErrorWorkerCalculation e) {
                System.err.println("Worker calculation failed: " + e.getMessage());
                sendErrorMessage(ErrorMessages.workerCalculationErrorMessage,
                    "Worker computation error");
            }
        };
    }

    /**
     * Sends an error message to the client and logs the issue.
     *
     * @param errorMessage the error message to send
     * @param logMessage the message to log locally
     */
    private void sendErrorMessage(ErrorMessageRecord errorMessage, String logMessage) {
        try {
            TcpSocket.postJsonObject(sessionSocket, errorMessage);
        } catch (IOException e) {
            System.err.println(
                logMessage + " for client " + sessionSocket.getRemoteSocketAddress()
                    + ": " + e.getMessage()
            );
        }
    }
}
