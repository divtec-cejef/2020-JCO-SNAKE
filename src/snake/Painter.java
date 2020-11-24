package snake;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

// Importation des constantes nécessaires
import static snake.Constants.TEXT_COLOR;
import static snake.Constants.TILE_SIZE;
import static snake.Constants.PATH_TO_IMAGES;
import static snake.Constants.INITIAL_SNAKE_LENGTH;

public class Painter {

    /**
     * Dessine sur la grille
     * @param grid grille sur laquelle on veut dessiner
     * @param gc GraphicsContext
     */
    public static void paint(Grid grid, GraphicsContext gc) {
        // Dessine le fond du jeu
        gc.setFill(Constants.BACKGROUND_COLOR);
        gc.fillRect(0, 0, grid.getWidth(), grid.getHeight());

        // Dessine la nourriture du serpent
        paintImage(grid.getFood().getDot(), Snake.Direction.NONE, gc);

        // Dessine le serpent
        Snake snake = grid.getSnake();
        for (Dot dot: snake.getDots())
            paintImage(dot, snake.getSnakeDirection(), gc);

        // Dessine la tête du serpent lorsqu'il est mort
        if (snake.isDead())
            paintDeadSnakeHead(snake.getHead(), snake.getSnakeDirection(), gc);

        // Dessine le score
        gc.setFill(TEXT_COLOR);
        gc.fillText("Score : " + 100 * (snake.getDots().size() - INITIAL_SNAKE_LENGTH), TILE_SIZE / 2.0f, 15);
    }

    /**
     * Dessine une image sur la grille
     * @param dot Point à dessiner
     * @param direction Orientation du point
     * @param gc GraphicsContext
     */
    private static void paintImage(Dot dot, Snake.Direction direction, GraphicsContext gc) {
        gc.drawImage(getImages(dot.getDotType(), direction, Snake.SNAKE_COLOR),
                    dot.getX() * TILE_SIZE,
                    dot.getY() * TILE_SIZE,
                    TILE_SIZE, TILE_SIZE);
    }

    /**
     * Change la couleur de la tête du serpent lorsqu'il est mort
     * @param snakeHeadDot Point correspondant à la tête du serpent
     * @param snakeDirection Direction de la tête
     * @param gc GraphicsContext
     */
    private static void paintDeadSnakeHead(Dot snakeHeadDot, Snake.Direction snakeDirection, GraphicsContext gc) {
        Snake.SnakeColor deathColor = Snake.SnakeColor.RED;

        if (Snake.SNAKE_COLOR == Snake.SnakeColor.RED)
            deathColor = Snake.SnakeColor.BLUE;

        gc.drawImage(getImages(snakeHeadDot.getDotType(), snakeDirection, deathColor),
                snakeHeadDot.getX() * TILE_SIZE,
                snakeHeadDot.getY() * TILE_SIZE,
                TILE_SIZE, TILE_SIZE);
    }

    /**
     * Affiche le message de fin de jeu
     * @param gc GraphicsContext
     */
    public static void paintResetMessage(GraphicsContext gc) {
        gc.setFill(TEXT_COLOR);
        gc.fillText("Appuyez sur ENTER pour recommencer.", Constants.WIDTH / 3.2f, Constants.HEIGHT / 2.0f);
//        gc.fillText("Appuyez sur ENTER pour recommencer.", TILE_SIZE, Main.HEIGHT - 10);
    }

    /**
     * Récupère les images utilisées pour le jeu
     * @param dotType Le type du point dont on veut l'image
     * @param direction La direction du serpent, pour récupérer l'orientation de son corps
     * @return L'image correspondante aux paramètres fournis
     */
    private static Image getImages(Dot.DotType dotType, Snake.Direction direction, Snake.SnakeColor snakeColor) {
        // Le chemin vers les images du serpent
        String PATH_TO_SNAKE_IMAGES = PATH_TO_IMAGES + snakeColor.toString() + "/";

        if (dotType == Dot.DotType.FOOD || direction == Snake.Direction.NONE)
            return new Image(PATH_TO_IMAGES + "apple.png");

        switch (dotType){
            case HEAD:
                return new Image(PATH_TO_SNAKE_IMAGES + "head_" + direction.toString().toLowerCase() + ".png");
            case TAIL:
                return new Image(PATH_TO_SNAKE_IMAGES + "tail_" + direction.toString().toLowerCase() + ".png");
            case BODY:
            default:
                String orientation;
                if (direction == Snake.Direction.LEFT || direction == Snake.Direction.RIGHT)
                    orientation = "horizontal";
                else
                    orientation = "vertical";

                return new Image(PATH_TO_SNAKE_IMAGES + "body_" + orientation + ".png");
        }
    }
}
