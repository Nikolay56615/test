package ru.nsu.lebedev.snake.game;

/**
 * Enum representing movement directions as vectors.
 */
public enum GameVector {
    UP(new GamePoint(0, -1)),
    RIGHT(new GamePoint(1, 0)),
    DOWN(new GamePoint(0, 1)),
    LEFT(new GamePoint(-1, 0));

    private final GamePoint offset;

    /**
     * Constructor for the enum.
     *
     * @param point A point representing the x and y displacement of the vector.
     */
    GameVector(GamePoint point) {
        this.offset = point;
    }

    /**
     * Gets the vector's displacement as a point.
     *
     * @return A Point object representing the x and y offsets.
     */
    public GamePoint getOffset() {
        return offset;
    }

    /**
     * Checks whether two vectors are collinear (pointing in the same or opposite directions).
     *
     * @param other The vector to compare with.
     * @return True if both vectors are either vertical or horizontal, false otherwise.
     */
    public boolean isCollinear(GameVector other) {
        return (this == UP || this == DOWN) && (other == UP || other == DOWN)
            || (this == LEFT || this == RIGHT) && (other == LEFT || other == RIGHT);
    }
}
