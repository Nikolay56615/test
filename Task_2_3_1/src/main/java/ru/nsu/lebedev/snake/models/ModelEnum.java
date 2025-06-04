package ru.nsu.lebedev.snake.models;

/**
 * Enum class for singletones Models.
 */
public enum ModelEnum {
    SETTINGS(new ModelSettings()),
    GAME(new ModelGame());

    private final ModelContract modelFragment;

    /**
     * Constructor for the enum.
     * Initializes the enum constant with the provided model fragment instance.
     *
     * @param modelFragment The model fragment to be associated with this enum constant.
     */
    ModelEnum(ModelContract modelFragment) {
        this.modelFragment = modelFragment;
    }

    /**
     * Retrieves the model fragment instance associated with the enum constant.
     *
     * @return The singleton instance of the associated model fragment.
     */
    public ModelContract get() {
        return modelFragment;
    }
}
