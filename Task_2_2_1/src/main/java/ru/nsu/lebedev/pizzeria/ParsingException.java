package ru.nsu.lebedev.pizzeria;

import java.io.IOException;

/**
 * Custom exception to handle errors that occur during the parsing of input data.
 */
public class ParsingException extends IOException {
    /**
     * Default constructor.
     * Not intended for direct use, provided for serialization.
     */
    public ParsingException() {
        super();
    }

    /**
     * Constructs a new ParsingException with the specified cause.
     * The cause is an exception produced by JsonParse - MismatchedInputException.
     *
     * @param cause the underlying exception that caused the parsing error
     */
    public ParsingException(Throwable cause) {
        super(cause);
    }
}
