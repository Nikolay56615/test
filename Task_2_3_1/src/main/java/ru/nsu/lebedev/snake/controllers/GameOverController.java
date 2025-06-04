package ru.nsu.lebedev.snake.controllers;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import ru.nsu.lebedev.snake.models.ModelEnum;
import ru.nsu.lebedev.snake.models.ModelGame;
import ru.nsu.lebedev.snake.scenes.SceneEnum;
import ru.nsu.lebedev.snake.scenes.ScenesControllerContract;
import ru.nsu.lebedev.snake.scenes.ScenesManager;

/**
 * Controller for the Game Over scene.
 */
public class GameOverController implements ScenesControllerContract {

    private ScenesManager sceneManager;

    @FXML
    private Label score;

    /**
     * Sets the ScenesManager and updates the score label with the current game score.
     *
     * @param sceneManager the ScenesManager instance
     */
    @Override
    public void setSceneManager(ScenesManager sceneManager) {
        this.sceneManager = sceneManager;
        ModelGame gameModel = (ModelGame) ModelEnum.GAME.get();
        score.textProperty().bind(Bindings.convert(gameModel.scoreProperty()));
    }

    /**
     * Handler for the back button. Switches the scene back to the MENU scene.
     */
    @FXML
    protected void back() {
        ModelGame gameModel = (ModelGame) ModelEnum.GAME.get();
        gameModel.restartModel();
        sceneManager.changeScene(SceneEnum.MENU);
    }
}

