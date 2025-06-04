package ru.nsu.lebedev.snake.game;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.nsu.lebedev.snake.models.ModelEnum;
import ru.nsu.lebedev.snake.models.ModelGame;

/**
 * Tests for GameModel model fragment. Contains almost whole methods of internal objects
 */
public class GameTest {

    @Test
    void testValidSnakeDirectionChanges() {
        ModelGame gameModel = ModelEnum.GAME.get().restartModel();
        gameModel.getPlayerSnake().setDirection(GameVector.RIGHT);
        Assertions.assertEquals(GameVector.RIGHT, gameModel.getPlayerSnake().getDirection());
        gameModel.getPlayerSnake().setDirection(GameVector.UP);
        Assertions.assertEquals(GameVector.UP, gameModel.getPlayerSnake().getDirection());
        gameModel.getPlayerSnake().setDirection(GameVector.LEFT);
        Assertions.assertEquals(GameVector.LEFT, gameModel.getPlayerSnake().getDirection());
        gameModel.getPlayerSnake().setDirection(GameVector.DOWN);
        Assertions.assertEquals(GameVector.DOWN, gameModel.getPlayerSnake().getDirection());
    }

    @Test
    void testInvalidSnakeDirectionChanges() {
        ModelGame gameModel = ModelEnum.GAME.get().restartModel();

        gameModel.getPlayerSnake().setDirection(GameVector.RIGHT);
        gameModel.getPlayerSnake().setDirection(GameVector.LEFT);
        Assertions.assertEquals(GameVector.RIGHT, gameModel.getPlayerSnake().getDirection());

        gameModel.getPlayerSnake().setDirection(GameVector.UP);
        gameModel.getPlayerSnake().setDirection(GameVector.DOWN);
        Assertions.assertEquals(GameVector.UP, gameModel.getPlayerSnake().getDirection());

        gameModel.getPlayerSnake().setDirection(GameVector.LEFT);
        gameModel.getPlayerSnake().setDirection(GameVector.RIGHT);
        Assertions.assertEquals(GameVector.LEFT, gameModel.getPlayerSnake().getDirection());

        gameModel.getPlayerSnake().setDirection(GameVector.DOWN);
        gameModel.getPlayerSnake().setDirection(GameVector.UP);
        Assertions.assertEquals(GameVector.DOWN, gameModel.getPlayerSnake().getDirection());
    }

    @Test
    void testSnakeGrowth() {
        ModelGame game = ModelEnum.GAME.get().restartModel();
        GamePoint applePosition = game.getPlayerSnake().getHead().copy();
        applePosition.move(game.getPlayerSnake().getDirection());
        game.getApples().add(applePosition);

        Assertions.assertEquals(1, game.getScore());
        Assertions.assertEquals(game.getPlayerSnake().getHead(), game.getPlayerSnake().getTail());

        game.update();
        Assertions.assertEquals(2, game.getScore());
        Assertions.assertEquals(game.getPlayerSnake().getBody().get(0),
            game.getPlayerSnake().getTail());

        applePosition = game.getPlayerSnake().getHead().copy();
        applePosition.move(game.getPlayerSnake().getDirection());
        game.getApples().add(applePosition);

        game.update();
        Assertions.assertEquals(3, game.getScore());
        Assertions.assertNotEquals(game.getPlayerSnake().getBody().get(0),
            game.getPlayerSnake().getHead());
        Assertions.assertNotEquals(game.getPlayerSnake().getBody().get(0),
            game.getPlayerSnake().getTail());
    }

    @Test
    void testGameOverCondition() {
        ModelGame gameModel = ModelEnum.GAME.get().restartModel();
        gameModel.getPlayerSnake().setDirection(GameVector.RIGHT);
        for (int i = 0; i < 5; ++i) {
            gameModel.getPlayerSnake().move();
            gameModel.getPlayerSnake().grow();
        }
        gameModel.getPlayerSnake().setDirection(GameVector.UP);
        gameModel.getPlayerSnake().move();
        gameModel.getPlayerSnake().setDirection(GameVector.LEFT);
        gameModel.getPlayerSnake().move();
        gameModel.getPlayerSnake().setDirection(GameVector.DOWN);
        gameModel.getPlayerSnake().move();
        Assertions.assertTrue(gameModel.isGameOver());
    }
}
