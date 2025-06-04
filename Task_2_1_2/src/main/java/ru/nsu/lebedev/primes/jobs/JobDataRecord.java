package ru.nsu.lebedev.primes.jobs;

import java.util.ArrayList;
import ru.nsu.lebedev.primes.json.JsonSerializable;

/**
 * Record class representing a data for each worker, implementing JsonSerializable.
 *
 * @param numbers List of numbers.
 */
public record JobDataRecord(ArrayList<Integer> numbers) implements JsonSerializable {}
