package ru.nsu.lebedev.pizzeria;

/**
 * Record class representing an order in the pizzeria
 * system, implementing JsonSerializable for JSON serialization.
 *
 * @param id Unique identifier of the order.
 * @param deliveryTime Time for delivering the order, represented in conventional units.
 */
public record Order(String id, int deliveryTime) implements JsonSerializable {}
