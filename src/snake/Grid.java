package snake;

import javafx.scene.paint.Color;

import java.util.Random;

/**
 * Représente une grille
 */
public class Grid {

    // La taille d'une case de la grille
    public static final int TILE_SIZE = 10;
    // Couleur de fond de la grille
    public static final Color BACKGROUND_COLOR = new Color(0.1, 0.1, 0.1, 1);
    // Couleur des textes
    public static final Color TEXT_COLOR = Color.BEIGE;

    // Le nombre de colonnes de la grille
    private final int cols;
    // Le nombre de lignes de la grille
    private final int rows;

    private final Snake snake;
    private final Food food;

    /**
     * Crée une nouvelle grille
     * @param width Largeur de la grille
     * @param height Hauteur de la grille
     */
    public Grid(final double width, final double height) {
        rows = (int) width / TILE_SIZE;
        cols = (int) height / TILE_SIZE;

        // Place le serpent au centre de l'écran
        snake = new Snake(this);

        // Place la pomme à un endroit aléatoire
        food = new Food(getRandomDot());
    }

    /**
     * Aligne un point sur la grille
     * @param dot Le point dont on veut corriger l'emplacement
     * @return Le point aligné correctement
     */
    public Dot wrap(Dot dot) {
        int x = dot.getX();
        int y = dot.getY();
        if (x >= rows) x = 0;
        if (y >= cols) y = 0;
        if (x < 0) x = rows - 1;
        if (y < 0) y = cols - 1;

        // Recrée et retourne le point avec le bon alignement
        return new Dot(dot.getDotType(), x, y);
    }

    /**
     * @return un point tiré au hasard
     */
    private Dot getRandomDot() {
        Random random = new Random();
        Dot randomDot;
        do {
            randomDot = new Dot(Dot.DotType.FOOD, random.nextInt(rows), random.nextInt(cols));
        } while (randomDot.equals(snake.getHead()));
        return randomDot;
    }

    /**
     * Méthode appelée à chaque cycle d'éxécution<br>
     *
     * Teste si le serpent mange une pomme<br>
     * - S'il en mange une : fais grandir le serpent et place une pomme<br>
     * - Sinon : déplace le serpent sur la case suivante
     */
    public void update() {
        if (food.getDot().equals(snake.getHead())) {
            snake.extend();
            food.setDot(getRandomDot());
        } else {
            snake.move();
        }
    }

    /**
     * @return Les colonnes de la grille
     */
    public int getCols() {
        return cols;
    }

    /**
     * @return Les lignes de la grille
     */
    public int getRows() {
        return rows;
    }

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
