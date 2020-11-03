package snake;

import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

/**
 * Représente un plateau de jeu
 */
public class Board extends Scene {
    Snake serpent;

    public Board(Parent root, int w, int h, Color c) {
        super(root, w, h, c);
        setSnakeListener();
        serpent = Game.getSnake();
    }

    public void setSnakeListener() {
        this.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {

                Snake.Direction snakeDirection = serpent.getSnakeDirection();

                if ((event.getCode() == KeyCode.LEFT) && (snakeDirection != Snake.Direction.RIGHT) && (snakeDirection != Snake.Direction.LEFT)) {
                    System.out.println("left");
                    serpent.setSnakeDirection(Snake.Direction.LEFT);
                }

                if ((event.getCode() == KeyCode.RIGHT) && (snakeDirection != Snake.Direction.LEFT) && (snakeDirection != Snake.Direction.RIGHT)) {
                    System.out.println("right");
                    serpent.setSnakeDirection(Snake.Direction.RIGHT);
                }

                if ((event.getCode() == KeyCode.UP) && (snakeDirection != Snake.Direction.DOWN) && (snakeDirection != Snake.Direction.UP)) {
                    System.out.println("up");
                    serpent.setSnakeDirection(Snake.Direction.UP);
                }

                if ((event.getCode() == KeyCode.DOWN) && (snakeDirection != Snake.Direction.UP) && (snakeDirection != Snake.Direction.DOWN)) {
                    System.out.println("down");
                    serpent.setSnakeDirection(Snake.Direction.DOWN);
                }
            }
        });
    }
}
