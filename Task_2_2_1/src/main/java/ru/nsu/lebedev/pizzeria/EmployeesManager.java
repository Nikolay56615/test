package ru.nsu.lebedev.pizzeria;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * EmployeesManager is responsible for managing a list of pizzeria employees.
 * SOLID principles applied:
 * <strong>Single Responsibility Principle:</strong>
 * This class is solely responsible for managing the lifecycle
 * of employees.
 * <strong>Dependency Inversion Principle:</strong>
 * It works with employees via the TerminableEmployee interface.
 * <strong>Open/Closed Principle:</strong>
 * New employee types can be added without modifying this class.
 */
public class EmployeesManager<T extends TerminableEmployee> {
    private final ArrayList<T> employees;
    private final ExecutorService executor;

    /**
     * Constructs an EmployeesManager with the given list of employees.
     * An ExecutorService is created to manage employee threads.
     *
     * @param employees the list of employees to be managed.
     */
    public EmployeesManager(ArrayList<T> employees) {
        this.employees = employees;
        this.executor = Executors.newFixedThreadPool(employees.size());
    }

    /**
     * Starts all employee tasks using ExecutorService.
     */
    public void startEmployees() {
        for (T employee : employees) {
            executor.submit(employee);
        }
    }

    /**
     * Signals all employees to finish their work.
     * This method leverages the TerminableEmployee interface, following DIP.
     */
    public void offerEmployeesFinishJob() {
        for (T employee : employees) {
            employee.offerToFinishJob();
        }
    }

    /**
     * Shuts down the ExecutorService and waits for all tasks to complete.
     *
     * @param timeout the maximum time to wait
     * @param unit    the time unit of the timeout argument
     * @throws InterruptedException if interrupted while waiting
     */
    public void shutdownAndAwaitTermination(long timeout, TimeUnit unit)
            throws InterruptedException {
        executor.shutdown();
        if (!executor.awaitTermination(timeout, unit)) {
            executor.shutdownNow();
        }
    }
}
