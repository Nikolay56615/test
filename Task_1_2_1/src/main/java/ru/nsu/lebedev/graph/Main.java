package ru.nsu.lebedev.graph;

import java.io.IOException;
import java.util.List;

/**
 * Main class.
 */
public class Main {
    /**
     * Main function.
     */
    public static void main(String[] args) throws IOException {
        AdjacentMatrixGraph<String> graph1 = new AdjacentMatrixGraph<>();
        IncidenceMatrixGraph<String> graph2 = new IncidenceMatrixGraph<>();
        AdjacentListGraph<String> graph3 = new AdjacentListGraph<>();
        DefaultVertexReader reader = new DefaultVertexReader();
        Graph.readDataForGraphFromFile(graph1, "file.txt", reader);
        Graph.readDataForGraphFromFile(graph2, "file.txt", reader);
        Graph.readDataForGraphFromFile(graph3, "file.txt", reader);
        TopologicalSort<String> topSort = new TopologicalSort<>();
        List<String> sorted1 = topSort.sort(graph1);
        List<String> sorted2 = topSort.sort(graph2);
        List<String> sorted3 = topSort.sort(graph3);
        System.out.println(sorted1);
        System.out.println(sorted2);
        System.out.println(sorted3);
    }
}