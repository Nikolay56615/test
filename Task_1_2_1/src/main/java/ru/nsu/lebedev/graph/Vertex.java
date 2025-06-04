package ru.nsu.lebedev.graph;


import java.util.Objects;

/**
 * Vertex class.
 *
 * @param <T> type of vertex's value.
 */
public class Vertex<T> {
    private final T value;

    public Vertex(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Vertex<?>)) {
            return false;
        }
        Vertex<T> otherVertex = (Vertex<T>) obj;
        return value.equals(otherVertex.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return String.format("Vertex %s", value.toString());
    }
}
