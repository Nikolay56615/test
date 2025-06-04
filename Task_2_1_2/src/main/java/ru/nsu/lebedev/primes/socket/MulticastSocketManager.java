package ru.nsu.lebedev.primes.socket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Controller for managing multicast groups and their packet processors.
 */
public class MulticastSocketManager {

    private final int groupPort;
    private InetSocketAddress multicastGroup = null;
    private NetworkInterface networkInterface = null;
    private final List<MulticastSocket> sockets = new ArrayList<>();
    private final ExecutorService jobExecutor;
    private static final int BUFFER_SIZE = 2048;

    /**
     * Initializes a multicast group with a thread pool for processing packets.
     *
     * @param groupIp        the multicast group IP address
     * @param port           the port for the multicast group
     * @param threadPoolSize the size of the thread pool
     * @throws IOException if network initialization fails
     */
    public MulticastSocketManager(String groupIp, int port, int threadPoolSize) throws IOException {
        this.groupPort = port;
        this.jobExecutor = Executors.newFixedThreadPool(threadPoolSize,
            r -> new Thread(r, "PacketProcessor-" + System.nanoTime()));
        this.networkInterface = NetworkInterface.getByIndex(0);
        this.multicastGroup = new InetSocketAddress(InetAddress.getByName(groupIp), port);
    }

    /**
     * Registers a packet processor for the multicast group.
     *
     * @param processor the packet processor
     * @return the created multicast socket
     * @throws IOException if registration fails
     */
    public MulticastSocket addPacketProcessor(MulticastContract processor) throws IOException {
        MulticastSocket socket = new MulticastSocket(groupPort);
        socket.joinGroup(multicastGroup, networkInterface);
        sockets.add(socket);
        AtomicBoolean isActive = new AtomicBoolean(true);
        jobExecutor.submit(createProcessorJob(socket, processor, isActive));
        return socket;
    }

    /**
     * Shuts down the thread pool gracefully.
     */
    public void terminate() {
        for (MulticastSocket socket : sockets) {
            try {
                if (!socket.isClosed()) {
                    socket.leaveGroup(multicastGroup, networkInterface);
                    socket.close();
                }
            } catch (IOException e) {
                System.err.println("Error closing multicast socket: " + e.getMessage());
            }
        }
        sockets.clear();
        jobExecutor.shutdown();
        try {
            if (!jobExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                jobExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            jobExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    private Runnable createProcessorJob(
        MulticastSocket socket, MulticastContract processor, AtomicBoolean isActive
    ) {
        return () -> {
            while (isActive.get()) {
                try {
                    byte[] buffer = new byte[BUFFER_SIZE];
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    socket.receive(packet);
                    processor.process(packet);
                } catch (IOException e) {
                    if (isActive.get()) {
                        System.err.println("Packet processing error: " + e.getMessage());
                    }
                    break;
                }
            }
            System.out.println("Processor stopped for socket: " + socket.getLocalPort());
        };
    }
}