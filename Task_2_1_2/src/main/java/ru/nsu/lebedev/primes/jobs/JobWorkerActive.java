package ru.nsu.lebedev.primes.jobs;

import java.net.Socket;

/**
 * Represents an active worker job, holding the job data and the connection to the worker node.
 *
 * @param jobData The data of the sub-job assigned to the worker.
 * @param workerConnection The socket connection to the worker node processing the sub-job.
 */
record JobWorkerActive(JobDataRecord jobData, Socket workerConnection) {}