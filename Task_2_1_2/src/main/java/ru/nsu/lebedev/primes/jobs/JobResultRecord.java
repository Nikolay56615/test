package ru.nsu.lebedev.primes.jobs;

import ru.nsu.lebedev.primes.json.JsonSerializable;

/**
 * Record class representing a result from each worker, implementing JsonSerializable.
 *
 * @param result Boolean result of job.
 */
public record JobResultRecord(Boolean result) implements JsonSerializable {}