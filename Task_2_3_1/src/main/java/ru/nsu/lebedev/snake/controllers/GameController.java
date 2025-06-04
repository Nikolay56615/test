package ru.nsu.lebedev.snake.controllers;

import java.util.List;
import javafx.animation.AnimationTimer;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import ru.nsu.lebedev.snake.game.GamePoint;
import ru.nsu.lebedev.snake.game.GameSnake;
import ru.nsu.lebedev.snake.game.GameVector;
import ru.nsu.lebedev.snake.models.ModelEnum;
import ru.nsu.lebedev.snake.models.ModelGame;
import ru.nsu.lebedev.snake.scenes.GameView;
import ru.nsu.lebedev.snake.scenes.SceneEnum;
import ru.nsu.lebedev.snake.scenes.ScenesControllerContract;
import ru.nsu.lebedev.snake.scenes.ScenesManager;

/**
 * Controller for the Game scene.
 * Manages the game loop, handles key input for the player snake, and updates the game view.
 */
public class GameController implements ScenesControllerContract {

    private ScenesManager sceneManager;

    @FXML
    private Label scores;

    @FXML
    private GridPane fieldGrid;

    private boolean updatedAfterKeyPressed = false;
    private ModelGame gameModel;
    private GameView gameView;

    /**
     * Animation timer that updates the game state at a fixed interval.
     */
    private final AnimationTimer animationTimer = new AnimationTimer() {
        public static final long UPDATE_MS = 50;
        private long lastUpdateTimestamp = 0;

        @Override
        public void handle(long nanoSecTimestamp) {
            long msTimestamp = nanoSecTimestamp / 1_000_000;
            if (msTimestamp - lastUpdateTimestamp < UPDATE_MS) {
                return;
            }
            updateSnakeCells();
            lastUpdateTimestamp = msTimestamp;
        }
    };

    /**
     * Sets the ScenesManager, initializes the game model and view, and starts the animation timer.
     *
     * @param sceneManager the ScenesManager instance to manage scene transitions.
     */
    @Override
    public void setSceneManager(ScenesManager sceneManager) {
        this.sceneManager = sceneManager;
        this.gameModel = ModelEnum.GAME.get().restartModel();
        this.gameView = new GameView(
            fieldGrid,
            gameModel.getCurrentFieldHeight(),
            gameModel.getCurrentFieldWidth()
        );

        scores.textProperty().bind(Bindings.convert(gameModel.scoreProperty()));

        sceneManager.getCurrentScene().setOnKeyPressed(this::keyHandler);
        sceneManager.getCurrentScene().widthProperty();
        animationTimer.start();
    }

    /**
     * Handles key events for controlling the player snake.
     *
     * @param event the key event.
     */
    private void keyHandler(KeyEvent event) {
        if (!updatedAfterKeyPressed) {
            return;
        }
        GameVector direction = switch (event.getCode()) {
            case UP -> GameVector.UP;
            case RIGHT -> GameVector.RIGHT;
            case DOWN -> GameVector.DOWN;
            case LEFT -> GameVector.LEFT;
            default -> null;
        };

        if (direction != null) {
            gameModel.getPlayerSnake().setDirection(direction);
            updatedAfterKeyPressed = false;
        }
    }

    /**
     * Updates the game state: repaints cells, updates the model, and checks for game over or win.
     */
    private void updateSnakeCells() {
        for (GameSnake snake : gameModel.getSnakes()) {
            GamePoint tail = snake.getTail();
            gameView.setCellColor(tail, GameView.CellColor.FIELD);
        }

        List<GameSnake> deadSnakes = gameModel.update();
        if (gameModel.isGameOver()) {
            gameOver();
            return;
        }
        if (gameModel.isGameWon()) {
            win();
            return;
        }

        for (GameSnake deadSnake : deadSnakes) {
            for (GamePoint point : deadSnake.getWholeBody()) {
                gameView.setCellColor(point, GameView.CellColor.FIELD);
            }
        }

        for (int i = 0; i < gameModel.getSnakes().size(); i++) {
            GameSnake snake = gameModel.getSnakes().get(i);
            GameView.CellColor snakeColor = (i == 0) ? GameView.CellColor.SNAKE :
                (i == 1) ? GameView.CellColor.AI_SNAKE_1 : GameView.CellColor.AI_SNAKE_2;
            GameView.CellColor headColor = (i == 0) ? GameView.CellColor.SNAKE_HEAD :
                (i == 1) ? GameView.CellColor.AI_SNAKE_HEAD_1 : GameView.CellColor.AI_SNAKE_HEAD_2;
            for (GamePoint point : snake.getBody()) {
                gameView.setCellColor(point, snakeColor);
            }
            gameView.setCellColor(snake.getHead(), headColor);
        }

        for (GamePoint apple : gameModel.getApples()) {
            gameView.setCellColor(apple, GameView.CellColor.APPLE);
        }

        updatedAfterKeyPressed = true;
    }

    /**
     * Ends the game by stopping the animation timer and switching to the Game Over scene.
     */
    public void gameOver() {
        animationTimer.stop();
        sceneManager.changeScene(SceneEnum.GAME_OVER);
    }

    /**
     * Ends the game by stopping the animation timer and switching to the Game Win scene.
     */
    private void win() {
        animationTimer.stop();
        sceneManager.changeScene(SceneEnum.GAME_WIN);
    }
}
