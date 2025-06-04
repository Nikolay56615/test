package ru.nsu.lebedev.primes.errors;

import ru.nsu.lebedev.primes.json.JsonSerializable;

/**
 * Record for an error message sent over the network.
 *
 * @param message the error message string
 */
public record ErrorMessageRecord(String message) implements JsonSerializable {}
