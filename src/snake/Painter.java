package snake;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import static snake.Grid.TILE_SIZE;

public class Painter {

    public static void paint(Grid grid, GraphicsContext gc) {
        // Dessine le fond du jeu
        gc.setFill(Grid.COLOR);
        gc.fillRect(0, 0, grid.getWidth(), grid.getHeight());

        // Dessine la nourriture du serpent
        gc.setFill(Food.FOOD_COLOR);
        paintDot(grid.getFood().getDot(), gc);

        // Dessine le serpent
        Snake snake = grid.getSnake();
        gc.setFill(Snake.SNAKE_COLOR_CODE);
        snake.getDots().forEach(dot -> paintDot(dot, gc));
        if (!snake.isSafe()) {
            gc.setFill(Snake.SNAKE_DEAD_COLOR_CODE);
            paintDot(snake.getHead(), gc);
        }

        // Dessine le score
        gc.setFill(Color.BEIGE);
        gc.fillText("Score : " + 100 * snake.getDots().size(), 10, 490);
    }

    /**
     * Dessine un point dans la fenêtre
     * @param dot Le point à dessiner
     * @param gc GraphicsContext
     */
    private static void paintDot(Dot dot, GraphicsContext gc) {
        gc.fillRect(dot.getX() * TILE_SIZE, dot.getY() * TILE_SIZE, TILE_SIZE, TILE_SIZE);
    }

    /**
     * Affiche le message de fin de jeu
     * @param gc GraphicsContext
     */
    public static void paintResetMessage(GraphicsContext gc) {
        gc.setFill(Color.AQUAMARINE);
        gc.fillText("Appuyez sur ENTER pour recommencer.", 10, 10);
    }
}
