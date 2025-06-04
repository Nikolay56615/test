package ru.nsu.lebedev;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Class for testing ExpressionParser class.
 */
public class ExpressionParserTest {
    private Expression expression;

    @BeforeEach
    public void setUp() throws Exception {
        expression = new Add(new Add(new Number(3), new Mul(new Number(2),
            new Variable("x"))), new Variable("yxx"));
    }

    @Test
    void printExpressionParser() throws Exception {
        expression = ExpressionParser.parse("(3+2)*x + 1 * 2");
        assertEquals("(((3+2)*x)+(1*2))", expression.toString());
    }

    @Test
    void printExpressionParser2() throws Exception {
        String input = "2 + 3 * x - 5 / y";
        Expression expression = ExpressionParser.parse(input);
        assertEquals("((2+(3*x))-(5/y))", expression.toString());
    }
}


