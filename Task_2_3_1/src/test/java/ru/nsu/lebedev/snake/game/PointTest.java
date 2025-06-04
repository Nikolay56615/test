package ru.nsu.lebedev.snake.game;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the GamePoint class.
 */
public class PointTest {

    @Test
    void shouldWrapAroundWhenMovingLeftAtMinX() {
        GamePoint point = new GamePoint(0, 1);
        point.move(GameVector.LEFT, 0, 1, 0, 1);
        Assertions.assertEquals(new GamePoint(1, 1), point);
    }

    @Test
    void shouldWrapAroundWhenMovingRightAtMaxX() {
        GamePoint point = new GamePoint(1, 1);
        point.move(GameVector.RIGHT, 0, 1, 0, 1);
        Assertions.assertEquals(new GamePoint(0, 1), point);
    }

    @Test
    void shouldWrapAroundWhenMovingUpAtMinY() {
        GamePoint point = new GamePoint(1, 0);
        point.move(GameVector.UP, 0, 1, 0, 1);
        Assertions.assertEquals(new GamePoint(1, 1), point);
    }

    @Test
    void shouldWrapAroundWhenMovingDownAtMaxY() {
        GamePoint point = new GamePoint(1, 1);
        point.move(GameVector.DOWN, 0, 1, 0, 1);
        Assertions.assertEquals(new GamePoint(1, 0), point);
    }
}
