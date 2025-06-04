package ru.nsu.lebedev.snake.controllers;

import javafx.fxml.FXML;
import ru.nsu.lebedev.snake.scenes.SceneEnum;
import ru.nsu.lebedev.snake.scenes.ScenesControllerContract;
import ru.nsu.lebedev.snake.scenes.ScenesManager;

/**
 * Controller for the Menu scene.
 * This class handles user actions on the Menu scene such as starting the game, opening the
 * settings, and exiting the application.
 */
public class MenuController implements ScenesControllerContract {

    private ScenesManager sceneManager;

    /**
     * Sets the ScenesManager for this Controller.
     *
     * @param sceneManager the ScenesManager instance to manage scene transitions.
     */
    @Override
    public void setSceneManager(ScenesManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    /**
     * Handles the start button action. Changes the current scene to the GAME scene.
     */
    @FXML
    protected void start() {
        sceneManager.changeScene(SceneEnum.GAME);
    }

    /**
     * Handles the settings button action. Changes the current scene to the SETTINGS scene.
     */
    @FXML
    protected void settings() {
        sceneManager.changeScene(SceneEnum.SETTINGS);
    }

    /**
     * Handles the exit button action. Closes the application.
     */
    @FXML
    protected void exit() {
        sceneManager.close();
    }
}

