package snake;

import java.util.*;

// Importation des constantes
import static snake.Constants.*;

// Importation des enums
import static snake.GameSettings.Settings;

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
        GREEN("Vert"),
        RED("Rouge"),
        BLUE("Bleu"),
        YELLOW("Jaune"),
        NONE("NONE");

        // Nom en français de la couleur
        private final String name;

        // Liste de toutes les couleurs
        private static final List<SnakeColor> COLOR_POSSIBILITIES = Collections.unmodifiableList(Arrays.asList(values()));
        private static final int SIZE = COLOR_POSSIBILITIES.size();
        private static final Random RANDOM = new Random();

        /**
         * Construit une couleur avec un nom
         *
         * @param colorName Nom de la couleur
         */
        SnakeColor(String colorName) {
            name = colorName;
        }

        /**
         * @return une couleur aléatoire
         */
        public static SnakeColor randomColor() {
            SnakeColor color = COLOR_POSSIBILITIES.get(RANDOM.nextInt(SIZE));
            while (color == NONE)
                color = COLOR_POSSIBILITIES.get(RANDOM.nextInt(SIZE));

            return color;
        }

        /**
         * @return le nom d'une couleur
         */
        public String getName() {
            return this.name;
        }
    }

    // Couleur du serpent
    public SnakeColor snakeColor;

    // Longueur du serpent
    private int length = INITIAL_SNAKE_LENGTH;

    /*
     * Direction du serpent
     * On démarre sans direction précise pour pouvoir partir n'importe où
     */
    private Direction snakeDirection = Direction.NONE;
    // Grille du jeu
    private final Grid grid;
    // Liste des points du serpent
    private final List<SnakeDot> dots;
    // Tête du serpent
    private SnakeDot head;
    // Score du joueur
    private int score;

    private int stepX;
    private int stepY;

    private final int startX;
    private final int startY;

    private boolean isAlive;
    public boolean canDie;

    /**
     * Construit un nouveau serpent
     *
     * @param grid Grille de jeu
     */
    public Snake(SnakeColor color, Grid grid, int x, int y) {
        dots = new LinkedList<>();
        isAlive = true;
        canDie = Settings.WALLS.isActivated();
        this.grid = grid;
        stepX = 0;
        stepY = 0;
        this.snakeColor = color;
        startX = x;
        startY = y;
        createDots();
    }

    /**
     * Crée les points de base du serpent
     */
    private void createDots() {
        for (int z = 0; z < INITIAL_SNAKE_LENGTH; z++) {
            if (z == 0) {
                // Crée une tête en premier
                head = createNewSnakeParts(Dot.DotType.HEAD, startX, startY);
                dots.add(head);
            } else if (z == INITIAL_SNAKE_LENGTH - 1) {
                // Crée une queue en dernier
                // Queue du serpent
                SnakeDot tail = createNewSnakeParts(Dot.DotType.TAIL, startX, startY);
                dots.add(tail);
            } else
                // Crée un corps entre deux
                dots.add(z, createNewSnakeParts(Dot.DotType.BODY, startX, startY));
        }
    }

    /**
     * Est appelé après que le serpent ait mangé une pomme, augmente sa taille et sa vitesse
     *
     * @param dot Le point où était placée la nourriture que le serpent à mangé
     */
    private void growTo(SnakeDot dot) {
        if (Settings.SNAKE_RAINBOW_SHEDDING.isActivated())
            setRandomColor(snakeColor);

        length++;
        canDie = true;
        checkAndAdd(dot);
        checkDotList();
    }

    /**
     * Replace la tête du serpent et enlève son ancienne position
     *
     * @param dot Le nouvel emplacement de la tête du serpent
     */
    private void shiftTo(SnakeDot dot) {
        // Le nouvel emplacement de la tête est défini
        checkAndAdd(dot);
        // L'ancien emplacement est enlevé
        dots.remove(0);
        checkDotList();
    }

    /**
     * Vérifie où placer un point sur la grille qui représente la tête
     *
     * @param dot Le point vers lequel déplacer le serpent
     */
    private void checkAndAdd(SnakeDot dot) {
        dot = grid.wrap(dot);
        if (canDie)
            isAlive &= !dots.contains(dot);
        head = dot;
        score = 100 * (dots.size() - INITIAL_SNAKE_LENGTH);
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
     * @return {@code true} si le serpent s'est mangé lui-même
     */
    public boolean isDead() {
        return !isAlive && canDie;
    }

    /**
     * @return {@code true} si le serpent se déplace
     */
    public boolean isMoving() {
        return !(stepX == 0 & stepY == 0);
    }

    /**
     * Déplace le serpent d'une case
     */
    public void move() {
        if (isMoving()) {
            shiftTo(head.translate(stepX, stepY));
        }
    }

    /**
     * Fait grandir le serpent
     */
    public void extend() {
        if (isMoving()) {
            growTo(head.translate(stepX, stepY));
        }
    }

    /**
     * Change la direction du serpent
     * @param newDirection nouvelle direction du serpent
     */
    public void changeDirection(Direction newDirection) {
        if (!Main.gameHasStarted)
            Main.gameHasStarted = true;

        setSnakeDirection(newDirection);
        switch (newDirection) {
            case RIGHT:
                stepX = 1;
                stepY = 0;
                break;
            case LEFT:
                stepX = -1;
                stepY = 0;
                break;
            case UP:
                stepX = 0;
                stepY = -1;
                break;
            case DOWN:
                stepX = 0;
                stepY = 1;
                break;
        }
    }

    /**
     * @param bodyType type du point dont on veut le sprite
     * @return un sprite choisi selon les paramètres fournis
     */
    private Sprite getSnakeSprite(Dot.DotType bodyType) {
        // Le chemin vers les images du serpent
        String PATH_TO_SNAKE_IMAGES = PATH_TO_IMAGES + snakeColor.toString() + "/";

        switch (bodyType) {
            case HEAD:
            case TAIL:
                return new Sprite(PATH_TO_SNAKE_IMAGES + bodyType.toString().toLowerCase() + "_" + INITIAL_SNAKE_DIRECTION.toString().toLowerCase() + ".png");
            case BODY:
            default:
                String orientation;
                if (INITIAL_SNAKE_DIRECTION == Snake.Direction.LEFT || INITIAL_SNAKE_DIRECTION == Snake.Direction.RIGHT)
                    orientation = "horizontal";
                else
                    orientation = "vertical";

                return new Sprite(PATH_TO_SNAKE_IMAGES + "body_" + orientation + ".png");
        }
    }

    /**
     * Crée un nouveau morceau de corps de serpent
     *
     * @param dotType le type de point de ce corps
     * @param x       sa position x
     * @param y       sa position y
     * @return ce point
     */
    private SnakeDot createNewSnakeParts(Dot.DotType dotType, int x, int y) {
        return new SnakeDot(dotType, x, y, snakeColor, getSnakeSprite(dotType), INITIAL_SNAKE_DIRECTION);
    }

    /**
     * Modifie provisoirement la couleur du serpent
     * @param snakeColor Couleur actuelle
     */
    private void setRandomColor(SnakeColor snakeColor) {
        SnakeColor newColor = SnakeColor.randomColor();
        while (newColor == snakeColor)
            newColor = SnakeColor.randomColor();

        for (SnakeDot dot: dots) {
            dot.setColor(newColor);
        }
    }

    // **************  PROVISOIRE  ************** //
    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    // ******************************  GETTER  ****************************** //

    /**
     * @return Tous les points sur lesquels se trouve le serpent
     */
    public List<SnakeDot> getDots() {
        return dots;
    }

    /**
     * @return Le point correspondant à la tête du serpent
     */
    public SnakeDot getHead() {
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
     *
     * @param newDirection Nouvelle direction du serpent
     */
    public void setSnakeDirection(Direction newDirection) {
        snakeDirection = newDirection;
        head.setDirection(newDirection);
    }

    /**
     * @return le score associé à ce serpent
     */
    public int getScore() {
        return score;
    }

    /**
     * @return la couleur de ce serpent
     */
    public SnakeColor getSnakeColor() {
        return snakeColor;
    }


}
