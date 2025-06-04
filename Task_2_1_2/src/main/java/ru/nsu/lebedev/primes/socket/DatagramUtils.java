package ru.nsu.lebedev.primes.socket;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

/**
 * Utility class for handling datagram packets.
 */
public final class DatagramUtils {

    private DatagramUtils() {
    }

    /**
     * Extracts content from a datagram packet.
     *
     * @param packet the datagram packet
     * @return the extracted string content
     */
    public static String getPacketContent(DatagramPacket packet) {
        return new String(packet.getData(), packet.getOffset(),
            packet.getLength(), StandardCharsets.UTF_8);
    }

    /**
     * Creates a datagram packet from a string.
     *
     * @param data the string data for the packet
     * @return a datagram packet
     */
    public static DatagramPacket buildPacket(String data) {
        byte[] buffer = data.getBytes(StandardCharsets.UTF_8);
        return new DatagramPacket(buffer, buffer.length);
    }

    /**
     * Creates a datagram packet ready for sending.
     *
     * @param data    the string data for the packet
     * @param address the destination address
     * @param port    the destination port
     * @return a datagram packet ready for sending
     */
    public static DatagramPacket buildSendablePacket(String data, InetAddress address, int port) {
        DatagramPacket packet = buildPacket(data);
        packet.setAddress(address);
        packet.setPort(port);
        return packet;
    }
}
