package ru.nsu.lebedev.primes.errors;

/**
 * Custom exception to handle errors that occur during the parsing of input data.
 */
public class ErrorParsingJson extends Exception {

    /**
     * Unused default constructor.
     */
    public ErrorParsingJson() {
        super();
    }

    /**
     * Constructs a new ErrorParsingJson with the specified cause.
     *
     * @param cause the underlying exception that caused the parsing error
     */
    public ErrorParsingJson(Throwable cause) {
        super(cause);
    }
}
