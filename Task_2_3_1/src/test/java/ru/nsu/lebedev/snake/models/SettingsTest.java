package ru.nsu.lebedev.snake.models;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.nsu.lebedev.snake.json.Json;
import ru.nsu.lebedev.snake.json.SettingsRecord;

/**
 * Tests for SettingsModel model fragment.
 */
public class SettingsTest {

    private static final String SETTINGS_TEST_FILE = "testSettings.json";

    @Test
    void testLoadSettingsFromFile() {
        ModelSettings settingsModel = (ModelSettings) ModelEnum.SETTINGS.get();
        settingsModel.setSettingsPath(SETTINGS_TEST_FILE);
        settingsModel.restartModel();
        Assertions.assertEquals(3, settingsModel.getFieldWidth());
        Assertions.assertEquals(2, settingsModel.getFieldHeight());
        Assertions.assertEquals(1, settingsModel.getApplesCount());
    }

    @Test
    void testSaveSettingsToFile() {
        ModelSettings settingsModel = (ModelSettings) ModelEnum.SETTINGS.get();
        settingsModel.setSettingsPath(SETTINGS_TEST_FILE);
        settingsModel.restartModel();

        settingsModel.setFieldWidth(4);
        settingsModel.setFieldHeight(5);
        settingsModel.setApplesCount(6);
        settingsModel.saveSettingsToJson();

        try (
            var fileInputStream = new FileInputStream(SETTINGS_TEST_FILE)
        ) {
            SettingsRecord savedSettings = Json.deserialize(
                fileInputStream, SettingsRecord.class
            );
            Assertions.assertEquals(4, savedSettings.fieldWidth());
            Assertions.assertEquals(5, savedSettings.fieldHeight());
            Assertions.assertEquals(6, savedSettings.applesCount());
        } catch (IOException e) {
            Assertions.fail();
        }
        if (!new File(SETTINGS_TEST_FILE).delete()) {
            Assertions.fail();
        }
    }

    @Test
    void testLoadDefaultSettingsOnInvalidPath() {
        ModelSettings settingsModel = (ModelSettings) ModelEnum.SETTINGS.get();
        settingsModel.setSettingsPath(SETTINGS_TEST_FILE);
        settingsModel.restartModel();

        settingsModel.setFieldWidth(4);
        settingsModel.setFieldHeight(5);
        settingsModel.setApplesCount(6);

        settingsModel.setSettingsPath("invalid/path.json");
        settingsModel.restartModel();

        Assertions.assertEquals(4, settingsModel.getFieldWidth());
        Assertions.assertEquals(5, settingsModel.getFieldHeight());
        Assertions.assertEquals(6, settingsModel.getApplesCount());
    }
}
