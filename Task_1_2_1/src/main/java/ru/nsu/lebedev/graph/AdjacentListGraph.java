package ru.nsu.lebedev.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Adjacency List Graph class implementation.
 *
 * @param <T> type of graph's vertices
 */
public class AdjacentListGraph<T> implements Graph<T> {
    private final Map<T, Vertex<T>> vertices = new HashMap<>();
    private final Map<T, List<Edge<T>>> adjList = new HashMap<>();

    @Override
    public Vertex<T> getVertex(T value) {
        return vertices.get(value);
    }

    @Override
    public void addVertex(T value) {
        if (!vertices.containsKey(value)) {
            Vertex<T> newVertex = new Vertex<>(value);
            vertices.put(value, newVertex);
            adjList.put(value, new ArrayList<>());
        }
    }

    @Override
    public Vertex<T> removeVertex(T value) {
        if (!vertices.containsKey(value)) {
            return null;
        }
        vertices.remove(value);
        adjList.remove(value);
        for (List<Edge<T>> edges : adjList.values()) {
            edges.removeIf(edge -> edge.getTo().equals(value));
        }
        return new Vertex<>(value);
    }

    @Override
    public Edge<T> getEdge(T a, T b) {
        List<Edge<T>> edges = adjList.get(a);
        if (edges != null) {
            for (Edge<T> edge : edges) {
                if (edge.getTo().equals(b)) {
                    return edge;
                }
            }
        }
        return null;
    }

    @Override
    public void addEdge(T a, T b, double weight) {
        addVertex(a);
        addVertex(b);
        Edge<T> newEdge = new Edge<>(a, b, weight);
        adjList.get(a).add(newEdge);
    }

    @Override
    public Edge<T> removeEdge(T a, T b) {
        List<Edge<T>> edges = adjList.get(a);
        if (edges != null) {
            for (int i = 0; i < edges.size(); i++) {
                Edge<T> edge = edges.get(i);
                if (edge.getTo().equals(b)) {
                    edges.remove(i);
                    return edge;
                }
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
        List<Edge<T>> edges = adjList.get(vertex);
        if (edges != null) {
            for (Edge<T> edge : edges) {
                adjacentVertices.add(edge.getTo());
            }
        }
        return adjacentVertices;
    }
}
