package snake;

import java.util.LinkedList;
import java.util.List;

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

    // Couleurs possbiles pour le serpent
    public enum SnakeColor {
        GREEN,
        RED,
        BLUE,
        YELLOW
    }

    // Couleur du serpent
    public static final SnakeColor SNAKE_COLOR = SnakeColor.YELLOW;
    // Grille du jeu
    private Grid grid;
    // Longueur du serpent en début de partie
    private int INITIAL_SNAKE_LENGTH = 1;
    // Longueur du serpent
    private int length = INITIAL_SNAKE_LENGTH;

    // Direction initiale du serpent
    private final Snake.Direction INITIAL_SNAKE_DIRECTION = Snake.Direction.LEFT;
    // Direction du serpent
    private Direction snakeDirection = INITIAL_SNAKE_DIRECTION;

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
//     * @param headDot Le point où sera mise la tête du serpent
//     * @param tailDot Le point où sera mise la queue du serpent
     */
    public Snake(Grid grid) {
        dots = new LinkedList<>();
//        dots.add(headDot);
//        dots.add(tailDot);
//        head = headDot;
//        tail = tailDot;
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
     * Est appelé après que le serpent ait mangé une pomme, augmente sa taille et place une nouvelle pomme
     * @param dot Le point où était placée la nourriture que le serpent à mangé
     */
    private void growTo(Dot dot) {
        length++;
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
     * @return {@code true} si le serpent ne s'est pas mangé lui-même
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
//            shiftTo(head.translate(Dot.DotType.TAIL, xVelocity, yVelocity));
            shiftTo(head.translate(head.getDotType(), xVelocity, yVelocity));
        }
    }

    /**
     * Fait grandir le serpent
     */
    public void extend() {
        if (isMoving()) {
//            growTo(head.translate(head.getDotType(), xVelocity, yVelocity));
            growTo(head.translate(Dot.DotType.BODY, xVelocity, yVelocity));
        }
    }

    public void setUp() {
        if (yVelocity == 1 && length > 1) return;
        xVelocity = 0;
        yVelocity = -1;
        snakeDirection = Direction.UP;
    }

    public void setDown() {
        if (yVelocity == -1 && length > 1) return;
        xVelocity = 0;
        yVelocity = 1;
        snakeDirection = Direction.DOWN;
    }

    public void setLeft() {
        if (xVelocity == 1 && length > 1) return;
        xVelocity = -1;
        yVelocity = 0;
        snakeDirection = Direction.LEFT;
    }

    public void setRight() {
        if (xVelocity == -1 && length > 1) return;
        xVelocity = 1;
        yVelocity = 0;
        snakeDirection = Direction.RIGHT;
    }
//    // Liste des points du serpent
//    private ArrayList<SnakeDot> snakeDots = new ArrayList<>();
//
//
//    /**
//     * Crée les points de base du serpent
//     * @param snakeColor Couleur du serpent
//     */
//    private void createDots(Color snakeColor) {
//        for (int z = 0; z < INITIAL_SNAKE_LENGTH; z++) {
//            if (z == 0)
//                // Crée une tête en premier
//                snakeDots.add(new SnakeDot(SnakeDot.DotType.Head, snakeColor, INITIAL_SNAKE_DIRECTION));
//            else if (z == INITIAL_SNAKE_LENGTH - 1)
//                // Crée une queue en dernier
//                snakeDots.add(new SnakeDot(SnakeDot.DotType.Tail, snakeColor, INITIAL_SNAKE_DIRECTION));
//            else
//                // Crée un corps entre deux
//                snakeDots.add(new SnakeDot(SnakeDot.DotType.Body, snakeColor, INITIAL_SNAKE_DIRECTION));
//        }
//    }
//
//    /**
//     * Dessine le serpent sur le plateau de jeu
//     * @param g GraphicsContext
//     */
//    public void draw(GraphicsContext g) {
//
//        for (int z = 0; z < snakeDots.size(); z++) {
//            snakeDots.get(z).draw(g, z);
//        }
//
////        for (int z = 0; z < snakeLength; z++) {
////            if (z == 0) {
////                g.drawImage(headSprite, Board.X[z], Board.Y[z], imageObserver);
////            } else if (z == snakeLength - 1) {
////                g.drawImage(tailSprite, Board.X[z], Board.Y[z], imageObserver);
////            } else {
////                if(snakeDirection == Direction.LEFT || snakeDirection == Direction.RIGHT)
////                    g.drawImage(bodyHorizonzal, Board.X[z], Board.Y[z], imageObserver);
////                else
////                    g.drawImage(bodyVertical, Board.X[z], Board.Y[z], imageObserver);
////            }
////        }
//    }
//
//    public void delete(GraphicsContext g) {
//        for (int z = 0; z < snakeDots.size(); z++) {
//            snakeDots.get(z).delete(g, z);
//        }
//    }
//
//    /**
//     * Déplace le serpent
//     */
//    public void move(GraphicsContext g) {
//
//
//        for (int z = snakeDots.size(); z > 0; z--) {
//            Game.X[z] = Game.X[(z - 1)];
//            Game.Y[z] = Game.Y[(z - 1)];
//        }
//
////        switch (snakeDirection) {
////            case LEFT:
////                Board.X[0] -= Board.TILE_SIZE;
////                break;
////            case RIGHT:
////                Board.X[0] += Board.TILE_SIZE;
////                break;
////            case UP:
////                Board.Y[0] -= Board.TILE_SIZE;
////                break;
////            case DOWN:
////                Board.Y[0] += Board.TILE_SIZE;
////                break;
////        }
//
//        switch (snakeDots.get(0).getDotDirection()) {
//            case LEFT:
//                Game.X[0] -= Game.TILE_SIZE;
//                break;
//            case RIGHT:
//                Game.X[0] += Game.TILE_SIZE;
//                break;
//            case UP:
//                Game.Y[0] -= Game.TILE_SIZE;
//                break;
//            case DOWN:
//                Game.Y[0] += Game.TILE_SIZE;
//                break;
//        }
//
//        draw(g);
//    }
//
//    /**
//     * Le serpent mange une pomme
//     */
//    public void ateApple() {
//        for (SnakeDot dot : snakeDots) {
//            dot.setDotDirection(snakeDirection);
//        }
//
//        snakeDots.add(snakeDots.size() - 1, new SnakeDot(SnakeDot.DotType.Body, snakeColor, snakeDirection));
//        if (snakeSpeed > MINIMAL_SNAKE_SPEED)
//            snakeSpeed -= MINIMAL_SNAKE_SPEED;
//    }
//
//    /**
//     * @return La longueur du serpent
//     */
//    public int getSnakeLength() {
//        return snakeDots.size();
//    }
//
//    /**
//     * @return La direction vers laquelle se dirige le serpent
//     */
//    public Direction getSnakeDirection() {
//        return snakeDirection;
//    }
//
//    /**
//     * Défini la direction du serpent
//     * @param snakeDirection Nouvelle direction du serpent
//     */
//    public void setSnakeDirection(Direction snakeDirection) {
//        Direction previousDirection = null;
//        if (snakeDots.get(0).getDotPreviousDirection() != snakeDirection && snakeDots.get(0).getDotPreviousDirection() != this.snakeDirection) {
//            previousDirection = snakeDots.get(0).getDotPreviousDirection();
////            System.out.println("Direction précédente : " + previousDirection);
////            System.out.println("Direction actuelle : " + snakeDirection + "\n");
//        }
//
//        this.snakeDirection = snakeDirection;
//        for (SnakeDot dot: snakeDots) {
//            /* - Parcourir la liste
//             * - Ignorer la tête (changer uniquement sa direction)
//             * - mettre un angle
//             * - déplacer l'angle
//             * - répéter
//             * - mettre la queue à la fin
//             */
//            dot.setSprite(snakeDirection, previousDirection);
//
//            dot.setDotPreviousDirection(dot.getDotDirection());
//            dot.setDotDirection(snakeDirection);
//        }
////        snakeDots.get(0).setDotDirection(snakeDirection);
//    }
//
//    /**
//     * @return La vitesse du serpent
//     */
//    public int getSnakeSpeed() {
//        return snakeSpeed;
//    }

}
