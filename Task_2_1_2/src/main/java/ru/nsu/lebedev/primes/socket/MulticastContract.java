package ru.nsu.lebedev.primes.socket;

import java.net.DatagramPacket;

/**
 * Contract for processing group-based datagram packets.
 */
public interface MulticastContract {
    /**
     * Processes a received UDP packet from a multicast group.
     *
     * @param packet the received datagram packet
     */
    void process(DatagramPacket packet);
}
