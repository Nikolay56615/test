package ru.nsu.lebedev.graph;


/**
 * An interface that define a method for reading lines and adding vertices and edges to the graph.
 *
 * @param <T> type of graph's vertices
 */
public interface VertexReader<T> {
    /**
     * Function that reading lines and adding vertices and edges to the graph.
     *
     * @param graph graph upcasted to Graph class.
     * @param line line with information about graph.
     */
    void readVertex(Graph<T> graph, String line);
}