package ru.nsu.lebedev.snake.game;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Represents a point in a 2D coordinate system.
 */
public class GamePoint {

    private Integer x1;
    private Integer y1;

    /**
     * Constructs a point with the given coordinates.
     *
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     */
    public GamePoint(Integer x, Integer y) {
        this.x1 = x;
        this.y1 = y;
    }

    /**
     * Gets the x-coordinate.
     *
     * @return The x value.
     */
    public Integer getX1() {
        return x1;
    }

    /**
     * Gets the y-coordinate.
     *
     * @return The y value.
     */
    public Integer getY1() {
        return y1;
    }

    /**
     * Moves the point in the specified direction.
     *
     * @param vector The movement vector.
     */
    public void move(GameVector vector) {
        x1 += vector.getOffset().getX1();
        y1 += vector.getOffset().getY1();
    }

    /**
     * Moves the point within specified boundaries (wraps around when exceeding limits).
     *
     * @param vector The movement vector.
     * @param minX   Minimum x boundary.
     * @param maxX   Maximum x boundary.
     * @param minY   Minimum y boundary.
     * @param maxY   Maximum y boundary.
     */
    public void move(GameVector vector, int minX, int maxX, int minY, int maxY) {
        x1 += vector.getOffset().getX1();
        if (x1 < minX) {
            x1 = maxX;
        } else if (x1 > maxX) {
            x1 = minX;
        }

        y1 += vector.getOffset().getY1();
        if (y1 < minY) {
            y1 = maxY;
        } else if (y1 > maxY) {
            y1 = minY;
        }
    }

    /**
     * Creates a copy of this point.
     *
     * @return A new Point instance with the same coordinates.
     */
    public GamePoint copy() {
        return new GamePoint(x1, y1);
    }

    /**
     * Checks if this point is present in a given list.
     *
     * @param list The list to check.
     * @return True if the point exists in the list, false otherwise.
     */
    public boolean isInList(List<GamePoint> list) {
        if (list == null) {
            return false;
        }
        return list.contains(this);
    }

    /**
     * Finds the index of this point in a given list.
     *
     * @param list The list to check.
     * @return The index of the first occurrence wrapped in an Optional, or Optional.empty()
     */
    public Optional<Integer> getListCollision(List<GamePoint> list) {
        if (list == null) {
            return Optional.empty();
        }
        for (int i = 0; i < list.size(); ++i) {
            if (this.equals(list.get(i))) {
                return Optional.of(i);
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        GamePoint point = (GamePoint) obj;
        return x1.equals(point.x1) && y1.equals(point.y1);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x1, y1);
    }
}