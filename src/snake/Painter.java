package snake;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

// Importation des constantes
import static snake.Constants.*;

// Importation des enums
import static snake.GameSettings.Settings;
import static snake.Snake.SnakeColor;

/**
 * Dessine des éléments sur la grille
 */
public class Painter {

    // Serpents
    private static Snake playerOneSnake;
    private static Snake playerTwoSnake;

    // Points précédents
    private static SnakeDot playerOnePreviousDot = null;
    private static SnakeDot playerTwoPreviousDot = null;

    // Séparation entre les lignes
    private static final int separation = 35;

    private static GraphicsContext graphicsContext;

    /**
     * Initiallise la surface de jeu
     *
     * @param gc GraphicsContext
     */
    private static void initGrid(GraphicsContext gc) {
        graphicsContext = gc;
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
    private static void paintConfigMenu(GraphicsContext gc) {
        initGrid(gc);

        // Retour au menu
        paintTopLeft("[" + GO_BACK_KEY.getName() + "] Retour", gc);

        // Affichage des options
        int count = 0;
        int yLocation = TILE_SIZE * 11;
        for (Settings setting: Settings.getSettingsList()) {
            gc.fillText(setting.getSettingName(),TILE_SIZE * 2, yLocation);
            gc.fillText(Boolean.toString(setting.isActivated()), WIDTH * 0.80f, yLocation);
            count++;
            yLocation = TILE_SIZE * 11 + separation * count;
        }

        // Sauvegarder les paramètres
        paintTopRight("[" + SAVE_CONFIG_KEY.getName() + "] Sauvegarder et quitter", gc);

        // Modifier un paramètre
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText("[" + TOGGLE_OPTION_KEY.getName() + "] Modifier l'option", WIDTH * 0.5f, HEIGHT - TILE_SIZE);

        // Titre de la page
        gc.setFont(Font.font("Consolas", 24));
        gc.fillText("Config", WIDTH / 2.0f, TILE_SIZE * 6);

    }

    /**
     * Surligne une option du menu
     *
     * @param selectedOption Option seléctionnée
     * @param gc             GraphicsContext
     */
    public static void selectOption(int selectedOption, GraphicsContext gc) {
        // Dessine le menu
        paintConfigMenu(gc);

        // Option seléctionnée
        gc.setTextAlign(TextAlignment.LEFT);
        Settings option = Settings.getFromSettingsList(selectedOption);

        // Emplacement de l'option
        int yLocation = TILE_SIZE * 11 + separation * selectedOption;

        // Dessine le nom de l'option
        gc.setFont(Font.font("Consolas", FontWeight.BOLD, 16));
        gc.setFill(HIGHLIGHTED_TEXT_COLOR);
        gc.fillText(option.getSettingName(),TILE_SIZE * 2, yLocation);

        // Dessine la valeur de l'option
        gc.setFont(Font.font("Consolas", 16));
        if (Settings.getFromSettingsList(selectedOption).isActivated())
            gc.setFill(HIGHLIGHTED_TRUE_TEXT_COLOR);  // Couleur si l'option est activée
        else
            gc.setFill(HIGHLIGHTED_FALSE_TEXT_COLOR); // Couleur si l'option est désactivée
        gc.fillText(Boolean.toString(option.isActivated()), WIDTH * 0.80f, yLocation);
    }

    /**
     * Affiche le menu de sélection de mode de jeu
     *
     * @param gc GraphicsContext
     */
    public static void paintMenu(GraphicsContext gc) {
        initGrid(gc);

        // Quitter le jeu
        paintTopLeft("[" + CLOSE_GAME_KEY.getName() + "] Quitter le jeu", gc);
        gc.setTextAlign(TextAlignment.CENTER);

        // Sélection des modes de jeu
        gc.fillText("[<] Solo", WIDTH * 0.31f, HEIGHT * 0.58f);

        gc.setTextAlign(TextAlignment.RIGHT);
        gc.fillText("Multi [>]", WIDTH * 0.76f, HEIGHT * 0.58f);

        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText("Config\n[" + CONFIG_GAME_KEY.getName() + "]", WIDTH * 0.5f, HEIGHT * 0.75f);

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

        // Choix du message de fin
        String deathText = "Votre score est de " + deadSnake.getScore();
        if (Main.isIsInMultiGame())
            deathText = "Le serpent " + deadSnakeColor.getName() + " est mort";

        // On dessine le message dans la fenêtre
        gc.fillText(deathText, WIDTH / 2.0f, HEIGHT * 0.42f);

        // On affiche les commandes
        gc.fillText("Appuyez sur [" + RESTART_KEY.getName() + "] pour recommencer.", WIDTH / 2.0f, HEIGHT * 0.5f);
        gc.fillText("[" + CHANGE_GAME_MODE_KEY.getName() + "] pour changer de mode de jeu.", WIDTH / 2.0f, HEIGHT * 0.54f);
        gc.fillText("[" + CLOSE_GAME_KEY.getName() + "] pour quitter.", WIDTH / 2.0f, HEIGHT * 0.58f);
    }

    /**
     * Affiche le message de pause
     * @param gc GraphicsContext
     */
    public static void paintPause(GraphicsContext gc) {
        paintBottomLeft("[" + CLOSE_GAME_KEY.getName() + "] Quitter le jeu", gc);

        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText("PAUSE", WIDTH / 2.0f, HEIGHT * 0.5f);

        paintCommands(gc);
    }

    /**
     * Dessine sur la grille
     *
     * @param grid grille sur laquelle on veut dessiner
     * @param gc   GraphicsContext
     */
    public static void paintGame(Grid grid, GraphicsContext gc) {
        // On enlève tout ce qu'il y avait avant
        initGrid(gc);

        // Dessine la nourriture du serpent
        paintDot(grid.getFood().getDot(), gc);

        // Dessine le serpent du joueur 1
        playerOneSnake = grid.getPlayerOneSnake();

        // On dessine le premier serpent
        for (SnakeDot dot : playerOneSnake.getDots()) {
            paintSnake(dot, playerOnePreviousDot);
            playerOnePreviousDot = dot;
            if (playerOneSnake.getHead().getDirection() != dot.getDirection())
                paintDot(HIGHLIGHTED_FALSE_TEXT_COLOR, TILE_SIZE * dot.getX(), TILE_SIZE * dot.getY());
//            paintDot(HIGHLIGHTED_TEXT_COLOR, TILE_SIZE * playerOnePreviousDot.getX(), TILE_SIZE *  playerOnePreviousDot.getY());
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
//            paintSnake(dot, playerOnePreviousDot, gc);
//            if (dot.getDotType() == Dot.DotType.HEAD)
//                playerOnePreviousDot = dot;
//        }

        // Dessine le serpent du joueur 2
        if (Main.isIsInMultiGame()) {
            // On dessine le deuxième serpent
            playerTwoSnake = grid.getPlayerTwoSnake();

            for (SnakeDot dot : playerTwoSnake.getDots()) {
                paintSnake(dot, playerTwoPreviousDot);
                playerTwoPreviousDot = dot;
            }
        }

        // Avant que la partie ne commence, on affiche les commandes
        if (!playerOneSnake.isMoving() && !Main.isIsInMultiGame() || (!playerOneSnake.isMoving() && Main.isIsInMultiGame() && !playerTwoSnake.isMoving()))
            paintCommands(gc);

        // Affiche un message lorsqu'un des serpents est mort
        if (playerOneSnake.isDead())
            paintGameOverMenu(gc, playerOneSnake);

        if (Main.isIsInMultiGame() && playerTwoSnake.isDead())
            paintGameOverMenu(gc, playerTwoSnake);

        // Dessine le score
        paintScore(gc);
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
     */
    public static void paintSnake(SnakeDot dot, SnakeDot previousDot) {
        dot.setSnakeSprite(previousDot);
        graphicsContext.drawImage(dot.getSprite(),
                dot.getX() * TILE_SIZE,
                dot.getY() * TILE_SIZE,
                TILE_SIZE, TILE_SIZE);
    }

    public static void paintSnake(SnakeDot dot) {
        graphicsContext.drawImage(dot.getSprite(),
                dot.getX() * TILE_SIZE,
                dot.getY() * TILE_SIZE,
                TILE_SIZE, TILE_SIZE);
    }

    /**
     * Dessines les commandes du jeu
     * @param gc GraphicsContext
     */
    private static void paintCommands(GraphicsContext gc) {
        // Différence d'emplacement sur l'écran
        float difference;

        // Coordonnées pour le placement au centre
        float upXPosition = WIDTH * 0.5f;
        float leftXPosition = WIDTH * 0.36f;
        float downXPosition = WIDTH * 0.5f;
        float rightXPosition = WIDTH * 0.65f;

        float upYPosition = HEIGHT * 0.7f;
        float leftYPosition = HEIGHT * 0.75f;
        float downYPosition = HEIGHT * 0.8f;
        float rightYPosition = HEIGHT * 0.75f;

        if (Main.isIsInMultiGame()) {
            // Joueur 2
            difference = WIDTH * 0.25f;

            upXPosition = WIDTH * 0.5f + difference;
            leftXPosition = WIDTH * 0.36f + difference;
            downXPosition = WIDTH * 0.5f + difference;
            rightXPosition = WIDTH * 0.65f + difference;

            gc.fillText("[W]\nHaut", upXPosition, upYPosition);
            gc.fillText("[A]\nGauche", leftXPosition, leftYPosition);
            gc.fillText("[S]\nBas", downXPosition, downYPosition);
            gc.fillText("[D]\nDroite", rightXPosition, rightYPosition);

            // Joueur 1
            difference = - (WIDTH * 0.25f);

            upXPosition = WIDTH * 0.5f + difference;
            leftXPosition = WIDTH * 0.36f + difference;
            downXPosition = WIDTH * 0.5f + difference;
            rightXPosition = WIDTH * 0.65f + difference;
        }

        // Joueur 1
        gc.fillText("[^]\nHaut", upXPosition, upYPosition);
        gc.fillText("[<]\nGauche", leftXPosition, leftYPosition);
        gc.fillText("[v]\nBas", downXPosition, downYPosition);
        gc.fillText("[>]\nDroite", rightXPosition, rightYPosition);
    }

    /**
     * Dessine le score
     * @param gc GraphicsContext
     */
    private static void paintScore(GraphicsContext gc) {
        String playerOneScoreText = "Score : " + playerOneSnake.getScore();
        if (Main.isIsInMultiGame()) {
            playerOneScoreText = "Serpent " + playerOneSnake.getSnakeColor().getName() + " : " + playerOneSnake.getScore();
            paintTopRight("Serpent " + playerTwoSnake.getSnakeColor().getName() + " : " + playerTwoSnake.getScore(), gc);
        }

        paintTopLeft(playerOneScoreText, gc);
    }

    /**
     * Ecrit en haut à gauche de la fenêtre
     * @param text Texte à écrire
     * @param gc GraphicsContext
     */
    private static void paintTopLeft(String text, GraphicsContext gc) {
        gc.setTextAlign(TextAlignment.LEFT);
        gc.fillText(text, 0, TILE_SIZE * 1.5f);
    }

    /**
     * Ecrit en bas à gauche de la fenêtre
     * @param text Texte à écrire
     * @param gc GraphicsContext
     */
    private static void paintBottomLeft(String text, GraphicsContext gc) {
        gc.setTextAlign(TextAlignment.LEFT);
        gc.fillText(text, 0, HEIGHT - TILE_SIZE * 0.5f);
    }

    /**
     * Ecrit en haut à droite de la fenêtre
     * @param text Texte à écrire
     * @param gc GraphicsContext
     */
    private static void paintTopRight(String text, GraphicsContext gc) {
        gc.setTextAlign(TextAlignment.RIGHT);
        gc.fillText(text, WIDTH, TILE_SIZE * 1.5f);
    }

    public static void paintDot(Color color, int x, int y) {
        graphicsContext.setFill(color);
        graphicsContext.fillRect(x, y, TILE_SIZE, TILE_SIZE);
    }
}
