package ru.nsu.lebedev;

import java.util.Map;

/**
 * Class with division realisation.
 */
public class Div extends Expression {
    private Expression left;
    private Expression right;

    /**
     * Function with initialization.
     */
    public Div(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    /**
     * Rule a: Evaluate if both sides are numbers.
     *
     * @return simplify Expression.
     */
    @Override
    public Expression simplify() {
        Expression simplifiedLeft = left.simplify();
        Expression simplifiedRight = right.simplify();
        if (simplifiedLeft instanceof Number && simplifiedRight instanceof Number) {
            int result = simplifiedLeft.eval(Map.of()) / simplifiedRight.eval(Map.of());
            return new Number(result);
        }
        if (simplifiedRight instanceof Number && simplifiedRight.eval(Map.of()) == 1) {
            return simplifiedLeft;
        }
        if (simplifiedLeft instanceof Number && simplifiedLeft.eval(Map.of()) == 0) {
            return new Number(0);
        }
        return new Div(simplifiedLeft, simplifiedRight);
    }

    /**
     * A method for formating div.
     */
    @Override
    public String toString() {
        return "(" + left.toString() + "/"
                + right.toString() + ")";
    }

    /**
     * Function with derivative of division.
     * By rule: (f / g)' = (f' * g - f * g') / g^2.
     */
    @Override
    public Expression derivative(String variable) {
        return new Div(
            new Sub(new Mul(left.derivative(variable), right),
                new Mul(left, right.derivative(variable))),
            new Mul(right, right)
        );
    }

    /**
     * Function with eval of division.
     */
    @Override
    public int eval(Map<String, Integer> env) {
        return left.eval(env) / right.eval(env);
    }
}
