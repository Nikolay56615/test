package ru.nsu.lebedev;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Class for testing Add class.
 */
public class AddTest {
    private Expression add;

    @BeforeEach
    public void setUp() throws Exception {
        add = new Add(new Number(5), new Variable("x"));
    }

    @Test
    void simplifyAddConstants() {
        Expression add = new Add(new Number(2), new Number(3));
        assertEquals("5", add.simplify().toString(),
                "Addition of constants should simplify to their sum.");
    }

    @Test
    void simplifyAddWithVariable() {
        Expression add = new Add(new Variable("x"), new Number(0));
        assertEquals("x", add.simplify().toString(),
                "Adding 0 to a variable should simplify to the variable.");
    }

    @Test
    void printAdd() throws Exception {
        final PrintStream originalOut = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        add.print();
        String output = outputStream.toString();
        assertTrue(output.contains("(5+x)"));
        System.setOut(originalOut);
    }

    @Test
    void derivativeAdd() throws Exception {
        Expression de = add.derivative("x");
        assertEquals("(0+1)", de.toString());
    }

    @Test
    void evalAdd() throws Exception {
        assertEquals(15, add.eval("x = 10"));
    }

    @Test
    void evalDerivative() throws Exception {
        Expression de = add.derivative("x");
        Map<String, Integer> variables = new HashMap<>();
        variables.put("x", 10);
        assertEquals(1, de.eval(variables));
    }
}
