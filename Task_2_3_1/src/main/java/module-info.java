module ru.nsu.lebedev.snake {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;

    opens ru.nsu.lebedev.snake to javafx.fxml;
    exports ru.nsu.lebedev.snake;
    exports ru.nsu.lebedev.snake.scenes;
    opens ru.nsu.lebedev.snake.scenes to javafx.fxml;
    exports ru.nsu.lebedev.snake.game;
    opens ru.nsu.lebedev.snake.game to javafx.fxml;

    exports ru.nsu.lebedev.snake.controllers to com.fasterxml.jackson.databind;
    opens ru.nsu.lebedev.snake.controllers to javafx.fxml;

    opens  ru.nsu.lebedev.snake.json to com.fasterxml.jackson.databind;
}