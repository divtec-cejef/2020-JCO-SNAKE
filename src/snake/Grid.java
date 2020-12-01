package snake;

import java.util.Random;

// Importation des constantes
import static snake.Constants.*;

/**
 * Représente une grille
 */
public class Grid {

    // Le nombre de colonnes de la grille
    private final int cols;
    // Le nombre de lignes de la grille
    private final int rows;

    private final Snake snake;
    private Food food = new Food();

    /**
     * Crée une nouvelle grille
     */
    public Grid() {
        rows = WIDTH / TILE_SIZE;
        cols = HEIGHT / TILE_SIZE;

        // Place le serpent sur la grille
        snake = new Snake(this);

        // Place la pomme à un endroit aléatoire
//        food = new Food(getRandomDot());
        food = setNewFood();
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
        return new Dot(dot.getDotType(), x, y, dot.getSprite(), dot.getDirection());
    }

    /**
     * Aligne un point sur la grille
     * @param dot Le point dont on veut corriger l'emplacement
     * @return Le point aligné correctement
     */
    public SnakeDot wrap(SnakeDot dot) {
        int x = dot.getX();
        int y = dot.getY();

        if (x >= rows) x = 0;
        if (y >= cols) y = 0;
        if (x < 0) x = rows - 1;
        if (y < 0) y = cols - 1;

        // Recrée et retourne le point avec le bon alignement
        return new SnakeDot(dot.getDotType(), x, y, dot.getSprite(), dot.getDirection());
    }

//    /**
//     * @return un point tiré au hasard
//     */
//    private Dot getRandomDot() {
//        Random random = new Random();
//        Dot randomDot;
//        do {
//            randomDot = new Dot(Dot.DotType.FOOD, random.nextInt(rows), random.nextInt(cols), Food.getSprite(), Snake.Direction.NONE);
//        } while (randomDot.equals(snake.getHead()));
//
//        return randomDot;
//    }

    private Food setNewFood() {
        Random random = new Random();
        Food randomApple;
        do {
            randomApple = new Food(random.nextInt(rows), random.nextInt(cols));
        } while (randomApple.equals(snake.getHead()));

        return randomApple;
    }

    /**
     * Méthode appelée à chaque cycle d'éxécution<br>
     * Teste si le serpent mange une pomme<br>
     * - S'il en mange une : fais grandir le serpent et place une pomme<br>
     * - Sinon : déplace le serpent sur la case suivante
     */
    public void update() {
//        if (food.getDot().equals(snake.getHead())) {
        if (food.getDot().equals(snake.getHead())) {
            snake.extend();
            food = setNewFood();
//            food.setDot(getRandomDot());
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
