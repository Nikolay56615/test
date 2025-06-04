package ru.nsu.lebedev.primes.errors;

/**
 * Utility class with predefined error messages for network communication.
 */
public final class ErrorMessages {
    private ErrorMessages() {}

    public static ErrorMessageRecord workerNotFoundMessage = new ErrorMessageRecord(
        "Server couldn't find worker"
    );

    public static ErrorMessageRecord workerCalculationErrorMessage = new ErrorMessageRecord(
        "Server got error while calculated and cannot restart process"
    );

    public static ErrorMessageRecord jobDataParsingErrorMessage = new ErrorMessageRecord(
        "Send correct data: JSON message with field 'numbers' which is an integers' array"
    );
}
