package snake;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

// Importation des constantes
import static snake.Constants.*;

/**
 * Dessine des éléments sur la grille
 */
public class Painter {

    private static Snake playerOneSnake;
    private static Snake playerTwoSnake;

    /**
     * Initiallise la surface de jeu
     *
     * @param gc GraphicsContext
     */
    private static void initGrid(GraphicsContext gc) {
        // Dessine le fond du jeu
        gc.setFill(BACKGROUND_COLOR);
        gc.fillRect(0, 0, WIDTH, HEIGHT);

        // Prépare le format des textes
        gc.setFill(TEXT_COLOR);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setFont(Font.font("Consolas", 16));
    }

    /**
     * Affiche le menu de sélection de mode de jeu
     *
     * @param gc GraphicsContext
     */
    public static void paintMenu(GraphicsContext gc) {
        initGrid(gc);

        // Quitter le jeu
        gc.setTextAlign(TextAlignment.LEFT);
        gc.fillText("[ESC] Quitter le jeu", TILE_SIZE * 0.5f, TILE_SIZE * 1.5f);
        gc.setTextAlign(TextAlignment.CENTER);

        // Sélection des modes de jeu
        gc.fillText("< Solo", WIDTH * 0.31f, HEIGHT * 0.6f);
        gc.fillText("Multi >", WIDTH * 0.65f, HEIGHT * 0.6f);

        // Titre du jeu
        gc.setFont(Font.font("Consolas", 24));
        gc.fillText(GAME_NAME, WIDTH / 2.0f, HEIGHT * 0.4);
    }

    /**
     * Dessine sur la grille
     *
     * @param grid grille sur laquelle on veut dessiner
     * @param gc   GraphicsContext
     */
    public static void paintGame(Grid grid, GraphicsContext gc) {
        initGrid(gc);

        // Dessine la nourriture du serpent
        paintDot(grid.getFood().getDot(), gc);

        // Dessine le serpent
        playerOneSnake = grid.getPlayerOneSnake();

        SnakeDot previousDot = null;
        for (SnakeDot dot : playerOneSnake.getDots()) {
            paintSnake(dot, previousDot, gc);
            if (dot.getDotType() == Dot.DotType.HEAD)
                previousDot = dot;
        }

        if (Main.isInMultiGame) {
            playerTwoSnake = grid.getPlayerTwoSnake();

            SnakeDot previousDot2 = null;
            for (SnakeDot dot : playerTwoSnake.getDots()) {
                paintSnake(dot, previousDot2, gc);
                if (dot.getDotType() == Dot.DotType.HEAD)
                    previousDot2 = dot;
            }
        }

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

        // Affiche un message lorsqu'un serpent est mort
        if (playerOneSnake.isDead() || (Main.isInMultiGame && playerTwoSnake.isDead()))
            paintResetMessage(gc);

        float playerOneScoreLocationX = TILE_SIZE * 0.5f;
        float scoreLocationY = TILE_SIZE * 1.5f;

        // Dessine le score
        gc.setTextAlign(TextAlignment.LEFT);
        if (Main.isInMultiGame) {
            gc.fillText("Serpent " + playerOneSnake.getSnakeColor().getName() + " : " + playerOneSnake.getScore(), playerOneScoreLocationX, scoreLocationY);
            gc.setTextAlign(TextAlignment.RIGHT);
            gc.fillText("Serpent " + playerTwoSnake.getSnakeColor().getName() + " : " + playerTwoSnake.getScore(), WIDTH - TILE_SIZE, scoreLocationY);
        } else
            gc.fillText("Score : " + playerOneSnake.getScore(), playerOneScoreLocationX, scoreLocationY);
    }

    /**
     * Dessine l'image d'un point sur la grille
     *
     * @param dot Point à dessiner
     * @param gc  GraphicsContext
     */
    private static void paintDot(Dot dot, GraphicsContext gc) {
        gc.drawImage(dot.getSprite(),
                dot.getX() * TILE_SIZE,
                dot.getY() * TILE_SIZE,
                TILE_SIZE, TILE_SIZE);

    }

    private static void paintSnake(SnakeDot dot, SnakeDot previousDot, GraphicsContext gc) {
        dot.setSnakeSprite(previousDot);
        gc.drawImage(dot.getSprite(),
                dot.getX() * TILE_SIZE,
                dot.getY() * TILE_SIZE,
                TILE_SIZE, TILE_SIZE);

    }

    /**
     * Change la couleur de la tête du serpent lorsqu'il est mort
     *
     * @param snakeHeadDot Point correspondant à la tête du serpent
     * @param gc           GraphicsContext
     */
    private static void paintDeadSnakeHead(SnakeDot snakeHeadDot, GraphicsContext gc) {
        Snake.SnakeColor deathColor = Snake.SnakeColor.RED;

//        if (Snake.SNAKE_COLOR == Snake.SnakeColor.RED)
//            deathColor = Snake.SnakeColor.BLUE;

        gc.drawImage(getImages(snakeHeadDot, deathColor),
                snakeHeadDot.getX() * TILE_SIZE,
                snakeHeadDot.getY() * TILE_SIZE,
                TILE_SIZE, TILE_SIZE);

    }

    /**
     * Affiche le message de fin de jeu
     *
     * @param gc GraphicsContext
     */
    public static void paintResetMessage(GraphicsContext gc) {
        gc.fillText("Appuyez sur [ENTER] pour recommencer.", WIDTH / 2.0f, HEIGHT * 0.5f);
        gc.fillText("[Q] pour changer de mode de jeu.", WIDTH / 2.0f, HEIGHT * 0.54f);
        gc.fillText("[ESC] pour quitter.", WIDTH / 2.0f, HEIGHT * 0.58f);

    }

    /**
     * Récupère les images utilisées pour le jeu
     *
     * @param dot       Point dont on veut l'image
     * @param bodyColor La couleur des parties du serpent
     * @return L'image correspondante aux paramètres fournis
     */
    private static Sprite getImages(Dot dot, Snake.SnakeColor bodyColor) {
        Dot.DotType dotType = dot.getDotType();
        Snake.Direction dotDirection = dot.getDirection();

        if (dotType == Dot.DotType.FOOD)
            return new Sprite(PATH_TO_IMAGES + "apple.png");

        // Le chemin vers les images du serpent
        String PATH_TO_SNAKE_IMAGES = PATH_TO_IMAGES + bodyColor.toString() + "/";

        switch (dotType) {
            case HEAD:
                return new Sprite(PATH_TO_SNAKE_IMAGES + "head_" + dotDirection.toString().toLowerCase() + ".png");
            case TAIL:
                return new Sprite(PATH_TO_SNAKE_IMAGES + "tail_" + dotDirection.toString().toLowerCase() + ".png");
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
                return new Sprite(PATH_TO_SNAKE_IMAGES + "body_" + orientation + ".png");
        }
    }

//    /**
//     * Vérifie si un angle doit être placé
//     * @param bodyDirection Direction du corps du serpent
//     * @return {@code true} si il faut placer un angle
//     */
//    private static boolean isCorner(Snake.Direction bodyDirection) {
//        return bodyDirection != snake.getHead().getDirection();
//    }
//
//    /**
//     * Défini l'image utilisé pour l'angle formé par le corps du serpent
//     * @param imagesPath Chemin vers les images du corps du serpent
//     * @param bodyDirection Direction du serpent
//     * @return L'image correspondant aux paramètres fournis
//     */
//    public static Image checkForCorner(String imagesPath, Snake.Direction bodyDirection) {
//        Snake.Direction snakeDirection = snake.getHead().getDirection();
//
//        String oldDirection = "down";
//        if (bodyDirection == Snake.Direction.UP)
//            oldDirection = "up";
//        else if (bodyDirection == Snake.Direction.DOWN)
//            oldDirection = "down";
//
//        String newDirection = "left";
//        switch (snakeDirection){
//            case RIGHT:
//                newDirection = "left";
//                break;
//            case LEFT:
//                newDirection = "right";
//                break;
//        }
//
//        boolean isValidImage = true;
//        if (bodyDirection == Snake.Direction.RIGHT && snakeDirection == Snake.Direction.DOWN) {
//            oldDirection = "up";
//            newDirection = "right";
//        }
//
//        if (bodyDirection == Snake.Direction.RIGHT && snakeDirection == Snake.Direction.UP) {
//            oldDirection = "down";
//            newDirection = "right";
//        }
//
//        if (bodyDirection == Snake.Direction.LEFT && snakeDirection == Snake.Direction.UP) {
//            oldDirection = "down";
//            newDirection = "left";
//        }
//
//        if (bodyDirection == Snake.Direction.LEFT && snakeDirection == Snake.Direction.DOWN) {
//            oldDirection = "up";
//            newDirection = "left";
//        }
//
//        // Nom de l'image de l'angle
//        String imageName = "corner_" + oldDirection + newDirection.substring(0, 1).toUpperCase() + newDirection.substring(1) + ".png";
//
//        // Vérification de l'existence de l'image
//        switch (imageName) {
//            case "corner_downLeft.png":
//            case "corner_downRight.png":
//            case "corner_upLeft.png":
//            case "corner_upRight.png":
//                break;
//            default:
//                System.out.println(imageName + " : n'existe pas");
//                isValidImage = false;
//        }
//
//        // Chemin vers l'image
//        String spritePath = imagesPath + imageName;
//
//        // Si l'image n'est pas valide, on la remplace par une autre image
//        if (isValidImage)
//            return new Image(spritePath);
//
//        String orientation;
//        if (bodyDirection == Snake.Direction.LEFT || bodyDirection == Snake.Direction.RIGHT) {
//            orientation = "horizontal";
//        } else {
//            orientation = "vertical";
//        }
//
//        return new Image(imagesPath + "body_" + orientation + ".png");
//    }
}
