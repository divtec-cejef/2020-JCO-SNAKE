package snake;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import static snake.Grid.TILE_SIZE;

public class Painter {

    public static void paint(Grid grid, GraphicsContext gc) {
        // Dessine le fond du jeu
        gc.setFill(Grid.COLOR);
        gc.fillRect(0, 0, grid.getWidth(), grid.getHeight());

        // Dessine la nourriture du serpent
        paintFood(grid.getFood().getDot(), gc);

        // Dessine le serpent
        Snake snake = grid.getSnake();
        gc.setFill(Snake.SNAKE_COLOR_CODE);
        for (Dot dot: snake.getDots()) {
            paintSnake(dot, gc);
        }

        if (!snake.isSafe()) {
            gc.setFill(Snake.SNAKE_DEAD_COLOR_CODE);
            paintDot(snake.getHead(), gc);
        }

        // Dessine le score
        gc.setFill(Color.BEIGE);
        gc.fillText("Score : " + 100 * (snake.getDots().size() - 2), TILE_SIZE / 2.0f, 15);
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
     * Dessine le serpent sur la grille de jeu
     * @param snakeDot Point du serpent à dessiner
     * @param gc GraphicsContext
     */
    private static void paintSnake(Dot snakeDot, GraphicsContext gc) {
        gc.drawImage(getImages(snakeDot.getDotType()), snakeDot.getX() * TILE_SIZE, snakeDot.getY() * TILE_SIZE, TILE_SIZE, TILE_SIZE);
    }

    /**
     * Dessine la nourriture du serpent sur la grille de jeu
     * @param foodDot Point à dessiner
     * @param gc GraphicsContext
     */
    private static void paintFood(Dot foodDot, GraphicsContext gc) {
        gc.drawImage(getImages(foodDot.getDotType()), foodDot.getX() * TILE_SIZE, foodDot.getY() * TILE_SIZE, TILE_SIZE, TILE_SIZE);

    }

    /**
     * Affiche le message de fin de jeu
     * @param gc GraphicsContext
     */
    public static void paintResetMessage(GraphicsContext gc) {
        gc.setFill(Color.AQUAMARINE);
        gc.fillText("Appuyez sur ENTER pour recommencer.", Main.WIDTH / 3.2f, Main.HEIGHT / 2.0f);
//        gc.fillText("Appuyez sur ENTER pour recommencer.", TILE_SIZE, Main.HEIGHT - 10);
    }

    private static Image getImages(Dot.DotType dotType) {
        switch (dotType){
            case FOOD:
                return new Image("/images/apple.png");
            case HEAD:
//                return new Image("/images/" + "GREEN" + "/head_up.png");
                return new Image("/images/" + Snake.SNAKE_COLOR.toString() + "/head_up.png");
            case TAIL:
//                return new Image("/images/" + "GREEN" + "/tail_up.png");
                return new Image("/images/" + Snake.SNAKE_COLOR.toString() + "/tail_up.png");
            case BODY:
            default:
//                return new Image("/images/" + "GREEN" + "/body_horizontal.png");
                return new Image("/images/" + Snake.SNAKE_COLOR.toString() + "/body_horizontal.png");
        }
    }
}
