package ru.nsu.lebedev.primes.errors;

/**
 * Thrown when an error occurs during worker creation.
 */
public class ErrorWorkerCreation extends RuntimeException {

    /**
     * Unused default constructor.
     */
    public ErrorWorkerCreation() {
        super();
    }

    /**
     * Constructor with a cause.
     *
     * @param cause the cause of the exception
     */
    public ErrorWorkerCreation(Throwable cause) {
        super(cause);
    }
}
