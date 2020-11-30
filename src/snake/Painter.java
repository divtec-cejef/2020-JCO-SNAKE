package snake;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.text.Font;

// Importation des constantes
import static snake.Constants.*;

/**
 * Dessine des éléments sur la grille
 */
public class Painter {

    private static Snake snake;

    /**
     * Initiallise la surface de jeu
     * @param gc GraphicsContext
     */
    private static void initGrid(GraphicsContext gc) {
        // Dessine le fond du jeu
        gc.setFill(BACKGROUND_COLOR);
        gc.fillRect(0, 0, WIDTH, HEIGHT);

        // Prépare le format des textes
        gc.setFill(TEXT_COLOR);
        gc.setFont(Font.font("Consolas", 16));
    }

    /**
     * Affiche le menu de sélection de mode de jeu
     * @param gc GraphicsContext
     */
    public static void paintMenu(GraphicsContext gc) {
        initGrid(gc);

        gc.fillText("< Solo", WIDTH * 0.3f, HEIGHT * 0.5f + 40);
//        gc.fillText("Multi >", WIDTH * 0.6f, HEIGHT * 0.5f + 40);
        gc.setFont(Font.font("Consolas", 24));
        gc.fillText("SNAKE ", WIDTH * 0.43f, 155);
    }

    /**
     * Dessine sur la grille
     * @param grid grille sur laquelle on veut dessiner
     * @param gc   GraphicsContext
     */
    public static void paintGame(Grid grid, GraphicsContext gc) {
        initGrid(gc);

        // Dessine la nourriture du serpent
        paintDot(grid.getFood().getDot(), gc);

        // Dessine le serpent
        snake = grid.getSnake();
        for (Dot dot : snake.getDots())
            paintDot(dot, gc);
//        for (int i = 0; i < snake.getDots().size(); i++) {
//            Dot snakeDot = snake.getDots().get(i);
//
//            if (snakeDot.getDotType() == Dot.DotType.BODY && isCorner(snakeDot.getDirection())) {
//                gc.drawImage(checkForCorner(PATH_TO_IMAGES + Snake.SNAKE_COLOR.toString() + "/", snakeDot.getDirection()),
//                        snakeDot.getX() * TILE_SIZE,
//                        snakeDot.getY() * TILE_SIZE,
//                        TILE_SIZE, TILE_SIZE);
//            } else
//                paintDot(snakeDot, gc);
//        }

        // Dessine la tête du serpent d'une autre couleur lorsqu'il est mort
        if (snake.isDead()) {
            paintDeadSnakeHead(snake.getHead(), gc);
            paintResetMessage(gc);
        }

        // Dessine le score
        gc.fillText("Score : " + snake.getScore(), TILE_SIZE * 0.5f, TILE_SIZE * 1.5f);
    }

    /**
     * Dessine l'image d'un point sur la grille
     * @param dot Point à dessiner
     * @param gc  GraphicsContext
     */
    private static void paintDot(Dot dot, GraphicsContext gc) {
//        gc.drawImage(getImages(dot.getDotType(), dot.getDirection(), Snake.SNAKE_COLOR),
        gc.drawImage(getImages(dot, Snake.SNAKE_COLOR),
                dot.getX() * TILE_SIZE,
                dot.getY() * TILE_SIZE,
                TILE_SIZE, TILE_SIZE);
    }

    /**
     * Change la couleur de la tête du serpent lorsqu'il est mort
     * @param snakeHeadDot   Point correspondant à la tête du serpent
     * @param gc             GraphicsContext
     */
    private static void paintDeadSnakeHead(Dot snakeHeadDot, GraphicsContext gc) {
        Snake.SnakeColor deathColor = Snake.SnakeColor.RED;

        if (Snake.SNAKE_COLOR == Snake.SnakeColor.RED)
            deathColor = Snake.SnakeColor.BLUE;

        gc.drawImage(getImages(snakeHeadDot, deathColor),
                snakeHeadDot.getX() * TILE_SIZE,
                snakeHeadDot.getY() * TILE_SIZE,
                TILE_SIZE, TILE_SIZE);

    }

    /**
     * Affiche le message de fin de jeu
     * @param gc GraphicsContext
     */
    public static void paintResetMessage(GraphicsContext gc) {
        gc.fillText("Appuyez sur [ENTER] pour recommencer.", WIDTH * 0.2f, HEIGHT * 0.5f);
        gc.fillText("[Q] pour changer de mode de jeu.", WIDTH * 0.25f, HEIGHT * 0.54f);
        gc.fillText("[ESC] pour quitter.", WIDTH * 0.35f, HEIGHT * 0.58f);
    }

    /**
     * Récupère les images utilisées pour le jeu
     * @param dot       Point dont on veut l'image
     * @param bodyColor La couleur des parties du serpent
     * @return L'image correspondante aux paramètres fournis
     */
    private static Image getImages(Dot dot, Snake.SnakeColor bodyColor) {
        Dot.DotType dotType = dot.getDotType();
        Snake.Direction dotDirection = dot.getDirection();

        if (dotType == Dot.DotType.FOOD)
            return new Image(PATH_TO_IMAGES + "apple.png");

        // Le chemin vers les images du serpent
        String PATH_TO_SNAKE_IMAGES = PATH_TO_IMAGES + bodyColor.toString() + "/";

        switch (dotType) {
            case HEAD:
                return new Image(PATH_TO_SNAKE_IMAGES + "head_" + dotDirection.toString().toLowerCase() + ".png");
            case TAIL:
                return new Image(PATH_TO_SNAKE_IMAGES + "tail_" + dotDirection.toString().toLowerCase() + ".png");
            case BODY:
            default:
                String orientation;
                if (dotDirection == Snake.Direction.LEFT || dotDirection == Snake.Direction.RIGHT) {
                    orientation = "horizontal";
                } else {
                    orientation = "vertical";
                }

//                if (isCorner(dotDirection))
//                    return checkForCorner(PATH_TO_SNAKE_IMAGES, dotDirection);
//                else
                    return new Image(PATH_TO_SNAKE_IMAGES + "body_" + orientation + ".png");
        }
    }

    /**
     * Vérifie si un angle doit être placé
     * @param bodyDirection Direction du corps du serpent
     * @return {@code true} si il faut placer un angle
     */
    private static boolean isCorner(Snake.Direction bodyDirection) {
        return bodyDirection != snake.getSnakeDirection();
    }

    /**
     * Défini l'image utilisé pour l'angle formé par le corps du serpent
     * @param rootPath Chemin vers le corps du serpent
     * @param bodyDirection Direction du serpent
     * @return L'image correspondant aux paramètres fournis
     */
    private static Image checkForCorner(String rootPath, Snake.Direction bodyDirection) {
        String oldDirection = "";
        if (bodyDirection == Snake.Direction.UP)
            oldDirection = "up";
        else if (bodyDirection == Snake.Direction.DOWN)
            oldDirection = "down";

        String newDirection = "";
        switch (snake.getSnakeDirection()){
            case RIGHT:
                newDirection = "left";
                break;
            case LEFT:
                newDirection = "right";
                break;
        }

        boolean isInvalidDirection = false;
        if (bodyDirection == Snake.Direction.RIGHT && snake.getSnakeDirection() == Snake.Direction.DOWN) {
            oldDirection = "up";
            newDirection = "right";
        }

        if (bodyDirection == Snake.Direction.LEFT && snake.getSnakeDirection() == Snake.Direction.UP) {
            oldDirection = "down";
            newDirection = "left";
        }

        if (bodyDirection == Snake.Direction.RIGHT && snake.getSnakeDirection() == Snake.Direction.UP) {
            oldDirection = "up";
            newDirection = "right";
        }

        if (bodyDirection == Snake.Direction.LEFT && snake.getSnakeDirection() == Snake.Direction.DOWN) {
            oldDirection = "up";
            newDirection = "left";
        }

        // Nom de l'image de l'angle
        String imageName = "corner_" + oldDirection + newDirection.substring(0, 1).toUpperCase() + newDirection.substring(1) + ".png";
        switch (imageName) {
            case "corner_downLeft.png":
            case "corner_downRight.png":
            case "corner_upLeft.png":
            case "corner_upRight.png":
                break;
            default:
                System.out.println(imageName + " : n'existe pas");
                isInvalidDirection = true;
        }

        // Chemin vers l'image
        String imagePath = rootPath + imageName;
//                    System.out.println(imagePath);
        if (!isInvalidDirection)
            return new Image(imagePath);
        return new Image(rootPath + "tail_up.png");

    }
}
