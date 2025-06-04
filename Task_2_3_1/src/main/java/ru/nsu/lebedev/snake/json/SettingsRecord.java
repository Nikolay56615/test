package ru.nsu.lebedev.snake.json;

/**
 * Settings record class.
 *
 * @param fieldWidth  field's width
 * @param fieldHeight field's height
 * @param applesCount apples count on the field
 */
public record SettingsRecord(
    int fieldWidth,
    int fieldHeight,
    int applesCount
) implements JsonSerializable {

}
