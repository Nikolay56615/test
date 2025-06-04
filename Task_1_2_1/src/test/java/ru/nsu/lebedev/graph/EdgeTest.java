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
public class EdgeTest {
    private Edge<String> edge;

    /**
     * Function for creating default edge.
     */
    @BeforeEach
    public void setUp() throws Exception {
        edge = new Edge<String>("A", "B", 5.0);
    }

    @Test
    void printEdge() {
        String simulatedInput = "A - 5.0 -> B\n";
        final InputStream originalIn = System.in;
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
        System.out.println(edge);
        System.setIn(originalIn);
    }

    @Test
    void getFromEdge() {
        assertEquals("A", edge.getFrom());
    }

    @Test
    void getToEdge() {
        assertEquals("B", edge.getTo());
    }

    @Test
    void getWeightEdge() {
        assertEquals(5.0, edge.getWeight());
    }

    @Test
    void setWeightEdge() {
        edge.setWeight(6.0);
        assertEquals(6.0, edge.getWeight());
    }

    @Test
    void equalsEdge() {
        Edge<String> edge2 = new Edge<String>("A", "C", 7.0);
        assertNotEquals(edge, edge2);
    }

    @Test
    void hashCodeEdge() {
        assertEquals(edge.hashCode(), edge.hashCode());
    }
}
