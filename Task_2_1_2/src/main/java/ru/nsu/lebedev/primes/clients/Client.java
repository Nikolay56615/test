package ru.nsu.lebedev.primes.clients;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import ru.nsu.lebedev.primes.errors.ErrorMessageRecord;
import ru.nsu.lebedev.primes.errors.ErrorParsingJson;
import ru.nsu.lebedev.primes.jobs.JobDataRecord;
import ru.nsu.lebedev.primes.jobs.JobResultRecord;
import ru.nsu.lebedev.primes.json.Json;
import ru.nsu.lebedev.primes.json.JsonSerializable;
import ru.nsu.lebedev.primes.socket.TcpSocket;

/**
 * Client class for sending jobs to a server and receiving results.
 * Implements Callable to allow execution in a separate thread.
 */
public class Client implements Callable<JsonSerializable> {

    private final JobDataRecord jobData;
    private final String hostname;
    private final int serverPort;

    /**
     * Constructs a new Client with the given job data, server hostname, and port.
     *
     * @param list       the list of integers for the job
     * @param hostname   the server's hostname or IP address
     * @param serverPort the server's port number
     */
    public Client(ArrayList<Integer> list, String hostname, int serverPort) {
        this.jobData = new JobDataRecord(list);
        this.hostname = hostname;
        this.serverPort = serverPort;
    }

    /**
     * Receives the job result from the server. Attempts to deserialize the response as
     * JobResultRecord.
     *
     * @param socket the socket to receive from
     * @return the deserialized job result or an error message
     */
    private JsonSerializable receiveJobResult(Socket socket) {
        try {
            String response = TcpSocket.getString(socket);
            if (response == null) {
                return new ErrorMessageRecord("No response from server, stream closed");
            }
            try {
                return Json.deserialize(response, JobResultRecord.class);
            } catch (ErrorParsingJson e) {
                try {
                    return Json.deserialize(response, ErrorMessageRecord.class);
                } catch (ErrorParsingJson ex) {
                    return new ErrorMessageRecord("Failed to parse server response");
                }
            }
        } catch (IOException e) {
            return new ErrorMessageRecord("IO error while receiving response: " + e.getMessage());
        }
    }

    /**
     * Executes the client's job.
     *
     * @return the job result or an error message
     */
    @Override
    public JsonSerializable call() {
        try (Socket socket = new Socket(hostname, serverPort)) {
            TcpSocket.postJsonObject(socket, jobData);
            if (!Thread.currentThread().isInterrupted()) {
                return receiveJobResult(socket);
            } else {
                return new ErrorMessageRecord("Operation interrupted before receiving result");
            }
        } catch (IOException e) {
            return new ErrorMessageRecord("Failed to connect to server: " + e.getMessage());
        }
    }
}
