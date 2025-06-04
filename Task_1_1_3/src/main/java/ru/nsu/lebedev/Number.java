package ru.nsu.lebedev;

import java.util.Map;

/**
 * Class with constant realisation.
 */
public class Number extends Expression {
    private int value;

    /**
     * Function with initialization.
     */
    public Number(int value) {
        this.value = value;
    }

    /**
     * A number is already in its simplest form, so it returns itself.
     *
     * @return Number.
     */
    @Override
    public Expression simplify() {
        return new Number(value);
    }

    /**
     * A method for formating constant.
     */
    @Override
    public String toString() {
        return String.valueOf(value);
    }

    /**
     * Function with derivative of constant.
     */
    @Override
    public Expression derivative(String variable) {
        return new Number(0);
    }

    /**
     * Function with eval of constant.
     */
    @Override
    public int eval(Map<String, Integer> var) {
        return value;
    }
}
