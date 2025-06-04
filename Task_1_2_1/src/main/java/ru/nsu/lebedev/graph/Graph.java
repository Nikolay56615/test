package ru.nsu.lebedev.graph;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 * Graph interface.
 *
 * @param <T> type of graph's vertices
 */
public interface Graph<T> {
    /**
     * Vertex getter.
     *
     * @param value value of vertex
     * @return `Vertex` object if it exists otherwise `null`
     */
    Vertex<T> getVertex(T value);

    /**
     * Vertex creation.
     * Add vertex with specified value if vertex with it doesn't exist.
     *
     * @param value value of vertex
     */
    void addVertex(T value);

    /**
     * Vertex removing.
     * Removes vertex and all incident edges if vertex exists.
     *
     * @param value value of vertex
     * @return deleted vertex or null if it doesn't exist
     */
    Vertex<T> removeVertex(T value);

    /**
     * Edge getter.
     *
     * @param a first vertex value
     * @param b second vertex value
     * @return edge (a, b) if it exists and `null` otherwise
     */
    Edge<T> getEdge(T a, T b);

    /**
     * Edge creation.
     * Creates edge (a,b) with specified weight if edge (a,b) doesn't exist.
     * Creates vertices `a` and `b` if they don't exist
     *
     * @param a first vertex
     * @param b second vertex
     * @param weight double value
     */
    void addEdge(T a, T b, double weight);

    /**
     * Edge removing.
     * Removes edge (a,b) if it exists.
     *
     * @param a first vertex value
     * @param b second vertex value
     * @return deleted edge or null if it doesn't exist
     */
    Edge<T> removeEdge(T a, T b);

    /**
     * Method for reading graph's data from file.
     *
     * @param graph graph upcasted to Graph class.
     * @param filename file with data.
     * @param reader type of realization if interface VertexReader.
     */
    static <T> void readDataForGraphFromFile(Graph<T> graph, String filename,
                                             VertexReader<T> reader) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                reader.readVertex(graph, line);
            }
        }
    }

    /**
     * Method for getting vertices.
     *
     * @return list of all vertices of the graph
     */
    List<T> getVertices();

    /**
     * Method for getting vertices.
     *
     * @param vertex of graph.
     * @return a list of neighbors for the passed vertex.
     */
    List<T> getAdjacentVertices(T vertex);
}
