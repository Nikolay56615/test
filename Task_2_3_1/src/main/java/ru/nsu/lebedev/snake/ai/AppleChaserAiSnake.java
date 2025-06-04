package ru.nsu.lebedev.snake.ai;

import java.util.Comparator;
import ru.nsu.lebedev.snake.game.GamePoint;
import ru.nsu.lebedev.snake.game.GameSnake;
import ru.nsu.lebedev.snake.game.GameVector;
import ru.nsu.lebedev.snake.models.ModelGame;

/**
 * AI snake that chases the nearest apple.
 */
public class AppleChaserAiSnake implements AiSnakeContract {

    @Override
    public void updateDirection(ModelGame model) {
        GameSnake snake = model.getSnakes().stream()
            .filter(s -> !s.equals(model.getPlayerSnake()))
            .skip(1) // Skip the first AI snake (RandomAiSnake)
            .findFirst()
            .orElse(null);
        if (snake == null) {
            return;
        }

        GamePoint head = snake.getHead();
        GamePoint nearestApple = model.getApples().stream()
            .min(Comparator.comparingInt(p ->
                Math.abs(p.getX1() - head.getX1()) + Math.abs(p.getY1() - head.getY1())))
            .orElse(null);
        if (nearestApple == null) {
            return;
        }

        int dx = nearestApple.getX1() - head.getX1();
        int dy = nearestApple.getY1() - head.getY1();
        GameVector currentDirection = snake.getDirection();
        GameVector newDirection = null;

        if (Math.abs(dx) > Math.abs(dy)) {
            if (dx > 0 && !currentDirection.isCollinear(GameVector.RIGHT)) {
                newDirection = GameVector.RIGHT;
            } else if (dx < 0 && !currentDirection.isCollinear(GameVector.LEFT)) {
                newDirection = GameVector.LEFT;
            }
        } else {
            if (dy > 0 && !currentDirection.isCollinear(GameVector.DOWN)) {
                newDirection = GameVector.DOWN;
            } else if (dy < 0 && !currentDirection.isCollinear(GameVector.UP)) {
                newDirection = GameVector.UP;
            }
        }

        if (newDirection != null) {
            snake.setDirection(newDirection);
        }
    }
}