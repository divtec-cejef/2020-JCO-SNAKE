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
    public static boolean isInMultiGame;

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

        scene.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.SHIFT) {
                isShiftKeyPressed = false;
            }
        });

        scene.setOnKeyPressed(event -> {
            if (isInMenu) {
                menuListener(event);

                if (isInSettingsMenu)
                    settingsMenuListener(event);

                if (event.getCode() == CLOSE_GAME_KEY)
                    stageToClose.close();

            } else {
                playerOneKeyListener(event);
                if (isInMultiGame)
                    playerTwoKeyListener(event);
            }
        });

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

    /**
     * Ecoute les contrôles dans le menu
     *
     * @param event Touche appuyée
     */
    private void menuListener(KeyEvent event) {
        if (!isInSettingsMenu) {
            switch (event.getCode()) {
                // Partie solo
                case LEFT:
                    startGame(false);
                    break;
                // Partie multi
                case RIGHT:
                        startGame(true);
                    break;
                // Paramètres du jeu
                case DOWN:
                    selectedOption = 0;
                    Painter.selectOption(selectedOption, context);
                    isInSettingsMenu = true;
                    break;
            }
        }
    }

    /**
     * Ecoute les contrôles dans le menu de paramètres
     *
     * @param event Touche appuyée
     */
    private void settingsMenuListener(KeyEvent event) {
        switch (event.getCode()) {
            case DOWN:
                if (selectedOption >= Settings.getSettingsList().size() - 1)
                    selectedOption = 0;
                else
                    selectedOption++;
                break;
            case UP:
                if (selectedOption <= 0)
                    selectedOption = Settings.getSettingsList().size() - 1;
                else
                    selectedOption--;
                break;
        }

        if (event.getCode() == TOGGLE_OPTION_KEY) {
            Settings.getFromSettingsList(selectedOption).toggleOption();
        }

        if (event.getCode() == SELECT_OPTION_KEY) {
            if (isShiftKeyPressed){
                if (selectedOption <= 0)
                    selectedOption = Settings.getSettingsList().size() - 1;
                else
                    selectedOption--;
            } else {
                if (selectedOption >= Settings.getSettingsList().size() - 1)
                    selectedOption = 0;
                else
                    selectedOption++;
            }
        }

        if (event.getCode() == KeyCode.SHIFT) {
            isShiftKeyPressed = true;
        }

        Painter.selectOption(selectedOption, context);

        // Retour vers le menu
        if (event.getCode() == GO_BACK_KEY) {
            if (isInSettingsMenu) {
                settings.writeAllInFile();
                Painter.paintMenu(context);
                isInSettingsMenu = false;
            }
        }

    }

    /**
     * Ecoute les contrôles pour le premier joueur
     *
     * @param event Touche appuyée
     */
    private void playerOneKeyListener(KeyEvent event) {

        Snake playerOneSnake = grid.getPlayerOneSnake();
        Direction playerOneSnakeDirection = playerOneSnake.getSnakeDirection();

        switch (event.getCode()) {
            case UP:
                if (!isPaused()) {
                    if (playerOneSnakeDirection != Direction.DOWN && playerOneSnakeDirection != Direction.UP)
                        playerOneSnake.setUp();
                }
                break;
            case LEFT:
                if (!isPaused()) {
                    if (playerOneSnakeDirection != Direction.RIGHT && playerOneSnakeDirection != Direction.LEFT)
                        playerOneSnake.setLeft();
                }
                break;
            case DOWN:
                if (!isPaused()) {
                    if (playerOneSnakeDirection != Direction.UP && playerOneSnakeDirection != Direction.DOWN)
                        playerOneSnake.setDown();
                }
                break;
            case RIGHT:
                if (!isPaused()) {
                    if (playerOneSnakeDirection != Direction.LEFT && playerOneSnakeDirection != Direction.RIGHT)
                        playerOneSnake.setRight();
                }
                break;
            case P:
                paused = !isPaused();
                break;
        }
        if (event.getCode() == RESTART_KEY)
            if (isPaused())
                startGame(isInMultiGame);
        if (event.getCode() == CHANGE_GAME_MODE_KEY)
            if (isPaused()) {
                isInMenu = true;
                Painter.paintMenu(context);
            }

        if (event.getCode() == CLOSE_GAME_KEY)
            if (isPaused()) {
                stageToClose.close();
            }
    }

    /**
     * Ecoute les contrôles pour le deuxième joueur
     *
     * @param event Touche appuyée
     */
    private void playerTwoKeyListener(KeyEvent event) {
        Snake playerTwoSnake = grid.getPlayerTwoSnake();
        Direction playerTwoSnakeDirection = playerTwoSnake.getSnakeDirection();

        switch (event.getCode()) {
            case W:
                if (!isPaused()) {
                    if (playerTwoSnakeDirection != Direction.DOWN && playerTwoSnakeDirection != Direction.UP) {
                        playerTwoSnake.setUp();
                    }
                }
                break;
            case A:
                if (!isPaused()) {
                    if (playerTwoSnakeDirection != Direction.RIGHT && playerTwoSnakeDirection != Direction.LEFT)
                        playerTwoSnake.setLeft();
                }
                break;
            case S:
                if (!isPaused()) {
                    if (playerTwoSnakeDirection != Direction.UP && playerTwoSnakeDirection != Direction.DOWN)
                        playerTwoSnake.setDown();
                }
                break;
            case D:
                if (!isPaused()) {
                    if (playerTwoSnakeDirection != Direction.LEFT && playerTwoSnakeDirection != Direction.RIGHT)
                        playerTwoSnake.setRight();
                }
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
}
