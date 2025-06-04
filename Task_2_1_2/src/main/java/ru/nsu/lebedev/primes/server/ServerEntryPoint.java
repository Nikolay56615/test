package ru.nsu.lebedev.primes.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import ru.nsu.lebedev.primes.clients.ClientManager;

/**
 * Entry point for the server, handling incoming client connections.
 */
public class ServerEntryPoint implements Runnable {

    private static final int THREAD_POOL_SIZE = 6;
    private static final int SOCKET_BACKLOG = 50;
    public static final int CONNECTION_TIMEOUT = 1200;
    private final int serverPort;
    private final int groupCastPort;
    private final int maxConnections;
    private final int workersPerJob;
    private ServerSocket serverSocket = null;
    private final ExecutorService clientPool;

    /**
     * Constructor for ServerEntryPoint.
     *
     * @param serverPort     port for the server to listen on
     * @param groupCastPort  port for multicast communication with workers
     * @param maxConnections maximum number of client connections to handle (-1 for unlimited)
     * @param workersPerJob  number of workers required per job
     */
    public ServerEntryPoint(int serverPort, int groupCastPort, int maxConnections,
        int workersPerJob) {
        this.serverPort = serverPort;
        this.groupCastPort = groupCastPort;
        this.maxConnections = maxConnections;
        this.workersPerJob = workersPerJob;
        this.clientPool = Executors.newFixedThreadPool(
            THREAD_POOL_SIZE,
            r -> new Thread(r, "ClientSession-" + System.currentTimeMillis())
        );
        initializeServerSocket(serverPort);
    }

    /**
     * Initializes the server socket with the specified port and backlog.
     *
     * @param port the port to bind the server socket to
     */
    private void initializeServerSocket(int port) {
        try {
            serverSocket = new ServerSocket(port, SOCKET_BACKLOG);
            serverSocket.setSoTimeout(CONNECTION_TIMEOUT);
        } catch (IOException e) {
            System.err.println(
                "Unable to initialize server socket on thread " + Thread.currentThread().getName()
                    + ": " + e.getMessage()
            );
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieves the hostname of the server socket.
     *
     * @return the hostname of the server socket
     */
    public String getHostName() {
        return serverSocket.getInetAddress().getHostName();
    }

    /**
     * Retrieves the port of the server socket.
     *
     * @return the server port
     */
    public int getPort() {
        return serverPort;
    }

    /**
     * Main loop for accepting client connections and delegating to session coordinators.
     * Continues until the maximum number of connections is reached or no active sessions remain.
     */
    @Override
    public void run() {
        int connectionCount = 0;
        List<Future<?>> activeSessions = new ArrayList<>();
        try {
            while (maxConnections == -1 || !activeSessions.isEmpty()
                || connectionCount < maxConnections) {
                activeSessions.removeIf(Future::isDone);
                Socket clientSocket = null;
                try {
                    clientSocket = serverSocket.accept();
                } catch (SocketTimeoutException e) {
                    continue;
                } catch (IOException e) {
                    System.err.println("Failed to accept new client connection: " + e.getMessage());
                    continue;
                }
                System.out.println(
                    "New connection established from " + clientSocket.getRemoteSocketAddress());
                ClientManager session = new ClientManager(clientSocket, groupCastPort,
                    workersPerJob);
                Future<?> sessionFuture = clientPool.submit(session);
                activeSessions.add(sessionFuture);
                connectionCount++;
            }
        } catch (Exception e) {
            System.err.println("Unexpected error in server loop: " + e.getMessage());
        } finally {
            shutdown();
            try {
                serverSocket.close();
            } catch (IOException e) {
                System.err.println("Failed to close server socket: " + e.getMessage());
            }
            System.out.println("Server entry point shut down");
        }
    }

    /**
     * Shuts down the thread pool gracefully.
     */
    public void shutdown() {
        clientPool.shutdown();
    }
}
