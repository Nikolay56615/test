package ru.nsu.lebedev;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Class for testing Number class.
 */
public class NumberTest {
    private Expression number;

    @BeforeEach
    public void setUp() throws Exception {
        number = new Number(5);
    }

    @Test
    void simplifyNumber() {
        assertNotEquals(number, number.simplify(),
                "Number should simplify to itself (dif obj).");
    }

    @Test
    void printNumber() throws Exception {
        final PrintStream originalOut = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        number.print();
        String output = outputStream.toString();
        assertTrue(output.contains("5"));
        System.setOut(originalOut);
    }

    @Test
    void derivativeNumber() throws Exception {
        Expression de = number.derivative("x");
        assertEquals("0", de.toString());
    }

    @Test
    void evalNumber() throws Exception {
        assertEquals(5, number.eval("x = 10"));
    }

    @Test
    void evalDerivative() throws Exception {
        Expression de = number.derivative("x");
        Map<String, Integer> variables = new HashMap<>();
        variables.put("x", 10);
        assertEquals(0, de.eval(variables));
    }
}


