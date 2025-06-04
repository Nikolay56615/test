package ru.nsu.lebedev;

import java.util.Map;

/**
 * Class with multiplication realisation.
 */
public class Mul extends Expression {
    private Expression left;
    private Expression right;

    /**
     * Function with initialization.
     */
    public Mul(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    /**
     * Rule a: Evaluate if both sides are numbers.
     * Rule b: Multiplication by 0 results in 0.
     * Rule c: Multiplication by 1 results in the other operand.
     *
     * @return simplify Expression.
     */
    @Override
    public Expression simplify() {
        Expression simplifiedLeft = left.simplify();
        Expression simplifiedRight = right.simplify();
        if (simplifiedLeft instanceof Number && simplifiedRight instanceof Number) {
            int result = simplifiedLeft.eval(Map.of()) * simplifiedRight.eval(Map.of());
            return new Number(result);
        }
        if (simplifiedLeft instanceof Number && simplifiedLeft.eval(Map.of()) == 0
                ||
                simplifiedRight instanceof Number && simplifiedRight.eval(Map.of()) == 0) {
            return new Number(0);
        }
        if (simplifiedLeft instanceof Number && simplifiedLeft.eval(Map.of()) == 1) {
            return simplifiedRight;
        }
        if (simplifiedRight instanceof Number && simplifiedRight.eval(Map.of()) == 1) {
            return simplifiedLeft;
        }
        return new Mul(simplifiedLeft, simplifiedRight);
    }

    /**
     * A method for formating mul.
     */
    @Override
    public String toString() {
        return "(" + left.toString() + "*" + right.toString() + ")";
    }

    /**
     * Function with derivative of multiplication.
     * By rule: (f * g)' = f' * g + f * g'.
     */
    @Override
    public Expression derivative(String variable) {
        return new Add(new Mul(left.derivative(variable), right),
            new Mul(left, right.derivative(variable)));
    }

    /**
     * Function with eval of multiplication.
     */
    @Override
    public int eval(Map<String, Integer> env) {
        return left.eval(env) * right.eval(env);
    }
}
