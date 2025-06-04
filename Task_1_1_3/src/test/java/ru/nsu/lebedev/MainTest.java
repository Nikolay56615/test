package ru.nsu.lebedev;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Class for testing Main class.
 */
class MainTest {
    @Test
    void callMain() {
        Main.main(new String[]{});
        assertTrue(true);
    }
}