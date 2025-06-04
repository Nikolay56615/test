package ru.nsu.lebedev.snake.ai;

import java.util.Random;
import ru.nsu.lebedev.snake.game.GameSnake;
import ru.nsu.lebedev.snake.game.GameVector;
import ru.nsu.lebedev.snake.models.ModelGame;

/**
 * AI snake that moves randomly.
 */
public class RandomAiSnake implements AiSnakeContract {

    private final Random random = new Random();

    @Override
    public void updateDirection(ModelGame model) {
        GameSnake snake = model.getSnakes().stream()
            .filter(s -> !s.equals(model.getPlayerSnake()))
            .findFirst()
            .orElse(null);
        if (snake == null) {
            return;
        }

        GameVector[] directions = GameVector.values();
        GameVector currentDirection = snake.getDirection();
        GameVector newDirection;
        do {
            newDirection = directions[random.nextInt(directions.length)];
        } while (currentDirection.isCollinear(newDirection));
        snake.setDirection(newDirection);
    }
}
