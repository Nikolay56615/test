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
 * Class for testing Mul class.
 */
public class MulTest {
    private Expression mul;

    @BeforeEach
    public void setUp() throws Exception {
        mul = new Mul(new Number(5), new Variable("x"));
    }

    @Test
    void simplifyMulByZero() {
        Expression mul = new Mul(new Number(5), new Number(0));
        assertEquals("0", mul.simplify().toString(),
                "Multiplication by 0 should simplify to 0.");
    }

    @Test
    void simplifyMulByOne() {
        Expression mul = new Mul(new Variable("x"), new Number(1));
        assertEquals("x", mul.simplify().toString(),
                "Multiplication by 1 should simplify to the variable.");
    }

    @Test
    void printMul() throws Exception {
        final PrintStream originalOut = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        mul.print();
        String output = outputStream.toString();
        assertTrue(output.contains("(5*x)"));
        System.setOut(originalOut);
    }

    @Test
    void derivativeMul() throws Exception {
        Expression de = mul.derivative("x");
        assertEquals("((0*x)+(5*1))", de.toString());
    }

    @Test
    void evalMul() throws Exception {
        assertEquals(50, mul.eval("x = 10"));
    }

    @Test
    void evalDerivative() throws Exception {
        Expression de = mul.derivative("x");
        Map<String, Integer> variables = new HashMap<>();
        variables.put("x", 10);
        assertEquals(5, de.eval(variables));
    }
}

