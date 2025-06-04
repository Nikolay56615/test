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
 * Class for testing Variable class.
 */
public class VariableTest {
    private Expression variable;

    @BeforeEach
    public void setUp() throws Exception {
        variable = new Variable("x");
    }

    @Test
    void testSimplifyVariable() {
        assertNotEquals(variable, variable.simplify(),
                "Variable should simplify to itself.");
    }

    @Test
    void printVariable() throws Exception {
        final PrintStream originalOut = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        variable.print();
        String output = outputStream.toString();
        assertTrue(output.contains("x"));
        System.setOut(originalOut);
    }

    @Test
    void derivativeVariable() throws Exception {
        Expression de = variable.derivative("x");
        assertEquals("1", de.toString());
    }

    @Test
    void evalVariable() throws Exception {
        assertEquals(10, variable.eval("x = 10"));
    }

    @Test
    void evalDerivative() throws Exception {
        Expression de = variable.derivative("x");
        Map<String, Integer> variables = new HashMap<>();
        variables.put("x", 10);
        assertEquals(1, de.eval(variables));
        de = variable.derivative("y");
        variables = new HashMap<>();
        variables.put("x", 10);
        assertEquals(0, de.eval(variables));
    }
}


