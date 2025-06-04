package ru.nsu.lebedev.snake.ai;

import ru.nsu.lebedev.snake.models.ModelGame;

/**
 * Contract for Ai snakes.
 */
public interface AiSnakeContract {
    /**
     * Behavior of the ai snake.
     */
    void updateDirection(ModelGame model);
}
