package snake;

import javafx.scene.paint.Color;

import java.util.Random;

/**
 * Représente une grille
 */
public class Grid {

    // La taille d'une case de la grille
    public static final int TILE_SIZE = 10;
    public static final Color COLOR = new Color(0.1, 0.1, 0.1, 1);

    private final int cols;     // Le nombre de colonnes de la grille
    private final int rows;     // Le nombre de lignes de la grille

    private Snake snake;
    private Food food;

    /**
     * Crée une nouvelle grille
     * @param width
     * @param height
     */
    public Grid(final double width, final double height) {
        rows = (int) width / TILE_SIZE;
        cols = (int) height / TILE_SIZE;

        // Place le serpent au centre de l'écran
        snake = new Snake(this, new Dot(rows / 2, cols / 2));

        // Place la pomme à un endroit aléatoire
        food = new Food(getRandomDot());
    }

    public Dot wrap(Dot dot) {
        int x = dot.getX();
        int y = dot.getY();
        if (x >= rows) x = 0;
        if (y >= cols) y = 0;
        if (x < 0) x = rows - 1;
        if (y < 0) y = cols - 1;

        return new Dot(x, y);
    }

    /**
     * @return un point tiré au hasard
     */
    private Dot getRandomDot() {
        Random random = new Random();
        Dot dot;
        do {
            dot = new Dot(random.nextInt(rows), random.nextInt(cols));
        } while (dot.equals(snake.getHead()));
        return dot;
    }

    /**
     * Teste si le serpent mange une pomme
     * - S'il en mange : fais grandir le serpent et place une pomme
     * - Sinon : déplace le serpent sur la case suivante
     * Appelée à chaque cycle d'éxécution
     */
    public void update() {
        if (food.getDot().equals(snake.getHead())) {
            snake.extend();
            food.setDot(getRandomDot());
        } else {
            snake.move();
        }
    }

//    /**
//     * @return Les colonnes de la grille
//     */
//    public int getCols() {
//        return cols;
//    }
//
//    /**
//     * @return Les lignes de la grille
//     */
//    public int getRows() {
//        return rows;
//    }

    /**
     * @return La largeur de la grille
     */
    public double getWidth() {
        return rows * TILE_SIZE;
    }

    /**
     * @return La hauteur de la grille
     */
    public double getHeight() {
        return cols * TILE_SIZE;
    }

    /**
     * @return le serpent
     */
    public Snake getSnake() {
        return snake;
    }

    /**
     * @return la nourriture du serpent
     */
    public Food getFood() {
        return food;
    }
}
