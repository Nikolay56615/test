package ru.nsu.lebedev.snake.models;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import ru.nsu.lebedev.snake.ai.AiSnakeContract;
import ru.nsu.lebedev.snake.ai.AppleChaserAiSnake;
import ru.nsu.lebedev.snake.ai.RandomAiSnake;
import ru.nsu.lebedev.snake.game.GameAppleList;
import ru.nsu.lebedev.snake.game.GamePoint;
import ru.nsu.lebedev.snake.game.GameSnake;
import ru.nsu.lebedev.snake.game.GameVector;

/**
 * Represents the game model, managing the game state, including multiple snakes, apples, and field.
 */
public class ModelGame implements ModelContract {

    private int currentFieldWidth;
    private int currentFieldHeight;
    private static final int PRIZE = 5;
    private final List<GamePoint> fieldPoints = new ArrayList<>();
    private static final int INITIAL_SNAKE_SIZE = 1;
    private List<GameSnake> snakes; // All snakes (player + AI)
    private List<AiSnakeContract> aiControllers; // AI controllers for AI snakes
    private GameAppleList apples;
    private final IntegerProperty score = new SimpleIntegerProperty(0);

    /**
     * Restarts the game model by resetting the field dimensions, constructing the field points,
     * creating snakes, and adding apples.
     *
     * @return the current instance of the ModelGame with the restarted state.
     */
    @Override
    public ModelGame restartModel() {
        var settingsModel = (ModelSettings) ModelEnum.SETTINGS.get();
        currentFieldWidth = settingsModel.getFieldWidth();
        currentFieldHeight = settingsModel.getFieldHeight();
        constructFieldPoints();

        snakes = new ArrayList<>();
        aiControllers = new ArrayList<>();

        apples = new GameAppleList(this);
        apples.addRandomApples(settingsModel.getApplesCount());

        GameSnake playerSnake = new GameSnake(
            INITIAL_SNAKE_SIZE,
            new GamePoint(currentFieldWidth / 2, currentFieldHeight / 2),
            GameVector.RIGHT, this
        );
        snakes.add(playerSnake);

        Random random = new Random();
        List<GamePoint> freeCells = getFreeFieldCells();
        int aiSnakeCount = Math.min(2, freeCells.size());
        for (int i = 0; i < aiSnakeCount; i++) {
            if (freeCells.isEmpty()) {
                break;
            }
            int index = random.nextInt(freeCells.size());
            GamePoint startPoint = freeCells.get(index);
            freeCells.remove(index);
            GameSnake aiSnake = new GameSnake(
                INITIAL_SNAKE_SIZE,
                startPoint,
                GameVector.values()[random.nextInt(GameVector.values().length)],
                this
            );
            snakes.add(aiSnake);

            AiSnakeContract ai = (i % 2 == 0) ? new RandomAiSnake() : new AppleChaserAiSnake();
            aiControllers.add(ai);
        }

        score.set(playerSnake.getSize());

        return this;
    }

    /**
     * Initializes the field points by creating a list of all possible points in the game field.
     */
    private void constructFieldPoints() {
        fieldPoints.clear();
        for (int x = 0; x < currentFieldWidth; ++x) {
            for (int y = 0; y < currentFieldHeight; ++y) {
                fieldPoints.add(new GamePoint(x, y));
            }
        }
    }

    /**
     * Returns a copy of the list of all field points. This method provides a duplicate of the
     * current field points list, ensuring that changes to the copy do not affect the original
     * list.
     *
     * @return A list containing copies of all field points.
     */
    private List<GamePoint> getFieldPointsCopy() {
        List<GamePoint> copy = new ArrayList<>();
        for (var point : fieldPoints) {
            copy.add(point.copy());
        }
        return copy;
    }

    /**
     * Retrieves a list of non-lethal points for a specific snake.
     *
     * @param snake The snake to check against.
     * @return A list of points that are safe for the snake to move into.
     */
    public List<GamePoint> getNonKillingCells(GameSnake snake) {
        List<GamePoint> fieldCopy = getFieldPointsCopy();
        for (GameSnake otherSnake : snakes) {
            if (otherSnake != snake) {
                fieldCopy.removeAll(otherSnake.getBody());
            }
        }
        fieldCopy.removeAll(snake.getBody());
        return fieldCopy;
    }

    /**
     * Retrieves a list of free cells on the field where objects can be placed.
     *
     * @return A list of available cells.
     */
    public List<GamePoint> getFreeFieldCells() {
        List<GamePoint> fieldCopy = getFieldPointsCopy();
        for (GameSnake snake : snakes) {
            fieldCopy.removeAll(snake.getWholeBody());
        }
        fieldCopy.removeAll(apples.getApples());
        return fieldCopy;
    }

    /**
     * Retrieves the current score (player snake's size).
     *
     * @return The current score of the game.
     */
    public int getScore() {
        return score.get();
    }

    /**
     * Returns the score as an IntegerProperty for binding purposes.
     *
     * @return The score property.
     */
    public IntegerProperty scoreProperty() {
        return score;
    }

    /**
     * Handles collisions for a given snake.
     *
     * @param snakes The list of all snakes.
     * @return A list of snakes to be removed due to collisions.
     */
    private List<GameSnake> handleCollisions(List<GameSnake> snakes) {
        List<GameSnake> snakesToRemove = new ArrayList<>();
        for (int i = 0; i < snakes.size(); i++) {
            GameSnake snake = snakes.get(i);
            if (snake.isDead()) {
                snakesToRemove.add(snake);
                continue;
            }
            GamePoint head = snake.getHead();

            for (int j = i + 1; j < snakes.size(); j++) {
                GameSnake otherSnake = snakes.get(j);
                if (head.equals(otherSnake.getHead())) {
                    if (snake == snakes.get(0)) {
                        snakesToRemove.add(otherSnake);
                        score.set(score.get() + PRIZE);
                    } else if (otherSnake == snakes.get(0)) {
                        snakesToRemove.add(snake);
                        score.set(score.get() + PRIZE);
                    } else {
                        snakesToRemove.add(snake);
                        snakesToRemove.add(otherSnake);
                    }
                }
            }

            for (GameSnake otherSnake : snakes) {
                if (otherSnake != snake && head.isInList(otherSnake.getBody())) {
                    if (otherSnake == snakes.get(0) && snake != snakes.get(0)) {
                        snakesToRemove.add(snake);
                        score.set(score.get() + PRIZE);
                    } else if (snake == snakes.get(0) && otherSnake != snakes.get(0)) {
                        snakesToRemove.add(snake);
                    } else {
                        snakesToRemove.add(snake);
                    }
                }
            }
        }

        return snakesToRemove;
    }

    /**
     * Updates the game state for all snakes.
     *
     * @return List of dead snakes.
     */
    public List<GameSnake> update() {
        for (int i = 0; i < aiControllers.size(); i++) {
            aiControllers.get(i).updateDirection(this);
        }

        for (GameSnake snake : snakes) {
            snake.move();
        }

        List<GameSnake> snakesToRemove = handleCollisions(snakes);

        for (GameSnake deadSnake : snakesToRemove) {
            snakes.remove(deadSnake);
            int aiIndex = snakes.indexOf(deadSnake) - 1; // -1 because player snake has no AI
            if (aiIndex >= 0 && aiIndex < aiControllers.size()) {
                aiControllers.remove(aiIndex);
            }
        }

        for (GameSnake snake : snakes) {
            if (apples.checkSnakeGrowth(snake)) {
                apples.addRandomApple();
            }
            if (snake == snakes.get(0)) {
                score.set(snake.getSize());
            }
        }

        return snakesToRemove;
    }

    /**
     * Checks whether the game is over (player snake is dead).
     *
     * @return True if the game is over, otherwise false.
     */
    public boolean isGameOver() {
        return snakes.isEmpty() || snakes.get(0).isDead();
    }

    /**
     * Checks if any snake has won by filling the field.
     *
     * @return True if a snake has won.
     */
    public boolean isGameWon() {
        for (GameSnake snake : snakes) {
            if (snake.getSize() == currentFieldWidth * currentFieldHeight) {
                return true;
            }
        }
        return false;
    }

    /**
     * Retrieves the current width of the game field.
     *
     * @return The current width of the field.
     */
    public int getCurrentFieldWidth() {
        return currentFieldWidth;
    }

    /**
     * Retrieves the current height of the game field.
     *
     * @return The current height of the field.
     */
    public int getCurrentFieldHeight() {
        return currentFieldHeight;
    }

    /**
     * Retrieves all snakes in the game.
     *
     * @return The list of snakes.
     */
    public List<GameSnake> getSnakes() {
        return snakes;
    }

    /**
     * Retrieves the player snake.
     *
     * @return The player snake.
     */
    public GameSnake getPlayerSnake() {
        return snakes.isEmpty() ? null : snakes.get(0);
    }

    /**
     * Retrieves the list of apples currently on the field.
     *
     * @return The apples object containing all apples on the field.
     */
    public LinkedList<GamePoint> getApples() {
        return apples.getApples();
    }
}
