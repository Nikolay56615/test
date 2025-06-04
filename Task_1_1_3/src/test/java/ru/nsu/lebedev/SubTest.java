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
 * Class for testing Sub class.
 */
public class SubTest {
    private Expression sub;

    @BeforeEach
    public void setUp() throws Exception {
        sub = new Sub(new Number(5), new Variable("x"));
    }

    @Test
    void testSimplifySubEqualExpressions() {
        Expression sub = new Sub(new Number(5), new Number(5));
        assertEquals("0", sub.simplify().toString(),
                "Subtraction of equal expressions should simplify to 0.");
    }

    @Test
    void testSimplifySubWithConstants() {
        Expression sub = new Sub(new Number(7), new Number(2));
        assertEquals("5", sub.simplify().toString(),
                "Subtraction of constants should simplify to their difference.");
    }

    @Test
    void printSub() throws Exception {
        final PrintStream originalOut = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        sub.print();
        String output = outputStream.toString();
        assertTrue(output.contains("(5-x)"));
        System.setOut(originalOut);
    }

    @Test
    void derivativeSub() throws Exception {
        Expression de = sub.derivative("x");
        assertEquals("(0-1)", de.toString());
    }

    @Test
    void evalSub() throws Exception {
        assertEquals(-5, sub.eval("x = 10"));
    }

    @Test
    void evalDerivative() throws Exception {
        Expression de = sub.derivative("x");
        Map<String, Integer> variables = new HashMap<>();
        variables.put("x", 10);
        assertEquals(-1, de.eval(variables));
    }
}
