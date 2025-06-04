package ru.nsu.lebedev.snake;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.nsu.lebedev.snake.game.GamePoint;
import ru.nsu.lebedev.snake.models.ModelEnum;
import ru.nsu.lebedev.snake.models.ModelGame;
import ru.nsu.lebedev.snake.models.ModelSettings;

/**
 * Tests for complex usage of all model.
 */
public class ApiTest {

    @Test
    void testGameProcessWithChangedSettings() {
        ModelSettings settingsModel = ModelEnum.SETTINGS.get().restartModel();
        settingsModel.setSettingsPath("testSettings.json");
        settingsModel = ModelEnum.SETTINGS.get().restartModel();

        settingsModel.setFieldWidth(5);
        settingsModel.setFieldHeight(5);
        settingsModel.setApplesCount(1);

        ModelGame gameModel = ModelEnum.GAME.get().restartModel();
        Assertions.assertEquals(1, gameModel.getApples().size());

        GamePoint startPoint = gameModel.getPlayerSnake().getHead().copy();
        for (int i = 0; i < 5; ++i) {
            gameModel.update();
        }
        Assertions.assertEquals(startPoint, gameModel.getPlayerSnake().getHead());
    }
}
