package ru.nsu.lebedev.graph;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Class for testing Edge class.
 */
public class VertexTest {
    private Vertex<String> vertex;

    /**
     * Function for creating default vertex.
     */
    @BeforeEach
    public void setUp() throws Exception {
        vertex = new Vertex<>("A");
    }

    @Test
    void printVertex() {
        String simulatedInput = "Vertex A\n";
        final InputStream originalIn = System.in;
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
        System.out.println(vertex);
        System.setIn(originalIn);
    }

    @Test
    void getValueVertex() {
        assertEquals("A", vertex.getValue());
    }

    @Test
    void equalsVertex() {
        Vertex<String> vertex2 = new Vertex<>("C");
        assertNotEquals(vertex, vertex2);
    }

    @Test
    void hashCodeVertex() {
        assertEquals(vertex.hashCode(), vertex.hashCode());
    }
}
