package ru.nsu.lebedev.pizzeria;

import java.util.ArrayList;

/**
 * Record class representing the setup data for the pizzeria.
 * This class implements JsonSerializable for JSON serialization.
 *
 * @param bakers List of bakers
 * @param couriers List of couriers
 * @param warehouseCapacity Capacity of the warehouse
 * @param orders List of orders
 */
public record Setup(
        ArrayList<Baker> bakers,
        ArrayList<Courier> couriers,
        int warehouseCapacity,
        ArrayList<Order> orders
) implements JsonSerializable {}
