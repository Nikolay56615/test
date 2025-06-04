package ru.nsu.lebedev.pizzeria;

/**
 * Record class representing a courier, implementing JsonSerializable for JSON serialization.
 *
 * @param name Name of the courier.
 * @param capacity Vehicle size in conventional units.
 */
public record Courier(String name, int capacity) implements JsonSerializable {}
