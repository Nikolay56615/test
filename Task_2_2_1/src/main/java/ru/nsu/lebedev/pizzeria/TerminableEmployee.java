package ru.nsu.lebedev.pizzeria;

/**
 * TerminableEmployee interface defines the contract for pizzeria employees.
 * This interface ensures that any employee can be signaled to finish their work.
 * It supports the <strong>Dependency Inversion Principle</strong>
 * by allowing higher-level modules
 * to work with employees via this abstraction.
 */
public interface TerminableEmployee extends Runnable {
    /**
     * Signals the employee to finish their current work.
     */
    void offerToFinishJob();
}
