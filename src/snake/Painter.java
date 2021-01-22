package snake;

import javafx.scene.canvas.GraphicsContext;
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
    private static final int SEPARATION = 35;

    // Context graphique du canvas de le fenêtre
    private static GraphicsContext graphicsContext;

    /**
     * Initialise le Painter
     * @param gc Context graphique du canvas de la fenêtre
     */
    public static void initPainter(GraphicsContext gc) {
        graphicsContext = gc;
    }

    /**
     * Initiallise la surface de jeu
     */
    private static void initGrid() {
        // Supprime ce qui reste à l'écran
        graphicsContext.setFill(BACKGROUND_COLOR);
        graphicsContext.fillRect(0, 0, WIDTH, HEIGHT);

        // Prépare le format des textes
        graphicsContext.setFill(TEXT_COLOR);
        graphicsContext.setTextAlign(TextAlignment.CENTER);
        graphicsContext.setFont(Font.font("Consolas", 16));
    }

    /**
     * Affiche le menu des paramètres
     */
    private static void paintConfigMenu() {
        initGrid();

        // Retour au menu
        paintTopLeft("[" + GO_BACK_KEY.getName() + "] Retour");

        // Affichage des options
        int count = 0;
        int yLocation = TILE_SIZE * 11;
        for (Settings setting: Settings.getSettingsList()) {
            graphicsContext.fillText(setting.getDisplayName(),TILE_SIZE * 2, yLocation);
            graphicsContext.fillText(Boolean.toString(setting.isActivated()), WIDTH * 0.80f, yLocation);
            count++;
            yLocation = TILE_SIZE * 11 + SEPARATION * count;
        }

        // Sauvegarder les paramètres
        paintTopRight("[" + SAVE_CONFIG_KEY.getName() + "] Sauvegarder et quitter");

        // Modifier un paramètre
        graphicsContext.setTextAlign(TextAlignment.CENTER);
        graphicsContext.fillText("[" + TOGGLE_OPTION_KEY.getName() + "] Modifier l'option", WIDTH * 0.5f, HEIGHT - TILE_SIZE);

        // Titre de la page
        graphicsContext.setFont(Font.font("Consolas", 24));
        graphicsContext.fillText("Config", WIDTH / 2.0f, TILE_SIZE * 6);

    }

    /**
     * Surligne une option du menu
     *
     * @param selectedOption Option seléctionnée
     */
    public static void selectOption(int selectedOption) {
        // Dessine le menu
        paintConfigMenu();

        // Option seléctionnée
        graphicsContext.setTextAlign(TextAlignment.LEFT);
        Settings option = Settings.getFromSettingsList(selectedOption);

        // Emplacement de l'option
        int yLocation = TILE_SIZE * 11 + SEPARATION * selectedOption;

        // Dessine le nom de l'option
        graphicsContext.setFont(Font.font("Consolas", FontWeight.BOLD, 16));
        graphicsContext.setFill(HIGHLIGHTED_TEXT_COLOR);
        graphicsContext.fillText(option.getDisplayName(),TILE_SIZE * 2, yLocation);

        // Dessine la valeur de l'option
        graphicsContext.setFont(Font.font("Consolas", 16));
        if (Settings.getFromSettingsList(selectedOption).isActivated())
            graphicsContext.setFill(HIGHLIGHTED_TRUE_TEXT_COLOR);  // Couleur si l'option est activée
        else
            graphicsContext.setFill(HIGHLIGHTED_FALSE_TEXT_COLOR); // Couleur si l'option est désactivée
        graphicsContext.fillText(Boolean.toString(option.isActivated()), WIDTH * 0.80f, yLocation);
    }

    /**
     * Affiche le menu de sélection de mode de jeu
     */
    public static void paintMenu() {
        initGrid();

        // Quitter le jeu
        paintTopLeft("[" + CLOSE_GAME_KEY.getName() + "] Quitter le jeu");
        graphicsContext.setTextAlign(TextAlignment.CENTER);

        // Sélection des modes de jeu
        graphicsContext.fillText("[<] Solo", WIDTH * 0.31f, HEIGHT * 0.58f);

        graphicsContext.setTextAlign(TextAlignment.RIGHT);
        graphicsContext.fillText("Multi [>]", WIDTH * 0.76f, HEIGHT * 0.58f);

        graphicsContext.setTextAlign(TextAlignment.CENTER);
        graphicsContext.fillText("Config\n[" + CONFIG_GAME_KEY.getName() + "]", WIDTH * 0.5f, HEIGHT * 0.75f);

        // Titre du jeu
        graphicsContext.setFont(Font.font("Consolas", 24));
        graphicsContext.fillText(GAME_NAME, WIDTH / 2.0f, HEIGHT * 0.4);
    }

    /**
     * Affiche le message de fin de jeu
     * @param deadSnake Serpent qui est mort
     */
    public static void paintGameOverMenu(Snake deadSnake) {
        graphicsContext.setTextAlign(TextAlignment.CENTER);
        String deathText = "Les deux serpents sont morts";
        if (deadSnake != null){
            SnakeColor deadSnakeColor = deadSnake.getSnakeColor();
            // Choix du message de fin
            deathText = "Votre score est de " + deadSnake.getScore();

            if (Main.isIsInMultiGame())
                deathText = "Le serpent " + deadSnakeColor.getDisplayName() + " est mort";
        }

        // On dessine le message dans la fenêtre
        graphicsContext.fillText(deathText, WIDTH / 2.0f, HEIGHT * 0.42f);

        // On affiche les commandes
        graphicsContext.fillText("Appuyez sur [" + RESTART_KEY.getName() + "] pour recommencer.", WIDTH / 2.0f, HEIGHT * 0.5f);
        graphicsContext.fillText("[" + CHANGE_GAME_MODE_KEY.getName() + "] pour revenir au menu.", WIDTH / 2.0f, HEIGHT * 0.54f);
        graphicsContext.fillText("[" + CLOSE_GAME_KEY.getName() + "] pour quitter.", WIDTH / 2.0f, HEIGHT * 0.58f);
    }

    /**
     * Affiche le message de pause
     */
    public static void paintPause() {
        paintBottomLeft("[" + CLOSE_GAME_KEY.getName() + "] Quitter le jeu");

        graphicsContext.setTextAlign(TextAlignment.CENTER);
        graphicsContext.fillText("PAUSE", WIDTH / 2.0f, HEIGHT * 0.5f);

        paintCommands();
    }

    /**
     * Dessine sur la grille
     *
     * @param grid grille sur laquelle on veut dessiner
     */
    public static void paintGame(Grid grid) {
        // On enlève tout ce qu'il y avait avant
        initGrid();

        // Dessine la nourriture du serpent
        if (Settings.LEGACY_SNAKE.isActivated() && !Main.isIsInMultiGame()) {
//            graphicsContext.setFill(Color.WHITE);
            graphicsContext.fillRect(grid.getFood().getDot().getX() * TILE_SIZE,
                    grid.getFood().getDot().getY() * TILE_SIZE,
                    TILE_SIZE, TILE_SIZE);
        } else
            paintDot(grid.getFood().getDot());

        // Dessine le serpent du joueur 1
        playerOneSnake = grid.getPlayerOneSnake();

        // On dessine le premier serpent
        for (SnakeDot dot : playerOneSnake.getDots()) {
            if (Settings.LEGACY_SNAKE.isActivated() && !Main.isIsInMultiGame()) {
                graphicsContext.fillRect(dot.getX() * TILE_SIZE,
                        dot.getY() * TILE_SIZE,
                        TILE_SIZE, TILE_SIZE);
            } else {
                paintSnake(dot, playerOnePreviousDot);
                playerOnePreviousDot = dot;
            }
        }

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
            paintCommands();

        // Affiche un message lorsqu'un des serpents est mort
        if (playerOneSnake.isDead())
            paintGameOverMenu(playerOneSnake);

        if (Main.isIsInMultiGame() && playerTwoSnake.isDead())
            paintGameOverMenu(playerTwoSnake);

        // Dessine le score
        paintScore();

        Main.setPlayerOneKeyPressed(false);
        if (Main.isIsInMultiGame())
            Main.setPlayerTwoKeyPressed(false);
    }

    /**
     * Dessine l'image d'un point sur la grille
     * @param dot Point à dessiner
     */
    private static void paintDot(Dot dot) {
        graphicsContext.drawImage(dot.getSprite(),
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

    /**
     * Dessines les commandes du jeu
     */
    private static void paintCommands() {
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

            graphicsContext.fillText("[W]\nHaut", upXPosition, upYPosition);
            graphicsContext.fillText("[A]\nGauche", leftXPosition, leftYPosition);
            graphicsContext.fillText("[S]\nBas", downXPosition, downYPosition);
            graphicsContext.fillText("[D]\nDroite", rightXPosition, rightYPosition);

            // Joueur 1
            difference = - (WIDTH * 0.25f);

            upXPosition = WIDTH * 0.5f + difference;
            leftXPosition = WIDTH * 0.36f + difference;
            downXPosition = WIDTH * 0.5f + difference;
            rightXPosition = WIDTH * 0.65f + difference;
        }

        // Joueur 1
        graphicsContext.fillText("[^]\nHaut", upXPosition, upYPosition);
        graphicsContext.fillText("[<]\nGauche", leftXPosition, leftYPosition);
        graphicsContext.fillText("[v]\nBas", downXPosition, downYPosition);
        graphicsContext.fillText("[>]\nDroite", rightXPosition, rightYPosition);
    }

    /**
     * Dessine le score
     */
    private static void paintScore() {
        String playerOneScoreText = "Score : " + playerOneSnake.getScore();
        if (Main.isIsInMultiGame()) {
            playerOneScoreText = "Serpent " + playerOneSnake.getSnakeColor().getDisplayName() + " : " + playerOneSnake.getScore();
            paintTopRight("Serpent " + playerTwoSnake.getSnakeColor().getDisplayName() + " : " + playerTwoSnake.getScore());
        }

        paintTopLeft(playerOneScoreText);
    }

    /**
     * Ecrit en haut à gauche de la fenêtre
     * @param text Texte à écrire
     */
    private static void paintTopLeft(String text) {
        graphicsContext.setTextAlign(TextAlignment.LEFT);
        graphicsContext.fillText(text, TILE_SIZE, TILE_SIZE * 1.5f);
    }

    /**
     * Ecrit en bas à gauche de la fenêtre
     * @param text Texte à écrire
     */
    private static void paintBottomLeft(String text) {
        graphicsContext.setTextAlign(TextAlignment.LEFT);
        graphicsContext.fillText(text, TILE_SIZE, HEIGHT - TILE_SIZE * 0.5f);
    }

    /**
     * Ecrit en haut à droite de la fenêtre
     * @param text Texte à écrire
     */
    private static void paintTopRight(String text) {
        graphicsContext.setTextAlign(TextAlignment.RIGHT);
        graphicsContext.fillText(text, WIDTH - TILE_SIZE, TILE_SIZE * 1.5f);
    }

}

