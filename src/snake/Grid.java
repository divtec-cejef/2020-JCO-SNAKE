package snake;

import java.util.Random;

// Importation des constantes
import static snake.Constants.*;

// Importation des enums
import static snake.GameSettings.Settings;

/**
 * Représente une grille
 */
public class Grid {

    // Le nombre de colonnes de la grille
    private final int cols;
    // Le nombre de lignes de la grille
    private final int rows;

    // Serpents
    private final Snake playerOneSnake;
    private Snake playerTwoSnake;

    private Food food;

    /**
     * Crée une nouvelle grille
     */
    public Grid() {
        rows = WIDTH / TILE_SIZE;
        cols = HEIGHT / TILE_SIZE;

        // Place le serpent du joueur 1 sur la grille
        int coordX = rows / 2;
        int coordY = cols / 2;

        if (Main.isIsInMultiGame()){
            coordX = rows / 3;
            coordY = cols / 2;
        }

        playerOneSnake = new Snake(Snake.SnakeColor.randomColor(), this, coordX, coordY);

        // Place le serpent du joueur 2 sur la grille
        if (Main.isIsInMultiGame()) {
            Snake.SnakeColor colorPlayerTwo = Snake.SnakeColor.randomColor();
            while (colorPlayerTwo.equals(playerOneSnake.snakeColor)) {
                colorPlayerTwo = Snake.SnakeColor.randomColor();
            }
            playerTwoSnake = new Snake(colorPlayerTwo, this, rows / 3 * 2, cols / 2);
        }

        // Place la pomme à un endroit aléatoire
        food = setNewFood();
    }

    /**
     * Aligne un point sur la grille
     *
     * @param snakeDot Le point dont on veut corriger l'emplacement
     * @return Le point aligné correctement
     */
    public SnakeDot wrap(SnakeDot snakeDot) {
        int x = snakeDot.getX();
        int y = snakeDot.getY();

        if (Settings.WALLS.isActivated()) {
            // Le serpent mourra
            if (x >= rows) x = rows;
            if (y >= cols) y = cols;
            if (x < 0) x = 0;
            if (y < 0) y = 0;
        } else {
            // Le serpent va se téléporter à l'opposé
            if (x >= rows) x = 0;
            if (y >= cols) y = 0;
            if (x < 0) x = rows - 1;
            if (y < 0) y = cols - 1;
        }

        // Recrée et retourne le point avec le bon alignement
        return new SnakeDot(snakeDot.getDotType(), x, y, snakeDot.getColor(), snakeDot.getSprite(), snakeDot.getDirection());
    }

    /**
     * Choisi l'emplacement d'une nouvelle nourriture
     * @return la nourriture tirée au hasard
     */
    private Food setNewFood() {
        Random random = new Random();
        Food randomApple;
        do
            randomApple = new Food(random.nextInt(rows), random.nextInt(cols));
        while (randomApple.getDot().equals(playerOneSnake.getHead()) || (Main.isIsInMultiGame() && randomApple.getDot().equals(playerTwoSnake.getHead())));

        return randomApple;
    }

    /**
     * Méthode appelée à chaque cycle d'éxécution<br>
     * Teste si le serpent mange une pomme<br>
     * - S'il en mange une : fais grandir le serpent et place une pomme<br>
     * - Sinon : déplace le serpent sur la case suivante
     */
    public void update() {
        if (food.getDot().equals(playerOneSnake.getHead())) {
            playerOneSnake.extend();
            food = setNewFood();
        } else
            playerOneSnake.move();

        if (Main.isIsInMultiGame()) {
            if (food.getDot().equals(playerTwoSnake.getHead())) {
                playerTwoSnake.extend();
                food = setNewFood();
            } else
                playerTwoSnake.move();
        }
    }

    // ******************************  GETTER  ****************************** //

    /**
     * @return le serpent du joueur 1
     */
    public Snake getPlayerOneSnake() {
        return playerOneSnake;
    }

    /**
     * @return le serpent du joueur 2
     */
    public Snake getPlayerTwoSnake() {
        return playerTwoSnake;
    }

    /**
     * @return la nourriture du serpent
     */
    public Food getFood() {
        return food;
    }
}
