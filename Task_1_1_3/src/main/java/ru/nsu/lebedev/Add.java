package ru.nsu.lebedev;

import java.util.Map;

/**
 * Class with addition realisation.
 */
public class Add extends Expression {
    private Expression left;
    private Expression right;

    /**
     * Function with initialization.
     */
    public Add(Expression left, Expression right) {
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
            int result = simplifiedLeft.eval(Map.of()) + simplifiedRight.eval(Map.of());
            return new Number(result);
        }
        if (simplifiedLeft instanceof Number && ((Number) simplifiedLeft).eval(Map.of()) == 0) {
            return simplifiedRight;
        }
        if (simplifiedRight instanceof Number && ((Number) simplifiedRight).eval(Map.of()) == 0) {
            return simplifiedLeft;
        }
        return new Add(simplifiedLeft, simplifiedRight);
    }

    /**
     * A method for formating add.
     */
    @Override
    public String toString() {
        return "(" + left.toString() + "+" + right.toString() + ")";
    }

    /**
     * Function with derivative of addition.
     */
    @Override
    public Expression derivative(String variable) {
        return new Add(left.derivative(variable), right.derivative(variable));
    }

    /**
     * Function with eval of addition.
     */
    @Override
    public int eval(Map<String, Integer> env) {
        return left.eval(env) + right.eval(env);
    }
}
