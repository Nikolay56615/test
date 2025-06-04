package ru.nsu.lebedev.snake.scenes;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.nsu.lebedev.snake.AppEntryPoint;

/**
 * Scene manager class. Used for easily switching between scenes in the application.
 * Handles loading and switching scenes, and binds controllers to the scenes.
 */
public class ScenesManager {

    private static final int DEFAULT_WIDTH = 640;
    private static final int DEFAULT_HEIGHT = 700;
    private final Stage primaryStage;
    private int currentWidth = DEFAULT_WIDTH;
    private int currentHeight = DEFAULT_HEIGHT;

    /**
     * Constructor.
     *
     * @param primaryStage the stage on which the scenes will be displayed.
     */
    public ScenesManager(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setMinWidth(640);
        primaryStage.setMinHeight(700);
    }

    /**
     * Closes the primary stage.
     */
    public void close() {
        primaryStage.close();
    }

    /**
     * Loads a scene from the specified FXML file and binds the controller. Sets up the scene with
     * the given FXML, assigns the appropriate controller, and binds it with the scene manager.
     *
     * @param fxmlPath the path to the FXML file.
     * @throws IOException if there is an issue with loading the FXML file.
     */
    private void loadScene(String fxmlPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(
            AppEntryPoint.class.getResource(fxmlPath)
        );

        Scene scene = new Scene(loader.load(), currentWidth, currentHeight);
        scene.getStylesheets().add("style.css");
        primaryStage.setScene(scene);
        primaryStage.show();

        ScenesControllerContract controller = loader.getController();
        controller.setSceneManager(this);
    }

    /**
     * Changes the current scene to the specified one. Switches to the specified scene by loading
     * the corresponding FXML file.
     *
     * @param sceneEnum the enum value representing the scene to switch to.
     */
    public void changeScene(SceneEnum sceneEnum) {
        try {
            switch (sceneEnum) {
                case MENU:
                    loadScene("game-menu-scene-view.fxml");
                    break;
                case SETTINGS:
                    loadScene("game-settings-scene-view.fxml");
                    break;
                case GAME:
                    loadScene("game-scene-view.fxml");
                    break;
                case GAME_OVER:
                    loadScene("game-over-scene-view.fxml");
                    break;
                case GAME_WIN:
                    loadScene("game-win-scene-view.fxml");
                    break;
                default:
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
            close();
        }
    }

    /**
     * Returns the current scene displayed in the primary stage.
     *
     * @return the current scene.
     */
    public Scene getCurrentScene() {
        return primaryStage.getScene();
    }
}
