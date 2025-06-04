package ru.nsu.lebedev.graph;

import java.util.Objects;

/**
 * Edge class.
 *
 * @param <T> type of edge's vertices.
 */
public class Edge<T> {
    private final T from;
    private final T to;
    private double weight = 0;

    /**
     * Weighted edge constructor.
     *
     * @param from value of start vertex
     * @param to value of end vertex
     * @param weight double value
     */
    public Edge(T from, T to, double weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    /**
     * From vertex getter.
     *
     * @return from value of start vertex
     */
    public T getFrom() {
        return from;
    }

    /**
     * To vertex getter.
     *
     * @return to value of end vertex
     */
    public T getTo() {
        return to;
    }

    /**
     * Weight getter.
     *
     * @return weight double value
     */
    public double getWeight() {
        return weight;
    }

    /**
     * Change weight of edge.
     *
     * @param weight weight double value
     */
    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Edge<?>)) {
            return false;
        }
        Edge<T> otherEdge = (Edge<T>) obj;
        return from.equals(otherEdge.from) && to.equals(otherEdge.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to);
    }

    @Override
    public String toString() {
        return String.format("%s - %f -> %s", from.toString(), weight, to.toString());
    }
}
