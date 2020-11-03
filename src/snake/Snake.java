package snake;

import java.awt.*;
import java.awt.image.ImageObserver;
import java.util.ArrayList;

/**
 * Classe qui représente un serpent
 */
public class Snake {
    // Directions possibles du serpent
    public enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT
    }

    // Couleurs possibles pour le serpent
    public enum Color {
        GREEN,
        RED,
        BLUE,
        YELLOW
    }

    // Vitesse du serpent
    private int snakeSpeed = 140;
    // Vitesse maximale du serpent
    private final int MINIMAL_SNAKE_SPEED = 10;

    // Longueur initiale du serpent (ne peut pas être < 2)
    private final int INITIAL_SNAKE_LENGTH = 2;

    // Le serpent est en vie
    public boolean isAlive = true;

    // Couleur du serpent
    private final Color snakeColor;

    // Direction initiale du serpent
    private final Snake.Direction INITIAL_SNAKE_DIRECTION = Snake.Direction.RIGHT;
    // Direction du serpent
    private Direction snakeDirection = INITIAL_SNAKE_DIRECTION;

    // Liste des points du serpent
    private ArrayList<SnakeDot> snakeDots = new ArrayList<>();

    /**
     * Crée un nouveau serpent
     * @param snakeColor Couleur du serpent
     */
    public Snake(Color snakeColor) {
        this.snakeColor = snakeColor;

        createDots(snakeColor);
    }

    /**
     * Crée les points de base du serpent
     * @param snakeColor Couleur du serpent
     */
    private void createDots(Color snakeColor) {
        for (int z = 0; z < INITIAL_SNAKE_LENGTH; z++) {
            if (z == 0)
                // Crée une tête en premier
                snakeDots.add(new SnakeDot(SnakeDot.DotType.Head, snakeColor, INITIAL_SNAKE_DIRECTION));
            else if (z == INITIAL_SNAKE_LENGTH - 1)
                // Crée une queue en dernier
                snakeDots.add(new SnakeDot(SnakeDot.DotType.Tail, snakeColor, INITIAL_SNAKE_DIRECTION));
            else
                // Crée un corps entre deux
                snakeDots.add(new SnakeDot(SnakeDot.DotType.Body, snakeColor, INITIAL_SNAKE_DIRECTION));
        }
    }

    /**
     * Dessine le serpent sur le plateau de jeu
     * @param g Graphics
     * @param imageObserver Observer d'image
     */
    public void draw(Graphics g, ImageObserver imageObserver) {

        for (int z = 0; z < snakeDots.size(); z++) {
            snakeDots.get(z).draw(g, imageObserver, z);
        }

//        for (int z = 0; z < snakeLength; z++) {
//            if (z == 0) {
//                g.drawImage(headSprite, Board.X[z], Board.Y[z], imageObserver);
//            } else if (z == snakeLength - 1) {
//                g.drawImage(tailSprite, Board.X[z], Board.Y[z], imageObserver);
//            } else {
//                if(snakeDirection == Direction.LEFT || snakeDirection == Direction.RIGHT)
//                    g.drawImage(bodyHorizonzal, Board.X[z], Board.Y[z], imageObserver);
//                else
//                    g.drawImage(bodyVertical, Board.X[z], Board.Y[z], imageObserver);
//            }
//        }
    }

    /**
     * Déplace le serpent
     */
    public void move() {
        for (int z = snakeDots.size(); z > 0; z--) {
            Board.X[z] = Board.X[(z - 1)];
            Board.Y[z] = Board.Y[(z - 1)];
        }

//        switch (snakeDirection) {
//            case LEFT:
//                Board.X[0] -= Board.TILE_SIZE;
//                break;
//            case RIGHT:
//                Board.X[0] += Board.TILE_SIZE;
//                break;
//            case UP:
//                Board.Y[0] -= Board.TILE_SIZE;
//                break;
//            case DOWN:
//                Board.Y[0] += Board.TILE_SIZE;
//                break;
//        }

        switch (snakeDots.get(0).getDotDirection()) {
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
        for (SnakeDot dot : snakeDots) {
            dot.setDotDirection(snakeDirection);
        }

        snakeDots.add(snakeDots.size() - 1, new SnakeDot(SnakeDot.DotType.Body, snakeColor, snakeDirection));
        if (snakeSpeed > MINIMAL_SNAKE_SPEED)
            snakeSpeed -= MINIMAL_SNAKE_SPEED;
    }

    /**
     * @return La longueur du serpent
     */
    public int getSnakeLength() {
        return snakeDots.size();
    }

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
        if (snakeDots.get(0).getDotPreviousDirection() != snakeDirection && snakeDots.get(0).getDotPreviousDirection() != this.snakeDirection) {
            System.out.println("Direction précédente : " + snakeDots.get(0).getDotPreviousDirection());
            System.out.println("Direction actuelle : " + snakeDirection + "\n");
        }

        this.snakeDirection = snakeDirection;
        for (SnakeDot dot: snakeDots) {
            /* - Parcourir la liste
             * - Ignorer la tête (changer uniquement sa direction)
             * - mettre un angle
             * - déplacer l'angle
             * - répéter
             * - mettre la queue à la fin
             */
            dot.setDotPreviousDirection(dot.getDotDirection());
            dot.setDotDirection(snakeDirection);
        }
//        snakeDots.get(0).setDotDirection(snakeDirection);
    }

    /**
     * @return La vitesse du serpent
     */
    public int getSnakeSpeed() {
        return snakeSpeed;
    }

}
