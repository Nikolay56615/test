package ru.nsu.lebedev.pizzeria;

import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests for MyBlockingQueue class.
 */
public class BlockingQueueTest {
    @Test
    void testIncrementingWithThreads() {
        MyBlockingQueue<Integer> myQueue = new MyBlockingQueue<>(1);
        int[] array = new int[100];
        try {
            myQueue.add(0);
        } catch (IllegalStateException | InterruptedException e) {
            Assertions.fail(e);
        }
        Thread[] threads = new Thread[100];
        for (int i = 0; i < 100; ++i) {
            final int index = i;
            threads[i] = new Thread(() -> {
                try {
                    Optional<Integer> optNum = myQueue.poll();
                    int num = optNum.orElseThrow();
                    myQueue.add(num + 1);
                    synchronized (array) {
                        array[index] += 1;
                    }
                } catch (IllegalStateException | InterruptedException e) {
                    Assertions.fail(e);
                }
            });
            threads[i].start();
        }
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Assertions.fail(e);
            }
        }

        for (int i = 0; i < 100; i++) {
            System.out.println(i + ": " + array[i]);
            Assertions.assertEquals(1, array[i]);
        }
    }

    @Test
    void testPollTimeoutOnEmptyQueue() throws InterruptedException {
        MyBlockingQueue<Integer> queue = new MyBlockingQueue<>(1);
        long timeoutMs = 1000;
        long startTime = System.currentTimeMillis();
        Optional<Integer> result = queue.poll(timeoutMs);
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        Assertions.assertTrue(duration >= timeoutMs,
            "Ожидание должно быть не менее таймаута");
        Assertions.assertTrue(duration < timeoutMs + 100,
            "Ожидание не должно быть слишком долгим");
        Assertions.assertEquals(Optional.empty(), result);
    }

    @Test
    void testPollWithElementAddedDuringWait() throws InterruptedException {
        MyBlockingQueue<Integer> queue = new MyBlockingQueue<>(1);
        long timeoutMs = 2000;
        Thread addThread = new Thread(() -> {
            try {
                Thread.sleep(500);
                queue.add(42);
            } catch (InterruptedException e) {
                Assertions.fail("Прерывание в потоке добавления");
            }
        });
        addThread.start();
        long startTime = System.currentTimeMillis();
        Optional<Integer> result = queue.poll(timeoutMs);
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        Assertions.assertTrue(duration >= 500,
            "Ожидание должно быть не менее 500 мс");
        Assertions.assertTrue(duration < 2000,
            "Ожидание должно быть меньше таймаута");
        Assertions.assertEquals(Optional.of(42), result);
        addThread.join();
    }

    @Test
    void testAddTimeoutOnFullQueue() throws InterruptedException {
        MyBlockingQueue<Integer> queue = new MyBlockingQueue<>(1);
        queue.add(1);
        long timeoutMs = 1000;
        long startTime = System.currentTimeMillis();
        boolean added = queue.add(2, timeoutMs);
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        Assertions.assertTrue(duration >= timeoutMs,
            "Ожидание должно быть не менее таймаута");
        Assertions.assertTrue(duration < timeoutMs + 100,
            "Ожидание не должно быть слишком долгим");
        Assertions.assertFalse(added);
    }

    @Test
    void testAddWithSpaceFreedDuringWait() throws InterruptedException {
        MyBlockingQueue<Integer> queue = new MyBlockingQueue<>(1);
        queue.add(1);
        Thread pollThread = new Thread(() -> {
            try {
                Thread.sleep(500);
                queue.poll();
            } catch (InterruptedException e) {
                Assertions.fail("Прерывание в потоке извлечения");
            }
        });
        pollThread.start();
        long timeoutMs = 2000;
        long startTime = System.currentTimeMillis();
        boolean added = queue.add(2, timeoutMs);
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        Assertions.assertTrue(duration >= 500,
            "Ожидание должно быть не менее 500 мс");
        Assertions.assertTrue(duration < 2000,
            "Ожидание должно быть меньше таймаута");
        Assertions.assertTrue(added);
        Optional<Integer> result = queue.poll();
        Assertions.assertEquals(Optional.of(2), result);
        pollThread.join();
    }

    @Test
    void testPollOnClosedEmptyQueue() throws InterruptedException {
        MyBlockingQueue<Integer> queue = new MyBlockingQueue<>(1);
        queue.close();
        long timeoutMs = 1000;
        long startTime = System.currentTimeMillis();
        Optional<Integer> result = queue.poll(timeoutMs);
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        Assertions.assertTrue(duration < 100,
            "Ожидание должно быть минимальным");
        Assertions.assertEquals(Optional.empty(), result);
    }

    @Test
    void testAddOnClosedQueue() {
        MyBlockingQueue<Integer> queue = new MyBlockingQueue<>(1);
        queue.close();
        Assertions.assertThrows(IllegalStateException.class,
            () -> queue.add(1, 1000));
    }
}