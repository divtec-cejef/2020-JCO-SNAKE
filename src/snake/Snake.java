package snake;

import javax.swing.*;
import java.awt.*;
import java.awt.image.ImageObserver;

/**
 * Classe qui représente un serpent
 */
public class Snake {
    public enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT
    }

    public enum Color {
        GREEN,
        RED,
        BLUE,
        YELLOW
    }

    String pathToImages = "res/images/";

    // Vitesse du serpent
    private int snakeSpeed = 140;
    private final int MINIMAL_SNAKE_SPEED = 10;

    public boolean isAlive = true;

    // Longueur du serpent
    private int snakeLenght;
    // Couleur du serpent
    private Color snakeColor;
    // Images du serpent
    private Image bodyHorizonzal;
    private Image bodyVertical;
    private Image head;
    private Image tail;
    // Direction du serpent
    private Direction snakeDirection = Direction.RIGHT;

    public Snake(int snakeLenght, Color snakeColor) {
        this.snakeLenght = snakeLenght;
        this.snakeColor = snakeColor;

        setImages(snakeColor);
    }

    private void setImages(Color snakeColor) {
        ImageIcon imageIconBodyHorizontal = new ImageIcon(pathToImages + snakeColor.toString() + "/body_horizontal.png");
        bodyHorizonzal = imageIconBodyHorizontal.getImage();

        ImageIcon imageIconBodyVertical = new ImageIcon(pathToImages + snakeColor.toString() + "/body_vertical.png");
        bodyVertical = imageIconBodyVertical.getImage();

        ImageIcon imageIconHead = new ImageIcon(pathToImages + snakeColor.toString() + "head_right.png");
        head = imageIconHead.getImage();

        ImageIcon imageIconTail = new ImageIcon(pathToImages + snakeColor.toString() + "tail_right.png");
        tail = imageIconTail.getImage();
    }

    public void draw(Graphics g, int[] x, int[] y, ImageObserver imageObserver) {
        for (int z = 0; z < snakeLenght; z++) {
            if (z == 0) {
                g.drawImage(head, x[z], y[z], imageObserver);
            } else if (z == snakeLenght - 1) {
                g.drawImage(tail, x[z], y[z], imageObserver);
            } else {
                if(snakeDirection == Direction.LEFT || snakeDirection == Direction.RIGHT)
                    g.drawImage(bodyHorizonzal, x[z], y[z], imageObserver);
                else
                    g.drawImage(bodyVertical, x[z], y[z], imageObserver);
            }
        }
    }

    public void move(int tileSize, int[] x, int[] y) {
        for (int z = snakeLenght; z > 0; z--) {
            x[z] = x[(z - 1)];
            y[z] = y[(z - 1)];
        }

        // Déplace la tête du serpent vers la gauche
        if (snakeDirection == Direction.LEFT) {
            x[0] -= tileSize;
        }

        // Déplace la tête du serpent vers la droite
        if (snakeDirection == Direction.RIGHT) {
            x[0] += tileSize;
        }

        // Déplace la tête du serpent vers le haut
        if (snakeDirection == Direction.UP) {
            y[0] -= tileSize;
        }

        // Déplace la tête du serpent vers le bas
        if (snakeDirection == Direction.DOWN) {
            y[0] += tileSize;
        }
    }

    public void ateApple() {
        snakeLenght++;
        if (snakeSpeed > MINIMAL_SNAKE_SPEED)
            snakeSpeed -= MINIMAL_SNAKE_SPEED;
    }

    public int getSnakeLenght() {
        return snakeLenght;
    }

    public void setSnakeLenght(int snakeLenght) {
        this.snakeLenght = snakeLenght;
    }

    public Direction getSnakeDirection() {
        return snakeDirection;
    }

    public void setSnakeDirection(Direction snakeDirection) {
        this.snakeDirection = snakeDirection;
    }

    public int getSnakeSpeed() {
        return snakeSpeed;
    }

    public Color getSnakeColor() {
        return snakeColor;
    }
}
