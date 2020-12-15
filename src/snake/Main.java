package snake;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

// Importation des constantes
import static snake.Constants.*;

// Importation des enums
import static snake.Snake.Direction;
import static snake.GameSettings.Settings;

/**
 * Point d'entrée du jeu
 */
public class Main extends Application {

    // Couleur des contours de la fenêtre
    private final Color BORDER_COLOR = BACKGROUND_COLOR;

    // Boucle du jeu
    private Timeline timeline;
    // Jeu sur pause
    private boolean paused;

    private boolean isInMenu = true;
    private boolean isInSettingsMenu = false;
    private static boolean isInMultiGame;

    public static boolean gameHasStarted = false;

    private Grid grid;
    private GraphicsContext context;

    GameSettings settings;

    // Option seléctionnée dans le menu de paramètres
    private int selectedOption = 0;

    boolean isShiftKeyPressed = false;

    // Utilisé pour la fermeture de la fenêtre
    Stage stageToClose;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        stageToClose = primaryStage;
        StackPane root = new StackPane();
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        context = canvas.getGraphicsContext2D();
        settings = new GameSettings();

        canvas.setFocusTraversable(true);
        canvas.setCursor(Cursor.NONE);

        root.getChildren().add(canvas);

        Scene scene = new Scene(root, BORDER_COLOR);

        scene.setOnKeyReleased(this::onKeyReleasedListener);

        scene.setOnKeyPressed(this::onKeyPressedListener);

        // Paramètres de la fenêtre
        primaryStage.setResizable(false);
        primaryStage.setTitle(GAME_NAME);
        primaryStage.setOnCloseRequest(e -> System.exit(0));
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image(ICON_PATH));

        // Récupère les paramètres depuis le fichier "settings.config"
        settings.getFromFile();

        // Affiche la fenêtre
        primaryStage.show();

        // Affiche le menu de sélection de mode de jeu
        Painter.paintMenu(context);
    }

    private void onKeyReleasedListener(KeyEvent event) {
        if (event.getCode() == KeyCode.SHIFT) {
            isShiftKeyPressed = false;
        }
    }

    private void onKeyPressedListener(KeyEvent event) {
        KeyCode keyCode = event.getCode();
        if (isInMenu) {
            menuListener(keyCode);

            if (isInSettingsMenu)
                settingsMenuListener(keyCode);

            if (keyCode == CLOSE_GAME_KEY)
                stageToClose.close();

        } else {
            playerOneKeyListener(keyCode);
            if (isInMultiGame)
                playerTwoKeyListener(keyCode);
        }
    }

    /**
     * Ecoute les contrôles dans le menu
     *
     * @param keyCode Touche appuyée
     */
    private void menuListener(KeyCode keyCode) {
        if (!isInSettingsMenu) {
            switch (keyCode) {
                // Partie solo
                case LEFT:
                    startGame(false);
                    break;
                // Partie multi
                case RIGHT:
                        startGame(true);
                    break;
            }

            // Paramètres du jeu
            if (keyCode == CONFIG_GAME_KEY) {
                selectedOption = 0;
                Painter.selectOption(selectedOption, context);
                isInSettingsMenu = true;
            }
        }
    }

    /**
     * Ecoute les contrôles dans le menu de paramètres
     *
     * @param keyCode Touche appuyée
     */
    private void settingsMenuListener(KeyCode keyCode) {
        switch (keyCode) {
            case DOWN:
                if (selectedOption >= Settings.getLastSettingsIndex())
                    selectedOption = 0;
                else
                    selectedOption++;
                break;
            case UP:
                if (selectedOption <= 0)
                    selectedOption = Settings.getLastSettingsIndex();
                else
                    selectedOption--;
                break;
        }

        if (keyCode == TOGGLE_OPTION_KEY) {
            Settings.getFromSettingsList(selectedOption).toggleOption();
        }

        if (keyCode == SELECT_OPTION_KEY) {
            if (isShiftKeyPressed){
                if (selectedOption <= 0)
                    selectedOption = Settings.getLastSettingsIndex();
                else
                    selectedOption--;
            } else {
                if (selectedOption >= Settings.getLastSettingsIndex())
                    selectedOption = 0;
                else
                    selectedOption++;
            }
        }

        // Sauvegarde de la configuration
        if (keyCode == SAVE_CONFIG_KEY) {
            settings.writeAllInFile();
            // Retour au menu
            if (isInSettingsMenu) {
                isInSettingsMenu = false;
                selectedOption = 0;
                Painter.paintMenu(context);
            }
        }

        if (keyCode == KeyCode.SHIFT) {
            isShiftKeyPressed = true;
        }

        // Retour vers le menu
        if (keyCode == GO_BACK_KEY) {
            if (isInSettingsMenu) {
                isInSettingsMenu = false;
                selectedOption = 0;
                Painter.paintMenu(context);
            }
        }

        if (isInSettingsMenu)
            Painter.selectOption(selectedOption, context);
    }

    /**
     * Ecoute les contrôles pour le premier joueur
     *
     * @param keyCode Touche appuyée
     */
    private void playerOneKeyListener(KeyCode keyCode) {
        Snake playerOneSnake = grid.getPlayerOneSnake();
        Direction playerOneSnakeDirection = playerOneSnake.getSnakeDirection();

        switch (keyCode) {
            case UP:
                if (!isPaused())
                    if (playerOneSnakeDirection != Direction.DOWN && playerOneSnakeDirection != Direction.UP)
                        playerOneSnake.changeDirection(Direction.UP);
                break;
            case LEFT:
                if (!isPaused())
                    if (playerOneSnakeDirection != Direction.RIGHT && playerOneSnakeDirection != Direction.LEFT)
                        playerOneSnake.changeDirection(Direction.LEFT);
                break;
            case DOWN:
                if (!isPaused())
                    if (playerOneSnakeDirection != Direction.UP && playerOneSnakeDirection != Direction.DOWN)
                        playerOneSnake.changeDirection(Direction.DOWN);
                break;
            case RIGHT:
                if (!isPaused())
                    if (playerOneSnakeDirection != Direction.LEFT && playerOneSnakeDirection != Direction.RIGHT)
                        playerOneSnake.changeDirection(Direction.RIGHT);
                break;
            case P:
                if (gameHasStarted)
                    paused = !isPaused();
                break;
        }

        if (keyCode == RESTART_KEY)
            if (isPaused())
                startGame(isInMultiGame);
        if (keyCode == CHANGE_GAME_MODE_KEY)
            if (isPaused()) {
                isInMenu = true;
                Painter.paintMenu(context);
            }

        if (keyCode == CLOSE_GAME_KEY)
            if (isPaused())
                stageToClose.close();
    }

    /**
     * Ecoute les contrôles pour le deuxième joueur
     *
     * @param keyCode Touche appuyée
     */
    private void playerTwoKeyListener(KeyCode keyCode) {
        Snake playerTwoSnake = grid.getPlayerTwoSnake();
        Direction playerTwoSnakeDirection = playerTwoSnake.getSnakeDirection();

        switch (keyCode) {
            case W:
                if (!isPaused())
                    if (playerTwoSnakeDirection != Direction.DOWN && playerTwoSnakeDirection != Direction.UP)
                        playerTwoSnake.changeDirection(Direction.UP);
                break;
            case A:
                if (!isPaused())
                    if (playerTwoSnakeDirection != Direction.RIGHT && playerTwoSnakeDirection != Direction.LEFT)
                        playerTwoSnake.changeDirection(Direction.LEFT);
                break;
            case S:
                if (!isPaused())
                    if (playerTwoSnakeDirection != Direction.UP && playerTwoSnakeDirection != Direction.DOWN)
                        playerTwoSnake.changeDirection(Direction.DOWN);
                break;
            case D:
                if (!isPaused())
                    if (playerTwoSnakeDirection != Direction.LEFT && playerTwoSnakeDirection != Direction.RIGHT)
                        playerTwoSnake.changeDirection(Direction.RIGHT);
                break;
        }
    }

    /**
     * Initialise le jeu avant de le lancer
     */
    private void initGame() {
        isInMenu = false;
        grid = new Grid();
        paused = false;
    }

    /**
     * Lance une partie
     *
     * @param isMultiGame {@code true} Si la partie est en multijoueur
     */
    private void startGame(boolean isMultiGame) {
        isInMultiGame = isMultiGame;

        initGame();

        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            if (!isPaused()) {
                grid.update();
                Painter.paintGame(grid, context);

                if (grid.getPlayerOneSnake().isDead() || (isMultiGame && grid.getPlayerTwoSnake().isDead()))
                    stopGame();
            } else
                Painter.paintPause(context);
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        timeline.setRate(FPS);
    }

    /**
     * Stop le jeu
     */
    public void stopGame() {
        paused = true;
        timeline.stop();
    }

    /**
     * @return si le jeu est en pause
     */
    public boolean isPaused() {
        return paused;
    }

    public static boolean isIsInMultiGame() {
        return isInMultiGame;
    }
}
