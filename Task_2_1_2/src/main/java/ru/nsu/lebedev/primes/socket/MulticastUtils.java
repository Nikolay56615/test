package ru.nsu.lebedev.primes.socket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * Utility class for broadcast-based socket operations.
 */
public final class MulticastUtils {
    private MulticastUtils() {}

    private static final int BROADCAST_TIMEOUT = 1200;
    private static final int MAX_BROADCAST_ATTEMPTS = 4;

    /**
     * Establishes a TCP socket connection using a broadcast request.
     *
     * @param localPort the local port for the broadcast socket
     * @param targetPort the target port for the broadcast request
     * @return the created TCP socket, or null if connection fails
     */
    public static Socket establishTcpConnection(int localPort, int targetPort) {
        try (DatagramSocket socket = new DatagramSocket(localPort)) {
            socket.setBroadcast(true);
            DatagramPacket packet = DatagramUtils.buildSendablePacket(
                localPort + "\n", InetAddress.getByName("255.255.255.255"), targetPort
            );
            socket.setSoTimeout(BROADCAST_TIMEOUT);
            int attempts = 0;
            while (attempts < MAX_BROADCAST_ATTEMPTS) {
                attempts++;
                try {
                    socket.send(packet);
                    socket.receive(packet);
                    System.out.println("Response received from: " + packet.getSocketAddress());
                    return new Socket(packet.getAddress(), packet.getPort());
                } catch (SocketTimeoutException e) {
                    System.err.println("No response received. Attempt "
                        + attempts + " of " + MAX_BROADCAST_ATTEMPTS);
                    if (attempts >= MAX_BROADCAST_ATTEMPTS) {
                        System.err.println("Max broadcast attempts reached.");
                        return null;
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Broadcast error: " + e.getMessage());
        }
        return null;
    }
}
