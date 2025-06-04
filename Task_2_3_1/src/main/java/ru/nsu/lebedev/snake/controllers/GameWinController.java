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
 * Controller for the Game Win scene.
 */
public class GameWinController implements ScenesControllerContract {

    private ScenesManager sceneManager;

    @FXML
    private Label winMessage;
    @FXML
    private Label score;


    /**
     * Sets the ScenesManager, updates the score label with the current game score and win message.
     *
     * @param sceneManager the ScenesManager instance
     */
    @Override
    public void setSceneManager(ScenesManager sceneManager) {
        this.sceneManager = sceneManager;
        ModelGame gameModel = (ModelGame) ModelEnum.GAME.get();
        score.textProperty().bind(Bindings.convert(gameModel.scoreProperty()));
        winMessage.setText("Congratulation! You win the game!");
    }

    /**
     * Handler for the back button. Switches the scene back to the MENU scene.
     */
    @FXML
    protected void backToMenu() {
        ModelGame gameModel = (ModelGame) ModelEnum.GAME.get();
        gameModel.restartModel();
        sceneManager.changeScene(SceneEnum.MENU);
    }
}