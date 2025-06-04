package ru.nsu.lebedev.snake.models;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import ru.nsu.lebedev.snake.json.Json;
import ru.nsu.lebedev.snake.json.ParsingException;
import ru.nsu.lebedev.snake.json.SettingsRecord;

/**
 * The ModelSettings class is responsible for managing game settings.
 * It loads settings from a JSON file and can save updated settings back to the file.
 */
public class ModelSettings implements ModelContract {

    private int fieldWidth;
    private int fieldHeight;
    private int applesCount;

    private String settingsPath = "settings.json";

    /**
     * Constructor. Loads settings from the JSON file during object creation.
     */
    public ModelSettings() {
        try {
            loadSettingsFromJson();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Resets the model by reloading the settings from the JSON file.
     *
     * @return The updated ModelSettings instance.
     */
    @Override
    public ModelSettings restartModel() {
        try {
            loadSettingsFromJson();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * Loads settings from the JSON file.
     *
     * @throws IOException if the settings file cannot be found or parsed.
     */
    private void loadSettingsFromJson() throws IOException {
        System.out.println("Loading settings...");
        InputStream inputStream;
        SettingsRecord settingsRecord = null;
        try {
            inputStream = new FileInputStream(settingsPath);
        } catch (IOException notFoundException) {
            inputStream = getClass().getClassLoader().getResourceAsStream(settingsPath);
        }
        if (inputStream == null) {
            FileNotFoundException e = new FileNotFoundException(
                "Settings JSON file not found at: " + settingsPath);
            System.err.println("Failed to load settings file");
            throw e;
        }
        try {
            settingsRecord = Json.deserialize(inputStream, SettingsRecord.class);
        } catch (ParsingException e) {
            System.err.println("Failed to parse settings");
            throw e;
        }
        inputStream.close();
        fieldWidth = settingsRecord.fieldWidth();
        fieldHeight = settingsRecord.fieldHeight();
        applesCount = settingsRecord.applesCount();
        System.out.println("Settings loaded successfully.");
    }

    /**
     * Saves the current settings to the JSON file.
     */
    public void saveSettingsToJson() {
        System.out.println("Saving settings...");
        try (FileOutputStream outputStream = new FileOutputStream(settingsPath)) {
            SettingsRecord settingsRecord = new SettingsRecord(fieldWidth, fieldHeight,
                applesCount);
            Json.serialize(settingsRecord, outputStream);
        } catch (IOException e) {
            System.err.println("Failed to save settings");
            e.printStackTrace();
        }
        System.out.println("Settings saved successfully.");
    }

    /**
     * Gets the field width.
     *
     * @return the width of the field.
     */
    public int getFieldWidth() {
        return fieldWidth;
    }

    /**
     * Sets the field width.
     *
     * @param fieldWidth the new width of the field.
     */
    public void setFieldWidth(int fieldWidth) {
        this.fieldWidth = fieldWidth;
    }

    /**
     * Gets the field height.
     *
     * @return the height of the field.
     */
    public int getFieldHeight() {
        return fieldHeight;
    }

    /**
     * Sets the field height.
     *
     * @param fieldHeight the new height of the field.
     */
    public void setFieldHeight(int fieldHeight) {
        this.fieldHeight = fieldHeight;
    }

    /**
     * Gets the number of apples.
     *
     * @return the apples count.
     */
    public int getApplesCount() {
        return applesCount;
    }

    /**
     * Sets the number of apples.
     *
     * @param applesCount the new apples count.
     */
    public void setApplesCount(int applesCount) {
        this.applesCount = applesCount;
    }

    /**
     * Gets the path to settings.
     */
    public String getSettingsPath() {
        return settingsPath;
    }

    /**
     * Sets the path to settings.
     *
     * @param settingsPath the new path to settings.
     */
    public void setSettingsPath(String settingsPath) {
        this.settingsPath = settingsPath;
    }
}
