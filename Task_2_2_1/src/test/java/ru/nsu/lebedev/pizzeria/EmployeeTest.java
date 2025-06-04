package ru.nsu.lebedev.pizzeria;

import java.util.ArrayList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests for employees.
 */
public class EmployeeTest {

    @Test
    void bakerFailOrderTest() {
        ArrayList<Order> orders = new ArrayList<>();
        orders.add(new Order("a", 1));
        MyBlockingQueue<Order> newOrders = new MyBlockingQueue<>(orders);
        MyBlockingQueue<Order> warehouse = new MyBlockingQueue<>(1);
        warehouse.close();
        ThreadBaker baker = new ThreadBaker(
                new Baker("Test", 5), newOrders, warehouse
        );
        Thread bakerThread = new Thread(baker);
        bakerThread.start();
        try {
            Thread.sleep(Pizzeria.TIME_STEP_MS);
            Assertions.assertTrue(newOrders.isEmpty(),
                "Очередь новых заказов должна быть пуста");
        } catch (InterruptedException e) {
            Assertions.fail("Тест прерван: " + e.getMessage());
        }
        baker.offerToFinishJob();
        try {
            Thread.sleep(5 * Pizzeria.TIME_STEP_MS);
            Assertions.assertFalse(newOrders.isEmpty(),
                "Очередь новых заказов не должна быть пуста");
        } catch (InterruptedException e) {
            Assertions.fail("Тест прерван: " + e.getMessage());
        }
    }

    @Test
    void courierFailOrderTest() {
        ArrayList<Order> orders = new ArrayList<>();
        orders.add(new Order("a", 1000));
        MyBlockingQueue<Order> newOrders = new MyBlockingQueue<>(orders);
        MyBlockingQueue<Order> warehouse = new MyBlockingQueue<>(1);
        ThreadBaker baker = new ThreadBaker(
                new Baker("Test", 1), newOrders, warehouse
        );
        ThreadCourier courier = new ThreadCourier(
                new Courier("Test", 1), newOrders, warehouse
        );
        new Thread(baker).start();
        Thread courierThread = new Thread(courier);
        courierThread.start();
        try {
            Thread.sleep(5 * Pizzeria.TIME_STEP_MS);
            Assertions.assertTrue(newOrders.isEmpty());
            Assertions.assertTrue(warehouse.isEmpty());
        } catch (InterruptedException e) {
            Assertions.fail();
        }
        baker.offerToFinishJob();
        courier.offerToFinishJob();
        newOrders.close();
        courierThread.interrupt();
        try {
            Thread.sleep(5 * Pizzeria.TIME_STEP_MS);
            Assertions.assertTrue(newOrders.isEmpty());
        } catch (InterruptedException e) {
            Assertions.fail();
        }
    }
}