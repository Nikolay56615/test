package ru.nsu.lebedev.primes.jobs;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Optional;
import ru.nsu.lebedev.primes.errors.ErrorParsingJson;
import ru.nsu.lebedev.primes.errors.ErrorSocketStreamEnd;
import ru.nsu.lebedev.primes.errors.ErrorWorkerCalculation;
import ru.nsu.lebedev.primes.errors.ErrorWorkerNotFound;
import ru.nsu.lebedev.primes.socket.MulticastUtils;
import ru.nsu.lebedev.primes.socket.TcpSocket;

/**
 * Coordinator for managing jobs distributed across workers.
 */
public class JobManager {

    private final JobDataRecord jobData;
    private final int workersPerJob;
    private final int coordinatorPort;
    private final int groupCastPort;

    /**
     * Constructor for JobCoordinator.
     *
     * @param jobData         data for the job to be processed
     * @param workersPerJob   number of workers needed to process the job
     * @param groupCastPort   port for discovering workers via multicast
     * @param coordinatorPort port of the clientManager's socket
     */
    public JobManager(
        JobDataRecord jobData, int workersPerJob,
        int groupCastPort, int coordinatorPort
    ) {
        this.jobData = jobData;
        this.workersPerJob = workersPerJob;
        this.groupCastPort = groupCastPort;
        this.coordinatorPort = coordinatorPort;
    }

    /**
     * Executes the job by distributing sub-jobs to workers and collecting results.
     *
     * @return the result of the job execution wrapped in an Optional
     * @throws ErrorWorkerNotFound    if not enough workers are available
     * @throws ErrorWorkerCalculation if a worker fails and cannot be replaced
     */
    public Optional<JobResultRecord> executeJob()
        throws ErrorWorkerNotFound, ErrorWorkerCalculation {
        System.out.println("Initiating job manager on port " + coordinatorPort);
        ArrayList<JobDataRecord> subJobs = divideJobData(jobData, workersPerJob);
        LinkedList<JobWorkerActive> activeWorkers = assembleWorkerQueue(subJobs);
        JobResultRecord finalResult = new JobResultRecord(false);
        while (!activeWorkers.isEmpty()) {
            JobWorkerActive activeJob = activeWorkers.poll();
            try {
                JobResultRecord workerResult = TcpSocket.getJsonObject(
                    activeJob.workerConnection(), JobResultRecord.class
                );
                if (workerResult.result()) {
                    finalResult = workerResult;
                    break;
                }
            } catch (IOException | ErrorParsingJson | ErrorSocketStreamEnd e) {
                System.err.println("Worker error detected. Attempting to reassign sub-job...");
                try {
                    findWorkerConnection(activeJob.jobData())
                        .ifPresent(activeWorkers::add);
                } catch (ErrorWorkerNotFound notFoundException) {
                    throw new ErrorWorkerCalculation();
                }
            }
        }
        return Optional.of(finalResult);
    }

    /**
     * Finds a new worker connection for a given sub-job.
     *
     * @param subJob the sub-job to assign to a worker
     * @return an Optional containing the ActiveWorkerJob if a worker is found, otherwise empty
     * @throws ErrorWorkerNotFound if no worker is available
     */
    private Optional<JobWorkerActive> findWorkerConnection(JobDataRecord subJob)
        throws ErrorWorkerNotFound {
        if (subJob.numbers().isEmpty()) {
            return Optional.empty();
        }
        Socket workerConnection = MulticastUtils.establishTcpConnection(
            coordinatorPort, groupCastPort
        );
        if (workerConnection == null) {
            throw new ErrorWorkerNotFound();
        }
        System.out.println("Assigned worker: " + workerConnection.getRemoteSocketAddress());
        try {
            TcpSocket.postJsonObject(workerConnection, subJob);
        } catch (IOException e) {
            System.err.println("Failed to communicate with worker: " + e.getMessage());
        }
        return Optional.of(new JobWorkerActive(subJob, workerConnection));
    }

    /**
     * Assembles a queue of active workers for the given sub-jobs.
     *
     * @param subJobs list of sub-jobs to assign to workers
     * @return a LinkedList of ActiveWorkerJobs
     * @throws ErrorWorkerNotFound if not enough workers are available for non-empty sub-jobs
     */
    private LinkedList<JobWorkerActive> assembleWorkerQueue(ArrayList<JobDataRecord> subJobs)
        throws ErrorWorkerNotFound {
        LinkedList<JobWorkerActive> workers = new LinkedList<>();
        for (JobDataRecord subJob : subJobs) {
            findWorkerConnection(subJob).ifPresent(workers::add);
        }
        return workers;
    }

    /**
     * Divides the job data into a specified number of segments.
     *
     * @param jobData  the job data to be segmented
     * @param segments the number of segments desired
     * @return a list of TaskData segments
     */
    public static ArrayList<JobDataRecord> divideJobData(JobDataRecord jobData, int segments) {
        if (segments <= 0) {
            return new ArrayList<>();
        }
        final int numbersSize = jobData.numbers().size();
        int segmentSize = numbersSize / segments;
        if (numbersSize % segments != 0) {
            segmentSize++;
        }
        ArrayList<JobDataRecord> subJobs = new ArrayList<>();
        for (int i = 0; i < segments; ++i) {
            final int startIndex = i * segmentSize;
            final int endIndex = Math.min(startIndex + segmentSize, numbersSize);
            if (endIndex <= startIndex) {
                break;
            }
            ArrayList<Integer> sublist = new ArrayList<>();
            for (int j = startIndex; j < endIndex; ++j) {
                sublist.add(jobData.numbers().get(j));
            }
            subJobs.add(new JobDataRecord(sublist));
        }
        return subJobs;
    }
}
