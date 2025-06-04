package ru.nsu.lebedev.pizzeria;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import org.junit.jupiter.api.Test;

/**
 * Main test.
 */
class MainTest {
    @Test
    void callMain() throws IOException {
        Main.main(new String[]{});
        assertTrue(true);
    }
}