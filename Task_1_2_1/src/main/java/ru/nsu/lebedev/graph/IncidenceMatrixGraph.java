package ru.nsu.lebedev.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Incidence Matrix Graph class implementation.
 * raw - vertex; col - edge.
 *
 * @param <T> type of graph's vertices
 */
public class IncidenceMatrixGraph<T> implements Graph<T> {
    private final Map<T, Vertex<T>> vertices = new HashMap<>();
    private final Map<T, Integer> vertexIndex = new HashMap<>();
    private final List<Map.Entry<Edge<T>, Double>> edges = new ArrayList<>();
    private final List<List<Double>> incMatrix = new ArrayList<>();

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
        incMatrix.add(new ArrayList<>(Collections.nCopies(edges.size(), null)));
    }

    @Override
    public Vertex<T> removeVertex(T value) {
        if (!vertices.containsKey(value)) {
            return null;
        }
        int index = vertexIndex.remove(value);
        vertices.remove(value);
        incMatrix.remove(index);
        for (List<Double> row : incMatrix) {
            row.remove(index);
        }
        return new Vertex<>(value);
    }

    @Override
    public Edge<T> getEdge(T a, T b) {
        for (Map.Entry<Edge<T>, Double> edge : edges) {
            if (edge.getKey().getFrom().equals(a) && edge.getKey().getTo().equals(b)) {
                return edge.getKey();
            }
        }
        return null;
    }

    @Override
    public void addEdge(T a, T b, double weight) {
        addVertex(a);
        addVertex(b);
        Edge<T> newEdge = new Edge<>(a, b, weight);
        edges.add(Map.entry(newEdge, weight));
        for (List<Double> row : incMatrix) {
            row.add(null);
        }
        int indexA = vertexIndex.get(a);
        int indexB = vertexIndex.get(b);
        incMatrix.get(indexA).set(edges.size() - 1, weight);
        incMatrix.get(indexB).set(edges.size() - 1, -weight);
    }

    @Override
    public Edge<T> removeEdge(T a, T b) {
        for (int i = 0; i < edges.size(); i++) {
            Edge<T> edge = edges.get(i).getKey();
            if (edge.getFrom().equals(a) && edge.getTo().equals(b)) {
                Edge<T> removedEdge = edges.remove(i).getKey();
                for (List<Double> row : incMatrix) {
                    row.remove(i);
                }
                return removedEdge;
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
        for (int i = 0; i < edges.size(); i++) {
            Double weight = incMatrix.get(index).get(i);
            if (weight != null && weight > 0) {
                adjacentVertices.add(edges.get(i).getKey().getTo());
            }
        }
        return adjacentVertices;
    }
}

