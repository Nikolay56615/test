package ru.nsu.lebedev.primes.errors;

/**
 * Thrown when the end of the socket stream is reached during parsing.
 */
public class ErrorSocketStreamEnd extends Exception {
    /**
     * Unused default constructor.
     */
    public ErrorSocketStreamEnd() {
        super();
    }

    /**
     * Constructs a new ErrorSocketStreamEnd with the specified cause.
     * The cause is an exception produced by JsonParse - MismatchedInputException.
     *
     * @param cause the cause of the exception
     */
    public ErrorSocketStreamEnd(Throwable cause) {
        super(cause);
    }
}
