package snake;

import java.util.LinkedList;
import java.util.List;

import static snake.Constants.INITIAL_SNAKE_LENGTH;
import static snake.Constants.TIMELINE_RATE_INCREASE;

/**
 * Classe qui représente un serpent
 */
public class Snake {

    /**
     * Directions possibles du serpent
     */
    public enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT,
        NONE
    }

    /**
     * Couleurs que le serpent peut avoir
     */
    public enum SnakeColor {
        GREEN,
        RED,
        BLUE,
        YELLOW,
        NONE
    }

    /**
     * Couleur du serpent
     */
    public static final SnakeColor SNAKE_COLOR = SnakeColor.YELLOW;

    // Longueur du serpent
    private int length = INITIAL_SNAKE_LENGTH;
    // Direction du serpent
    private Direction snakeDirection = Constants.INITIAL_SNAKE_DIRECTION;
    // Grille du jeu
    private Grid grid;
    // Liste des points du serpent
    private List<Dot> dots;
    // Tête du serpent
    private Dot head;
    // Queue du serpent
    private Dot tail;

    private int xVelocity;
    private int yVelocity;
    private boolean isAlive;

    /**
     * Construit un serpent
     * @param grid Grille de jeu
     */
    public Snake(Grid grid) {
        dots = new LinkedList<>();
        isAlive = true;
        this.grid = grid;
        xVelocity = 0;
        yVelocity = 0;
        createDots();
    }

    /**
     * Crée les points de base du serpent
     */
    private void createDots() {
        for (int z = 0; z < INITIAL_SNAKE_LENGTH; z++) {
            if (z == 0) {
                // Crée une tête en premier
                head = new Dot(Dot.DotType.HEAD, grid.getRows() / 2, grid.getCols() / 2);
                dots.add(head);
            } else if (z == INITIAL_SNAKE_LENGTH - 1) {
                // Crée une queue en dernier
                tail = new Dot(Dot.DotType.TAIL, grid.getRows() / 2 + z, grid.getCols() / 2);
                dots.add(tail);
            } else
                // Crée un corps entre deux
                dots.add(new Dot(Dot.DotType.BODY, grid.getRows() / 2 + z, grid.getCols() / 2));
        }
    }

    /**
     * Est appelé après que le serpent ait mangé une pomme, augmente sa taille et sa vitesse et place une nouvelle pomme
     * @param dot Le point où était placée la nourriture que le serpent à mangé
     */
    private void growTo(Dot dot) {
        length++;
        Main.addToRate(TIMELINE_RATE_INCREASE);
        checkAndAdd(dot);
        checkDotList();
    }

    /**
     * Replace la tête du serpent et enlève son ancienne position
     * @param dot Le nouvel emplacement de la tête du serpent
     */
    private void shiftTo(Dot dot) {
        // Le nouvel emplacement de la tête est défini
        checkAndAdd(dot);
        // L'ancien emplacement est enlevé
        dots.remove(0);
        checkDotList();
    }

    /**
     * Vérifie où placer un point sur la grille qui représente la tête
     * @param dot Le point vers lequel déplacer le serpent
     */
    private void checkAndAdd(Dot dot) {
        dot = grid.wrap(dot);
        isAlive &= !dots.contains(dot);
        head = dot;
        dots.add(dot);
    }

    /**
     * Vérifie le type des éléments de la liste selon leur emplacement
     */
    private void checkDotList() {
        if (length > 1) {
            for (int z = 0; z < dots.size(); z++) {
                if (z == 0) {
                    dots.get(z).setDotType(Dot.DotType.TAIL);
                } else if (z == dots.size() - 1) {
                    dots.get(z).setDotType(Dot.DotType.HEAD);
                } else {
                    dots.get(z).setDotType(Dot.DotType.BODY);
                }
            }
        } else {
            dots.get(0).setDotType(Dot.DotType.HEAD);
        }
    }

    /**
     * @return Tous les points sur lesquels se trouve le serpent
     */
    public List<Dot> getDots() {
        return dots;
    }

    /**
     * @return {@code true} si le serpent s'est mangé lui-même
     */
    public boolean isDead() {
        return !isAlive && length != 1;
    }

    /**
     * @return Le point correspondant à la tête du serpent
     */
    public Dot getHead() {
        return head;
    }

    /**
     * @return La direction du serpent
     */
    public Direction getSnakeDirection() {
        return snakeDirection;
    }

    /**
     * @return {@code true} si le serpent se déplace
     */
    private boolean isMoving() {
        return !(xVelocity == 0 & yVelocity == 0);
    }

    /**
     * Déplace le serpent d'une case
     */
    public void move() {
        if (isMoving()) {
            shiftTo(head.translate(head.getDotType(), xVelocity, yVelocity));
        }
    }

    /**
     * Fait grandir le serpent
     */
    public void extend() {
        if (isMoving()) {
            growTo(head.translate(Dot.DotType.BODY, xVelocity, yVelocity));
        }
    }

    /**
     * Déplace le serpent vers le haut
     */
    public void setUp() {
        if (yVelocity == 1 && length > 1) return;
        xVelocity = 0;
        yVelocity = -1;
        snakeDirection = Direction.UP;
    }

    /**
     * Déplace le serpent vers le bas
     */
    public void setDown() {
        if (yVelocity == -1 && length > 1) return;
        xVelocity = 0;
        yVelocity = 1;
        snakeDirection = Direction.DOWN;
    }

    /**
     * Déplace le serpent vers la gauche
     */
    public void setLeft() {
        if (xVelocity == 1 && length > 1) return;
        xVelocity = -1;
        yVelocity = 0;
        snakeDirection = Direction.LEFT;
    }

    /**
     * Déplace le serpent vers la droite
     */
    public void setRight() {
        if (xVelocity == -1 && length > 1) return;
        xVelocity = 1;
        yVelocity = 0;
        snakeDirection = Direction.RIGHT;
    }
}
