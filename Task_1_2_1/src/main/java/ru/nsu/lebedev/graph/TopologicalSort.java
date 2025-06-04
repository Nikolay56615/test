package ru.nsu.lebedev.graph;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Topological sorter.
 */
public class TopologicalSort<T> {

    /**
     * Topological sorter.
     *
     * @param graph any type of graph
     * @return list of vertices in topological sort
     */
    public List<T> sort(Graph<T> graph) {
        List<T> sorted = new ArrayList<>();
        Set<T> visited = new HashSet<>();
        Deque<T> stack = new ArrayDeque<>();
        for (T vertex : graph.getVertices()) {
            if (!visited.contains(vertex)) {
                dfs(graph, vertex, visited, stack);
            }
        }
        while (!stack.isEmpty()) {
            sorted.add(stack.pop());
        }
        return sorted;
    }

    /**
     * DFS method for graph.
     *
     * @param graph any type of graph
     * @param vertex vertex of search
     * @param visited list of visited vertices
     * @param stack topological sort stack
     */
    private void dfs(Graph<T> graph, T vertex, Set<T> visited, Deque<T> stack) {
        visited.add(vertex);
        for (T adjacent : graph.getAdjacentVertices(vertex)) {
            if (!visited.contains(adjacent)) {
                dfs(graph, adjacent, visited, stack);
            }
        }
        stack.push(vertex);
    }
}

