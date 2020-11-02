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

    private String pathToImages = "res/images/";

    // Vitesse du serpent
    private int snakeSpeed = 140;
    private final int MINIMAL_SNAKE_SPEED = 10;

    // Longueur du serpent
    private int snakeLenght;
    private final int INITIAL_SNAKE_LENGHT = 3;

    public boolean isAlive = true;

    // Couleur du serpent
    private final Color snakeColor;

    /* Images du serpent */
    // Corps du serpent
    private Image bodyHorizonzal;
    private Image bodyVertical;
    // Tête du serpent
    private Image head;
    private Image headLeft;
    private Image headRight;
    private Image headUp;
    private Image headDown;
    // Queue du serpent
    private Image tail;
    private Image tailLeft;
    private Image tailRight;
    private Image tailUp;
    private Image tailDown;

    // Direction du serpent
    private Direction snakeDirection = Direction.RIGHT;
    private Direction snakeLastDirection = snakeDirection;

    /**
     * Crée un nouveau serpent
     * @param snakeColor Couleur du serpent
     */
    public Snake(Color snakeColor) {
        this.snakeLenght = INITIAL_SNAKE_LENGHT;
        this.snakeColor = snakeColor;

        setImages(snakeColor);
    }

    /**
     * Initialise les sprites du serpent
     * @param snakeColor Couleur utilisée pour les sprites
     */
    private void setImages(Color snakeColor) {
        ImageIcon imageIconBodyHorizontal = new ImageIcon(pathToImages + snakeColor.toString() + "/body_horizontal.png");
        bodyHorizonzal = imageIconBodyHorizontal.getImage();
        ImageIcon imageIconBodyVertical = new ImageIcon(pathToImages + snakeColor.toString() + "/body_vertical.png");
        bodyVertical = imageIconBodyVertical.getImage();

        ImageIcon imageIconHead = new ImageIcon(pathToImages + snakeColor.toString() + "/head_right.png");
        head = imageIconHead.getImage();
        ImageIcon imageIconTail = new ImageIcon(pathToImages + snakeColor.toString() + "/tail_right.png");
        tail = imageIconTail.getImage();

        ImageIcon imageIconHeadLeft = new ImageIcon(pathToImages + snakeColor.toString() + "/head_left.png");
        headLeft = imageIconHeadLeft.getImage();
        ImageIcon imageIconHeadRight = new ImageIcon(pathToImages + snakeColor.toString() + "/head_right.png");
        headRight = imageIconHeadRight.getImage();
        ImageIcon imageIconHeadUp = new ImageIcon(pathToImages + snakeColor.toString() + "/head_up.png");
        headUp = imageIconHeadUp.getImage();
        ImageIcon imageIconHeadDown = new ImageIcon(pathToImages + snakeColor.toString() + "/head_down.png");
        headDown = imageIconHeadDown.getImage();


        ImageIcon imageIconTailLeft = new ImageIcon(pathToImages + snakeColor.toString() + "/tail_left.png");
        tailLeft = imageIconTailLeft.getImage();
        ImageIcon imageIconTailRight = new ImageIcon(pathToImages + snakeColor.toString() + "/tail_right.png");
        tailRight = imageIconTailRight.getImage();
        ImageIcon imageIconTailUp = new ImageIcon(pathToImages + snakeColor.toString() + "/tail_up.png");
        tailUp = imageIconTailUp.getImage();
        ImageIcon imageIconTailDown = new ImageIcon(pathToImages + snakeColor.toString() + "/tail_down.png");
        tailDown = imageIconTailDown.getImage();

    }

    /**
     * Dessine le serpent sur le plateau de jeu
     * @param g Graphics
     * @param imageObserver Observer d'image
     */
    public void draw(Graphics g, ImageObserver imageObserver) {
        Image headSprite = head;
        Image tailSprite = tail;

        switch (snakeDirection) {
            case LEFT:
                headSprite = headLeft;
                tailSprite = tailLeft;
                break;
            case RIGHT:
                headSprite = headRight;
                tailSprite = tailRight;
                break;
            case UP:
                headSprite = headUp;
                tailSprite = tailUp;
                break;
            case DOWN:
                headSprite = headDown;
                tailSprite = tailDown;
                break;
        }

        for (int z = 0; z < snakeLenght; z++) {
            if (z == 0) {
                g.drawImage(headSprite, Board.X[z], Board.Y[z], imageObserver);
            } else if (z == snakeLenght - 1) {
                g.drawImage(tailSprite, Board.X[z], Board.Y[z], imageObserver);
            } else {
                if(snakeDirection == Direction.LEFT || snakeDirection == Direction.RIGHT)
                    g.drawImage(bodyHorizonzal, Board.X[z], Board.Y[z], imageObserver);
                else
                    g.drawImage(bodyVertical, Board.X[z], Board.Y[z], imageObserver);
            }
        }
    }

    /**
     * Déplace le serpent
     */
    public void move() {
        for (int z = snakeLenght; z > 0; z--) {
            Board.X[z] = Board.X[(z - 1)];
            Board.Y[z] = Board.Y[(z - 1)];
        }

        switch (snakeDirection) {
            case LEFT:
                Board.X[0] -= Board.TILE_SIZE;
                break;
            case RIGHT:
                Board.X[0] += Board.TILE_SIZE;
                break;
            case UP:
                Board.Y[0] -= Board.TILE_SIZE;
                break;
            case DOWN:
                Board.Y[0] += Board.TILE_SIZE;
                break;
        }
    }

    /**
     * Le serpent mange une pomme
     */
    public void ateApple() {
        snakeLenght++;
        if (snakeSpeed > MINIMAL_SNAKE_SPEED)
            snakeSpeed -= MINIMAL_SNAKE_SPEED;
        System.out.println("Direction actuelle : " + snakeDirection);
        System.out.println("Direction précédente : " + snakeLastDirection);
    }

    /**
     * @return La longueur du serpent
     */
    public int getSnakeLenght() {
        return snakeLenght;
    }

//    /**
//     * Défini la longueur du serpent
//     * @param snakeLenght Nouvelle longueur du serpent
//     */
//    public void setSnakeLenght(int snakeLenght) {
//        this.snakeLenght = snakeLenght;
//    }

    /**
     * @return La direction vers laquelle se dirige le serpent
     */
    public Direction getSnakeDirection() {
        return snakeDirection;
    }

    /**
     * Défini la direction du serpent
     * @param snakeDirection Nouvelle direction du serpent
     */
    public void setSnakeDirection(Direction snakeDirection) {
        this.snakeDirection = snakeDirection;
    }

//    /**
//     * @return La direction vers laquelle se dirigeait le serpent
//     */
//    public Direction getSnakeLastDirection() {
//        return snakeLastDirection;
//    }

    /**
     * Défini la direction précédente du serpent
     * @param snakeLastDirection Dernière direction du serpent
     */
    public void setSnakeLastDirection(Direction snakeLastDirection) {
        this.snakeLastDirection = snakeLastDirection;
    }

    /**
     * @return La vitesse du serpent
     */
    public int getSnakeSpeed() {
        return snakeSpeed;
    }

//    /**
//     * @return La couleur du serpent
//     */
//    public Color getSnakeColor() {
//        return snakeColor;
//    }
}
