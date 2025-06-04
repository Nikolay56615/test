package ru.nsu.lebedev;

import java.util.HashMap;
import java.util.Map;

/**
 * Main class for project.
 */
public class Main {

    /**
     * Function with observing of classes.
     */
    public static void main(String[] args) {
        Expression expression = new Add(new Add(new Number(3), new Mul(new Number(2),
            new Variable("x"))), new Variable("yxx"));
        System.out.print("Expression: ");
        expression.print(); // (3+(2*x))
        Expression de = expression.derivative("yxx");
        System.out.print("Derivative: ");
        de.print(); // (0+((0*x)+(2*1)))
        Map<String, Integer> variables = new HashMap<>();
        variables.put("x", 10);
        System.out.println("Evaluation: " + expression.eval("x = 10; yxx = 13"));  // 36
        System.out.println("Evaluation: " + expression.eval(variables));  // 23
        expression = ExpressionParser.parse("(3+2)*x + 1 * 2"); // (((3+2)*x)+(1*2))
        expression.print();
        variables = new HashMap<>();
        System.out.println("Evaluation: " + expression.eval(variables));  // 2
    }
}