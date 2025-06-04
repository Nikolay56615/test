package ru.nsu.lebedev.snake.game;

import java.util.LinkedList;
import ru.nsu.lebedev.snake.models.ModelGame;

/**
 * Represents the snake in the game.
 */
public class GameSnake {

    private final GamePoint head;
    private final LinkedList<GamePoint> body = new LinkedList<>();
    private GamePoint previousTail;
    private GameVector direction;
    private final int fieldWidth;
    private final int fieldHeight;
    private boolean isDead = false;
    private final ModelGame gameModel;

    /**
     * Constructs a snake with a specified size and starting position.
     *
     * @param initialSize      Initial size of the snake.
     * @param startPoint       Starting position of the snake.
     * @param initialDirection Initial movement direction.
     * @param gameModel        Reference to the game model.
     */
    public GameSnake(int initialSize, GamePoint startPoint, GameVector initialDirection,
        ModelGame gameModel) {
        if (initialSize < 1) {
            throw new IllegalArgumentException("Snake must have a positive initial length");
        }
        this.gameModel = gameModel;
        this.fieldWidth = gameModel.getCurrentFieldWidth();
        this.fieldHeight = gameModel.getCurrentFieldHeight();

        head = startPoint.copy();
        direction = initialDirection;
        for (int i = 0; i < initialSize - 1; ++i) {
            move();
            grow();
        }
    }

    /**
     * Sets the movement direction if it's not collinear with the current one.
     *
     * @param newDirection The new direction.
     * @return True if the direction was changed, false otherwise.
     */
    public boolean setDirection(GameVector newDirection) {
        if (!direction.isCollinear(newDirection)) {
            direction = newDirection;
            return true;
        }
        return false;
    }

    /**
     * Gets the snake's direction.
     *
     * @return The direction.
     */
    public GameVector getDirection() {
        return direction;
    }

    /**
     * Gets the snake's head.
     *
     * @return The head position.
     */
    public GamePoint getHead() {
        return head;
    }

    /**
     * Gets the snake's body (excluding the head).
     *
     * @return The body as a linked list of points.
     */
    public LinkedList<GamePoint> getBody() {
        return body;
    }

    /**
     * Gets the full body of the snake, including the head.
     *
     * @return A list of all snake's positions.
     */
    public LinkedList<GamePoint> getWholeBody() {
        LinkedList<GamePoint> wholeBody = new LinkedList<>();
        wholeBody.add(head.copy());
        for (var point : body) {
            wholeBody.add(point.copy());
        }
        return wholeBody;
    }

    /**
     * Gets the snake's tail position.
     *
     * @return The last body segment, or the head if the body is empty.
     */
    public GamePoint getTail() {
        return body.isEmpty() ? head : body.getLast();
    }

    /**
     * Gets the total size of the snake.
     *
     * @return The number of segments (body + head).
     */
    public int getSize() {
        return body.size() + 1;
    }

    /**
     * Moves the snake in its current direction, updating its position.
     */
    public void move() {
        body.addFirst(head.copy());
        head.move(direction, 0, fieldWidth - 1, 0, fieldHeight - 1);
        previousTail = getTail().copy();
        body.removeLast();
        isDead = isAlive();
    }

    /**
     * Increases the snake's length by one segment.
     */
    public void grow() {
        body.addLast(previousTail.copy());
        previousTail = null;
    }

    /**
     * Checks whether the snake is still alive.
     *
     * @return True if the snake is alive, false otherwise.
     */
    public boolean isAlive() {
        return !head.isInList(gameModel.getNonKillingCells(this));
    }

    /**
     * Checks whether the snake is dead.
     *
     * @return True if the snake has died, false otherwise.
     */
    public boolean isDead() {
        return isDead;
    }
}

