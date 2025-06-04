package ru.nsu.lebedev.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Adjacency Matrix Graph class implementation.
 * raw - vertex; col - vertex.
 *
 * @param <T> type of graph's vertices
 */
public class AdjacentMatrixGraph<T> implements Graph<T> {
    private final Map<T, Vertex<T>> vertices = new HashMap<>();
    private final Map<T, Integer> vertexIndex = new HashMap<>();
    private final List<List<Double>> adjMatrix = new ArrayList<>();

    @Override
    public Vertex<T> getVertex(T value) {
        return vertices.get(value);
    }

    @Override
    public void addVertex(T value) {
        if (vertices.containsKey(value)) {
            vertices.get(value);
            return;
        }
        Vertex<T> newVertex = new Vertex<>(value);
        vertices.put(value, newVertex);
        vertexIndex.put(value, vertexIndex.size());
        for (List<Double> row : adjMatrix) {
            row.add(null);
        }
        adjMatrix.add(new ArrayList<>(Collections.nCopies(vertices.size(), null)));
    }

    @Override
    public Vertex<T> removeVertex(T value) {
        if (!vertices.containsKey(value)) {
            return null;
        }
        int index = vertexIndex.remove(value);
        vertices.remove(value);
        adjMatrix.remove(index);
        for (List<Double> row : adjMatrix) {
            row.remove(index);
        }
        return new Vertex<>(value);
    }

    @Override
    public void addEdge(T a, T b, double weight) {
        addVertex(a);
        addVertex(b);
        int indexA = vertexIndex.get(a);
        int indexB = vertexIndex.get(b);
        if (adjMatrix.get(indexA).get(indexB) == null) {
            adjMatrix.get(indexA).set(indexB, weight);
        }
    }

    @Override
    public Edge<T> removeEdge(T a, T b) {
        if (!vertexIndex.containsKey(a) || !vertexIndex.containsKey(b)) {
            return null;
        }
        int indexA = vertexIndex.get(a);
        int indexB = vertexIndex.get(b);
        Double weight = adjMatrix.get(indexA).get(indexB);
        if (weight != null) {
            Edge<T> removedEdge = new Edge<>(a, b, weight);
            adjMatrix.get(indexA).set(indexB, null);
            return removedEdge;
        }
        return null;
    }

    @Override
    public Edge<T> getEdge(T a, T b) {
        if (!vertexIndex.containsKey(a) || !vertexIndex.containsKey(b)) {
            return null;
        }
        int indexA = vertexIndex.get(a);
        int indexB = vertexIndex.get(b);
        Double weight = adjMatrix.get(indexA).get(indexB);
        if (weight != null) {
            return new Edge<>(a, b, weight);
        }
        return null;
    }

    /**
     * Getter of vertex by index.
     *
     * @param index of vertex
     * @return vertex or null if it doesn't exist
     */
    private T getVertexByIndex(int index) {
        for (Map.Entry<T, Integer> entry : vertexIndex.entrySet()) {
            if (entry.getValue() == index) {
                return entry.getKey();
            }
        }
        return null;
    }

    @Override
    public List<T> getVertices() {
        return new ArrayList<>(vertices.keySet());
    }

    @Override
    public List<T> getAdjacentVertices(T vertex) {
        List<T> adjacentVertices = new ArrayList<>();
        int index = vertexIndex.get(vertex);
        for (int i = 0; i < adjMatrix.get(index).size(); i++) {
            if (adjMatrix.get(index).get(i) != null) {
                adjacentVertices.add(getVertexByIndex(i));
            }
        }
        return adjacentVertices;
    }
}
