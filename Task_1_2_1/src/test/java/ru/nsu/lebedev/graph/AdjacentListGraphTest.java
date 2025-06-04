package ru.nsu.lebedev.graph;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Class for testing AdjacentListGraph class.
 */
public class AdjacentListGraphTest {
    private Graph<String> graph;

    /**
     * Function for creating default graph.
     */
    @BeforeEach
    public void setUp() throws Exception {
        graph = new AdjacentListGraph<>();
        DefaultVertexReader reader = new DefaultVertexReader();
        Graph.readDataForGraphFromFile(graph, "file.txt", reader);
    }

    @Test
    void getVertexAdjacentListGraph() throws Exception {
        assertEquals(new Vertex<String>("A"), graph.getVertex("A"));
    }

    @Test
    void addVertexAdjacentListGraph() throws Exception {
        graph.addVertex("Q");
        assertEquals(new Vertex<String>("Q"), graph.getVertex("Q"));
    }

    @Test
    void removeVertexAdjacentListGraph() throws Exception {
        Vertex<String> vertex = graph.removeVertex("A");
        assertNull(graph.getVertex("A"));
        assertEquals(new Vertex<String>("A"), vertex);
    }

    @Test
    void removeVertexNullAdjacentListGraph() throws Exception {
        assertNull(graph.removeVertex("Z"));
    }

    @Test
    void getEdgeAdjacentListGraph() throws Exception {
        assertEquals(new Edge<>("A", "B", 1.0), graph.getEdge("A", "B"));
    }

    @Test
    void addEdgeAdjacentListGraph() throws Exception {
        graph.addEdge("A", "Z", 7.0);
        assertEquals(new Edge<>("A", "Z", 7.0), graph.getEdge("A", "Z"));
    }

    @Test
    void removeEdgeAdjacentListGraph() throws Exception {
        Edge<String> edge = graph.removeEdge("A", "B");
        assertEquals(new Edge<>("A", "B", 1.0), edge);
        assertNull(graph.getEdge("A", "B"));
    }

    @Test
    void removeEdgeNullAdjacentListGraph() throws Exception {
        graph.removeEdge("A", "Z");
        assertNull(graph.removeEdge("A", "Z"));
    }

    @Test
    void getVerticesAdjacentListGraph() throws Exception {
        assertEquals(new ArrayList<>(
                Arrays.asList("A", "B", "C", "D", "E", "F")), graph.getVertices());
    }

    @Test
    void getAdjacentVerticesAdjacentListGraph() throws Exception {
        assertEquals(new ArrayList<>(Arrays.asList("B", "C")), graph.getAdjacentVertices("A"));
    }
}
