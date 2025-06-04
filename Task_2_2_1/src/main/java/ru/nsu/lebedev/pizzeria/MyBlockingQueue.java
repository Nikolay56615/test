package ru.nsu.lebedev.pizzeria;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * MyBlockingQueue is a custom implementation of a blocking queue
 * using ReentrantLock and Conditions.
 * SOLID principles applied:
 * - <strong>Single Responsibility Principle</strong>:
 * The class manages thread-safe access to the queue.
 * - <strong>Open/Closed Principle</strong>:
 * It can be extended with new features without modifying its core behavior.
 * Key conditions:
 * - notEmpty: Signals threads waiting to poll an element
 * that the queue is no longer empty.
 * - notFull: Signals threads waiting to add an element
 * that space is available.
 * The lock is used to ensure that checking conditions and
 * performing queue operations happen atomically.
 *
 * @param <T> the type of elements held in this queue.
 */
public class MyBlockingQueue<T> {
    private final int maxSize;
    private final LinkedList<T> list = new LinkedList<>();

    private final ReentrantLock queueLock = new ReentrantLock();
    private final Condition notEmpty = queueLock.newCondition();
    private final Condition notFull = queueLock.newCondition();
    private volatile boolean closed = false;

    /**
     * Constructs a blocking queue with the specified maximum size.
     *
     * @param maxSize the maximum number of elements the queue can hold.
     */
    public MyBlockingQueue(int maxSize) {
        this.maxSize = maxSize;
    }

    /**
     * Constructs a blocking queue with initial data, setting maximum size to data size.
     *
     * @param data initial list of elements for the queue.
     */
    public MyBlockingQueue(ArrayList<T> data) {
        this.maxSize = data.size();
        list.addAll(data);
    }

    /**
     * Returns the maximum capacity of the queue.
     *
     * @return the maximum size of the queue.
     */
    public int maxSize() {
        return maxSize;
    }

    /**
     * Unreliable, reliable only for a moment after returning data.
     * Returns the current size of the queue, ensuring thread safety with a lock.
     *
     * @return the current number of elements in the queue.
     */
    public int size() {
        queueLock.lock();
        try {
            return list.size();
        } finally {
            queueLock.unlock();
        }
    }

    /**
     * Unreliable, reliable only for a moment after returning data.
     * Checks if the queue is empty, ensuring thread safety with a lock.
     *
     * @return true if the queue is empty, false otherwise.
     */
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Returns a shallow copy of the current queue elements, ensuring thread safety.
     *
     * @return a list containing the current elements.
     */
    public ArrayList<T> getListCopy() {
        queueLock.lock();
        try {
            if (!closed) {
                throw new IllegalStateException("Queue must be closed to get a reliable copy");
            }
            return new ArrayList<>(list);
        } finally {
            queueLock.unlock();
        }
    }

    /**
     * Closes the queue, preventing further additions and signaling waiting threads.
     * After closing, poll() will return Optional.empty() if the queue is empty.
     */
    public void close() {
        queueLock.lock();
        try {
            closed = true;
            notEmpty.signalAll();
            notFull.signalAll();
        } finally {
            queueLock.unlock();
        }
    }

    /**
     * Retrieves and removes the head of the queue, waiting
     * if empty until an element is added
     * or the queue is closed. If the queue is closed and
     * empty, returns Optional.empty().
     *
     * @return an Optional containing the head element, or Optional.empty().
     * @throws InterruptedException if interrupted while waiting.
     */
    public Optional<T> poll() throws InterruptedException {
        queueLock.lock();
        try {
            while (list.isEmpty() && !closed) {
                notEmpty.await();
            }
            if (list.isEmpty() && closed) {
                return Optional.empty();
            }
            T element = list.poll();
            notFull.signal();
            assert element != null;
            return Optional.of(element);
        } finally {
            queueLock.unlock();
        }
    }

    /**
     * Retrieves and removes the head of the queue with a timeout,
     * waiting up to the specified time
     * for an element to become available. If the queue is closed
     * and empty, returns Optional.empty().
     *
     * @param timeoutMs the maximum time to wait in milliseconds.
     * @return an Optional containing the head element, or Optional.empty().
     * @throws InterruptedException if interrupted while waiting.
     */
    public Optional<T> poll(long timeoutMs) throws InterruptedException {
        queueLock.lock();
        try {
            long remainingTime = timeoutMs;
            while (list.isEmpty() && !closed && remainingTime > 0) {
                long start = System.currentTimeMillis();
                notEmpty.await(remainingTime, TimeUnit.MILLISECONDS);
                remainingTime -= (System.currentTimeMillis() - start);
            }
            if (list.isEmpty() && (closed || remainingTime <= 0)) {
                return Optional.empty();
            }
            T element = list.poll();
            notFull.signal();
            assert element != null;
            return Optional.of(element);
        } finally {
            queueLock.unlock();
        }
    }

    /**
     * Adds an element to the queue, waiting if full until space is available.
     * Throws IllegalStateException if the queue is closed.
     *
     * @param element the element to add.
     * @throws IllegalStateException if the queue is closed.
     * @throws InterruptedException if interrupted while waiting.
     */
    public void add(T element) throws InterruptedException {
        if (closed) {
            throw new IllegalStateException("Queue is closed");
        }
        queueLock.lock();
        try {
            while (list.size() == maxSize && !closed) {
                notFull.await();
            }
            if (closed) {
                throw new IllegalStateException("Queue is closed");
            }
            list.add(element);
            notEmpty.signal();
        } finally {
            queueLock.unlock();
        }
    }

    /**
     * Adds an element to the queue with a timeout, waiting
     * up to the specified time for space to become available.
     * Throws IllegalStateException if the queue is closed.
     *
     * @param element the element to add.
     * @param timeoutMs the maximum time to wait in milliseconds.
     * @return true if the element was added, false if the timeout occurred.
     * @throws IllegalStateException if the queue is closed.
     * @throws InterruptedException if interrupted while waiting.
     */
    public boolean add(T element, long timeoutMs) throws InterruptedException {
        if (closed) {
            throw new IllegalStateException("Queue is closed");
        }
        queueLock.lock();
        try {
            long remainingTime = timeoutMs;
            while (list.size() == maxSize && !closed && remainingTime > 0) {
                long start = System.currentTimeMillis();
                notFull.await(remainingTime, TimeUnit.MILLISECONDS);
                remainingTime -= (System.currentTimeMillis() - start);
            }
            if (list.size() == maxSize || closed) {
                return false;
            }
            list.add(element);
            notEmpty.signal();
            return true;
        } finally {
            queueLock.unlock();
        }
    }
}