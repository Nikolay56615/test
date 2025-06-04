package ru.nsu.lebedev.snake.game;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import ru.nsu.lebedev.snake.models.ModelGame;

/**
 * Manages the list of apples in the game.
 */
public class GameAppleList {

    private final LinkedList<GamePoint> apples = new LinkedList<>();
    private final ModelGame gameModel;

    /**
     * Constructs an apple list manager.
     *
     * @param gameModel Reference to the game model.
     */
    public GameAppleList(ModelGame gameModel) {
        this.gameModel = gameModel;
    }

    /**
     * Gets the list of apples.
     *
     * @return A linked list of apple positions.
     */
    public LinkedList<GamePoint> getApples() {
        return apples;
    }

    /**
     * Checks if the snake eats an apple, growing it if necessary.
     *
     * @param snake The snake to check.
     * @return True if the snake ate an apple, false otherwise.
     */
    public boolean checkSnakeGrowth(GameSnake snake) {
        var collision = snake.getHead().getListCollision(apples);
        if (collision.isEmpty()) {
            return false;
        }
        collision.ifPresent(index -> {
            snake.grow();
            apples.remove((int) index);
        });
        return true;
    }

    /**
     * Adds a specified number of apples at random locations.
     *
     * @param count Number of apples to add.
     */
    public void addRandomApples(int count) {
        for (int i = 0; i < count; ++i) {
            addRandomApple();
        }
    }

    /**
     * Adds a single apple at a random location.
     */
    public void addRandomApple() {
        Random random = new Random();
        List<GamePoint> freeCells = gameModel.getFreeFieldCells();
        if (!freeCells.isEmpty()) {
            int index = random.nextInt(freeCells.size());
            GamePoint apple = freeCells.get(index).copy();
            apples.add(apple);
        }
    }
}
