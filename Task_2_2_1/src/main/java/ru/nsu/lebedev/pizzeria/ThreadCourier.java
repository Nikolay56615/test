package ru.nsu.lebedev.pizzeria;

import java.util.LinkedList;
import java.util.Optional;

/**
 * RunnableCourier represents a courier running in its own thread.
 * The courier repeatedly attempts to fill its trunk by polling orders from the stock queue.
 * Then, it delivers orders (simulated with a delay based on order deliveryTime).
 * In case of delivery errors, undelivered orders are returned to the pendingOrders queue.
 */
public class ThreadCourier implements TerminableEmployee {
    private final Courier courierData;
    private final MyBlockingQueue<Order> pendingOrders;
    private final MyBlockingQueue<Order> stock;
    private final LinkedList<Order> vehicle;
    private volatile boolean aboutToFinish = false;

    /**
     * Constructor.
     *
     * @param courierData data about the courier
     * @param pendingOrders queue with pending orders (for returning undelivered orders)
     * @param stock stock queue containing orders ready for delivery
     */
    public ThreadCourier(Courier courierData, MyBlockingQueue<Order> pendingOrders,
                         MyBlockingQueue<Order> stock) {
        this.courierData = courierData;
        this.pendingOrders = pendingOrders;
        this.stock = stock;
        this.vehicle = new LinkedList<>();
    }

    @Override
    public void offerToFinishJob() {
        aboutToFinish = true;
    }

    /**
     * Checks if the courier's vehicle is full based on its capacity.
     *
     * @return true if vehicle is full, false otherwise.
     */
    private boolean isVehicleFull() {
        return vehicle.size() >= courierData.capacity();
    }

    /**
     * Fills the vehicle by polling orders from the stock.
     * Uses a timeout to prevent indefinite waiting.
     */
    private void fillVehicle() {
        while (!isVehicleFull()) {
            try {
                Optional<Order> optOrder = stock.poll(Pizzeria.ORDER_TIMEOUT_MS);
                if (optOrder.isEmpty()) {
                    break;
                }
                Order order = optOrder.get();
                vehicle.add(order);
                System.out.println("Courier " + courierData
                    + " got order " + order + " from stock");
            } catch (InterruptedException e) {
                System.out.println("Courier " + courierData
                    + " interrupted while polling stock: " + e.getMessage());
                break;
            }
        }
    }

    /**
     * Attempts to deliver all orders in the vehicle.
     *
     * @return true if delivery succeeded for all orders, false if interrupted.
     */
    private boolean deliverOrders() {
        while (!vehicle.isEmpty()) {
            Order order = vehicle.poll();
            try {
                Thread.sleep((long) order.deliveryTime() * Pizzeria.TIME_STEP_MS);
                System.out.println("Courier " + courierData
                    + " delivered order " + order);
            } catch (InterruptedException e) {
                System.out.println("Courier " + courierData
                    + " interrupted while delivering order " + order
                    + ": " + e.getMessage());
                vehicle.add(order);
                return false;
            }
        }
        return true;
    }

    /**
     * Returns undelivered orders from the vehicle back to the pendingOrders queue.
     */
    private void returnOrders() {
        for (Order order : vehicle) {
            try {
                pendingOrders.add(order);
            } catch (InterruptedException e) {
                System.out.println("Courier " + courierData
                    + " interrupted while returning order " + order + ": " + e.getMessage());
            } catch (IllegalStateException e) {
                System.out.println("Courier " + courierData
                    + " cannot return order " + order + " (queue closed): " + e.getMessage());
            }
        }
        vehicle.clear();
    }

    /**
     * Main loop for the courier thread.
     * Continues filling vehicle and delivering orders until signaled to finish
     * and the stock becomes empty.
     */
    @Override
    public void run() {
        while (!aboutToFinish || !stock.isEmpty()) {
            fillVehicle();
            if (!deliverOrders()) {
                returnOrders();
            }
        }
        System.out.println("Courier " + courierData + " finished work.");
    }
}
