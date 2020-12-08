package snake;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import java.util.*;

// Importation des constantes
import static snake.Constants.*;

// Importation des enums
import static snake.Main.Settings;
import static snake.Snake.Direction;
import static snake.Snake.SnakeColor;

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
     * Affiche le menu des paramètres
     *
     * @param gc GraphicsContext
     */
    public static void paintConfigMenu(List<Settings> gameSettings, GraphicsContext gc) {
        initGrid(gc);

        gc.setTextAlign(TextAlignment.LEFT);
        int count = 0;
        for (Settings setting: gameSettings) {
            gc.fillText(setting.getSettingName(),TILE_SIZE * 2, TILE_SIZE * 10 + count);
            gc.fillText(Boolean.toString(setting.isActivated()), WIDTH * 0.80f, TILE_SIZE * 10 + count);
            count += 35;
        }

        gc.setTextAlign(TextAlignment.CENTER);
        gc.setFont(Font.font("Consolas", 24));
        gc.fillText("Config", WIDTH / 2.0f, TILE_SIZE * 4);

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
        gc.fillText("[" + CLOSE_GAME_KEY.getName() + "] Quitter le jeu", TILE_SIZE * 0.5f, TILE_SIZE * 1.5f);
        gc.setTextAlign(TextAlignment.CENTER);

        // Sélection des modes de jeu
        gc.fillText("[<] Solo", WIDTH * 0.31f, HEIGHT * 0.6f);
        gc.fillText("Multi [>]", WIDTH * 0.65f, HEIGHT * 0.6f);
        gc.fillText("Config\n[v]", WIDTH * 0.5f, HEIGHT * 0.68f);

        // Titre du jeu
        gc.setFont(Font.font("Consolas", 24));
        gc.fillText(GAME_NAME, WIDTH / 2.0f, HEIGHT * 0.4);
    }

    /**
     * Affiche le message de fin de jeu
     *
     * @param gc GraphicsContext
     */
    public static void paintGameOverMenu(GraphicsContext gc, Snake deadSnake) {
        SnakeColor deadSnakeColor = deadSnake.getSnakeColor();

        String deathText = "Vous êtes mort. Votre score est de " + deadSnake.getScore();
        if (Main.isInMultiGame)
            deathText = "Le serpent " + deadSnakeColor.getName() + " est mort";

        gc.fillText(deathText, WIDTH / 2.0f, HEIGHT * 0.42f);
        gc.fillText("Appuyez sur [" + RESTART_KEY.getName() + "] pour recommencer.", WIDTH / 2.0f, HEIGHT * 0.5f);
        gc.fillText("[" + CHANGE_GAME_MODE_KEY.getName() + "] pour changer de mode de jeu.", WIDTH / 2.0f, HEIGHT * 0.54f);
        gc.fillText("[" + CLOSE_GAME_KEY.getName() + "] pour quitter.", WIDTH / 2.0f, HEIGHT * 0.58f);
    }

    /**
     * Affiche le message de pause
     * @param gc GraphicsContext
     */
    public static void paintPause(GraphicsContext gc) {
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText("PAUSE", WIDTH / 2.0f, HEIGHT * 0.5f);
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

        // Dessine le serpent du joueur 1
        playerOneSnake = grid.getPlayerOneSnake();

        SnakeDot previousDot = null;
        for (SnakeDot dot : playerOneSnake.getDots()) {
            paintSnake(dot, previousDot, gc);
            previousDot = dot;
        }

//        int count = 0;
//        for (SnakeDot dot: playerOneSnake.getDots()) {
//            if (dot.getDotType() == Dot.DotType.TAIL)
//                count = 0;
//
//            if (dot.getDirection() == Direction.LEFT || dot.getDirection() == Direction.RIGHT){
//                int un = 1;
//                if (dot.getDirection() == Direction.RIGHT)
//                    un *= -1;
//
//                if (count == 0) {
//                    playerOneSnake.getDots().get(count).setX(playerOneSnake.getHead().getX() - un);
//                } else {
//                    playerOneSnake.getDots().get(count).setX(playerOneSnake.getDots().get(count - 1).getX() - un);
//                }
//            }
//
//            if (dot.getDirection() == Direction.UP || dot.getDirection() == Direction.DOWN){
//                int un = 1;
//                if (dot.getDirection() == Direction.DOWN)
//                    un *= -1;
//
//                if (count == 0) {
//                    playerOneSnake.getDots().get(count).setY(playerOneSnake.getHead().getY() - un);
//                } else {
//                    playerOneSnake.getDots().get(count).setY(playerOneSnake.getDots().get(count - 1).getY() - un);
//                }
//            }
//            count++;
//
//            paintSnake(dot, previousDot, gc);
//            if (dot.getDotType() == Dot.DotType.HEAD)
//                previousDot = dot;
//        }

        // Dessine le serpent du joueur 2
        if (Main.isInMultiGame) {
            playerTwoSnake = grid.getPlayerTwoSnake();

            SnakeDot previousDot2 = null;
            for (SnakeDot dot : playerTwoSnake.getDots()) {
                paintSnake(dot, previousDot2, gc);
                if (dot.getDotType() == Dot.DotType.HEAD)
                    previousDot2 = dot;
            }
        }

        // Affiche un message lorsqu'un serpent est mort
        if (playerOneSnake.isDead())
            paintGameOverMenu(gc, playerOneSnake);

        if (Main.isInMultiGame && playerTwoSnake.isDead())
            paintGameOverMenu(gc, playerTwoSnake);

        // Dessine le score
        float playerOneScoreLocationX = TILE_SIZE * 0.5f;
        float scoreLocationY = TILE_SIZE * 1.5f;
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

    /**
     * Dessine le serpent
     *
     * @param dot           Point où est placé le serpent
     * @param previousDot   Point précédent
     * @param gc            GraphicsContext
     */
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
        SnakeColor deathColor = SnakeColor.RED;

        if (snakeHeadDot.getColor() == SnakeColor.RED)
            deathColor = SnakeColor.BLUE;

        gc.drawImage(getImages(snakeHeadDot, deathColor),
                snakeHeadDot.getX() * TILE_SIZE,
                snakeHeadDot.getY() * TILE_SIZE,
                TILE_SIZE, TILE_SIZE);
    }

    /**
     * Récupère les images utilisées pour le jeu
     *
     * @param dot       Point dont on veut l'image
     * @param bodyColor La couleur des parties du serpent
     * @return L'image correspondante aux paramètres fournis
     */
    private static Sprite getImages(Dot dot, SnakeColor bodyColor) {
        Dot.DotType dotType = dot.getDotType();
        Direction dotDirection = dot.getDirection();

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
                if (dotDirection == Direction.LEFT || dotDirection == Direction.RIGHT)
                    orientation = "horizontal";
                else
                    orientation = "vertical";

                return new Sprite(PATH_TO_SNAKE_IMAGES + "body_" + orientation + ".png");
        }
    }

}
