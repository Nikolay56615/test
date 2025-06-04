package ru.nsu.lebedev.primes;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import org.junit.jupiter.api.Test;

/**
 * Class for testing Main class.
 */
class MainTest {

    @Test
    void callMain() throws IOException {
        Main.main(new String[]{});
        assertTrue(true);
    }
}
