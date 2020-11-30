package snake;

import java.util.*;

// Importation des constantes
import static snake.Constants.*;

/**
 * Classe qui représente un serpent
 */
public class Snake {

    // Directions possibles du serpent
    public enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT,
        NONE
    }

    // Couleurs que le serpent peut avoir
    public enum SnakeColor {
        GREEN,
        RED,
        BLUE,
        YELLOW,
        NONE;

        private static final List<SnakeColor> COLOR_POSSIBILITIES =
                Collections.unmodifiableList(Arrays.asList(values()));
        private static final int SIZE = COLOR_POSSIBILITIES.size();
        private static final Random RANDOM = new Random();

        public static SnakeColor randomColor() {
            SnakeColor color = COLOR_POSSIBILITIES.get(RANDOM.nextInt(SIZE));
            while (color == NONE) {
                color = COLOR_POSSIBILITIES.get(RANDOM.nextInt(SIZE));
            }
            return color;
        }
    }

    // Couleur du serpent
    public static SnakeColor SNAKE_COLOR = SnakeColor.randomColor();

    private final int MINIMUM_SNAKE_LENGTH = 2;

    // Longueur du serpent
    private int length = INITIAL_SNAKE_LENGTH;

    /*
     * Direction du serpent
     * On démarre sans direction pour pouvoir partir n'importe où
     */
    private Direction snakeDirection = Direction.NONE;
    // Grille du jeu
    private Grid grid;
    // Liste des points du serpent
    private List<Dot> dots;
    // Tête du serpent
    private Dot head;
    // Queue du serpent
    private Dot tail;
    // Score du joueur
    private int score;

    // Vitesse du serpent
    private int velocity = INITIAL_SNAKE_VELOCITY;
    private int xVelocity;
    private int yVelocity;
    private int stepX;
    private int stepY;

    private boolean isAlive;

    /**
     * Construit un nouveau serpent
     * @param grid Grille de jeu
     */
    public Snake(Grid grid) {
        dots = new LinkedList<>();
        isAlive = true;
        this.grid = grid;
        xVelocity = 0;
        yVelocity = 0;
        stepX = 0;
        stepY = 0;
        createDots();
    }

    /**
     * Crée les points de base du serpent
     */
    private void createDots() {
        for (int z = 0; z < INITIAL_SNAKE_LENGTH; z++) {
            if (z == 0) {
                // Crée une tête en premier
                head = new Dot(Dot.DotType.HEAD, grid.getRows() / 2, grid.getCols() / 2, INITIAL_SNAKE_DIRECTION);
                dots.add(head);
            } else if (z == INITIAL_SNAKE_LENGTH - 1) {
                // Crée une queue en dernier
                tail = new Dot(Dot.DotType.TAIL, grid.getRows() / 2, grid.getCols() / 2, INITIAL_SNAKE_DIRECTION);
                dots.add(tail);
            } else
                // Crée un corps entre deux
                dots.add(new Dot(Dot.DotType.BODY, grid.getRows() / 2, grid.getCols() / 2, INITIAL_SNAKE_DIRECTION));
        }
    }

    /**
     * Est appelé après que le serpent ait mangé une pomme, augmente sa taille et sa vitesse et place une nouvelle pomme
     * @param dot Le point où était placée la nourriture que le serpent à mangé
     */
    private void growTo(Dot dot) {
        length++;
        increaseVelocity();
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
        score = 100 * (this.getDots().size() - INITIAL_SNAKE_LENGTH);
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
        return !isAlive && length != MINIMUM_SNAKE_LENGTH;
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
     * Modifie la direction du serpent
     * @param newDirection Nouvelle direction du serpent
     */
    public void setSnakeDirection(Direction newDirection) {
        snakeDirection = newDirection;
        head.setDirection(newDirection);
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
            shiftTo(head.translate(head.getDotType(), stepX, stepY, head.getDirection()));
//            shiftTo(head.translate(head.getDotType(), xVelocity, yVelocity));
        }
    }

    /**
     * Fait grandir le serpent
     */
    public void extend() {
        if (isMoving()) {
            growTo(head.translate(head.getDotType(), stepX, stepY, head.getDirection()));
//            growTo(head.translate(head.getDotType(), xVelocity, yVelocity));
        }
    }

    /**
     * @return le score associé à ce serpent
     */
    public int getScore() {
        return score;
    }

    /**
     * Déplace le serpent vers le haut
     */
    public void setUp() {
        xVelocity = 0;
        yVelocity = -velocity;
        stepX = 0;
        stepY = -1;
        setSnakeDirection(Direction.UP);
    }

    /**
     * Déplace le serpent vers le bas
     */
    public void setDown() {
        xVelocity = 0;
        yVelocity = velocity;
        stepX = 0;
        stepY = 1;
        setSnakeDirection(Direction.DOWN);
    }

    /**
     * Déplace le serpent vers la gauche
     */
    public void setLeft() {
        xVelocity = -velocity;
        yVelocity = 0;
        stepX = -1;
        stepY = 0;
        setSnakeDirection(Direction.LEFT);
    }

    /**
     * Déplace le serpent vers la droite
     */
    public void setRight() {
        xVelocity = velocity;
        yVelocity = 0;
        stepX = 1;
        stepY = 0;
        setSnakeDirection(Direction.RIGHT);
    }

    /**
     * Augmente la vitesse du serpent
     */
    public void increaseVelocity() {
        velocity += SNAKE_VELOCITY_INCREASE;

        if (xVelocity != 0) {
            if (xVelocity < 0)
                xVelocity = -velocity;
            else
                xVelocity = velocity;
        }

        if (yVelocity != 0) {
            if (yVelocity < 0)
                yVelocity = -velocity;
            else
                yVelocity = velocity;
        }
    }

}
