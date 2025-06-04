package ru.nsu.lebedev.pizzeria;

/**
 * Record class representing a baker, implementing JsonSerializable for JSON serialization.
 *
 * @param name Name of the baker.
 * @param cookingTime Cooking time in conventional units.
 */
public record Baker(String name, int cookingTime) implements JsonSerializable {}