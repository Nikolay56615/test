package ru.nsu.lebedev.snake.controllers;

import java.util.HashMap;
import java.util.Map;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.Pair;
import javafx.util.converter.NumberStringConverter;
import ru.nsu.lebedev.snake.models.ModelEnum;
import ru.nsu.lebedev.snake.models.ModelSettings;
import ru.nsu.lebedev.snake.scenes.SceneEnum;
import ru.nsu.lebedev.snake.scenes.ScenesControllerContract;
import ru.nsu.lebedev.snake.scenes.ScenesManager;

/**
 * Controller for the settings scene.
 * Manages user input for game settings and performs validation.
 */
public class SettingsController implements ScenesControllerContract {

    private ScenesManager sceneManager;
    private ModelSettings settingsModel;

    @FXML
    private TextField widthField;
    private IntegerProperty widthProperty;
    @FXML
    private Label widthError;

    @FXML
    private TextField heightField;
    private IntegerProperty heightProperty;
    @FXML
    private Label heightError;

    @FXML
    private TextField applesCountField;
    private IntegerProperty applesCountProperty;
    @FXML
    private Label applesCountError;

    private final Map<TextField, Pair<Label, String>> fieldErrorMap = new HashMap<>();

    @Override
    public void setSceneManager(ScenesManager sceneManager) {
        this.sceneManager = sceneManager;

        settingsModel = ModelEnum.SETTINGS.get().restartModel();
        widthProperty = new SimpleIntegerProperty(settingsModel.getFieldWidth());
        heightProperty = new SimpleIntegerProperty(settingsModel.getFieldHeight());
        applesCountProperty = new SimpleIntegerProperty(settingsModel.getApplesCount());

        bindTextField(widthField, widthProperty);
        bindTextField(heightField, heightProperty);
        bindTextField(applesCountField, applesCountProperty);

        fieldErrorMap.put(widthField,
            new Pair<>(widthError, "Width should be a positive integer number"));
        fieldErrorMap.put(heightField,
            new Pair<>(heightError, "Height should be a positive integer number"));
        fieldErrorMap.put(applesCountField,
            new Pair<>(applesCountError, "Apples count should be a positive integer number"));

        addValidationListener(widthField);
        addValidationListener(heightField);
        addValidationListener(applesCountField);
    }

    /**
     * Binds a text field to an integer property using a NumberStringConverter.
     *
     * @param field    the text field to bind
     * @param property the integer property
     */
    private void bindTextField(TextField field, IntegerProperty property) {
        field.textProperty().bindBidirectional(property, new NumberStringConverter());
    }

    /**
     * Adds a validation listener to a text field.
     *
     * @param field the text field to validate
     */
    private void addValidationListener(TextField field) {
        field.textProperty().addListener(
            (observable, oldValue, newValue) -> validateTextField(field, oldValue, newValue));
    }

    /**
     * Validates the text input for a field.
     * Checks if the new value is a positive integer, shows error if invalid, and reverts to old
     * value.
     *
     * @param field    the text field being validated
     * @param oldValue the previous value
     * @param newValue the new value to validate
     */
    private void validateTextField(TextField field, String oldValue, String newValue) {
        Pair<Label, String> errorPair = fieldErrorMap.get(field);
        if (errorPair != null) {
            Label errorLabel = errorPair.getKey();
            String errorMessage = errorPair.getValue();
            if (isValidPositiveInteger(newValue)) {
                errorLabel.setText("");
            } else {
                errorLabel.setText(errorMessage);
                field.setText(oldValue);
            }
        }
    }

    /**
     * Checks if the given text is a valid positive integer.
     * Returns false if the text is empty, not a number, or less than 1.
     *
     * @param text the text to validate
     * @return true if the text is a valid positive integer
     */
    private boolean isValidPositiveInteger(String text) {
        if (text.isEmpty()) {
            return false;
        }
        try {
            int value = Integer.parseInt(text);
            return value >= 1;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @FXML
    protected void save() {
        settingsModel.setFieldWidth(widthProperty.get());
        settingsModel.setFieldHeight(heightProperty.get());
        settingsModel.setApplesCount(applesCountProperty.get());
        settingsModel.saveSettingsToJson();
        sceneManager.changeScene(SceneEnum.MENU);
    }

    @FXML
    protected void discard() {
        sceneManager.changeScene(SceneEnum.MENU);
    }
}
