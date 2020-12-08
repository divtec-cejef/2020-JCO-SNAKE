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
import java.util.*;

// Importation des constantes
import static snake.Constants.*;

// Importation des enums
import static snake.Snake.Direction;

/**
 * Point d'entrée du jeu
 */
public class Main extends Application {

    /**
     * Paramètres possibles pour le jeu
     */
    public enum Settings {
        WALLS("Murs autour du plateau", false);

        // Nom en français du paramètre
        private final String settingName;

        // Est-ce que le menu est activé
        private boolean isActivated;

        /**
         * Construit un paramètre
         *
         * @param settingName Nom du paramètre
         */
        Settings(String settingName, boolean isActivated) {
            this.settingName = settingName;
            this.isActivated = isActivated;
        }

        /**
         * @return le nom d'un paramètre
         */
        public String getSettingName() {
            return this.settingName;
        }

        /**
         * @return l'activation d'un paramètre
         */
        public boolean isActivated() {
            return isActivated;
        }

        /**
         * Inverse l'activation d'un paramètre
         */
        public void toggleOption() {
            isActivated = !isActivated;
        }

        // Liste de tous les paramètres
        public static final List<Settings> SETTINGS_LIST = Collections.unmodifiableList(Arrays.asList(values()));

    }

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
    private boolean keyIsPressed;

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
        switch (event.getCode()) {
            // Partie solo
            case LEFT:
                if (!isInSettingsMenu)
                    startGame(false);
                break;
            // Partie multi
            case RIGHT:
                if (!isInSettingsMenu)
                    startGame(true);
                break;
            // Paramètres du jeu
            case DOWN:
                if (!isInSettingsMenu) {
                    selectedOption = 0;
                    Painter.selectOption(selectedOption, context);
                    isInSettingsMenu = true;
                }
                break;
        }
    }

    /**
     * Ecoute les contrôles dans le menu de paramètres
     *
     * @param event Touche appuyée
     */
    private void settingsMenuListener(KeyEvent event) {
        if (event.getCode() == TOGGLE_OPTION_KEY) {
            Settings.SETTINGS_LIST.get(selectedOption).toggleOption();
            Painter.selectOption(selectedOption, context);
        }

        if (event.getCode() == SELECT_OPTION_KEY) {
            if (isShiftKeyPressed){
                if (selectedOption <= 0)
                    selectedOption = Settings.SETTINGS_LIST.size() - 1;
                else
                    selectedOption--;
            } else {
                if (selectedOption >= Settings.SETTINGS_LIST.size() - 1)
                    selectedOption = 0;
                else
                    selectedOption++;
            }
            Painter.selectOption(selectedOption, context);
        }

        if (event.getCode() == KeyCode.SHIFT) {
            isShiftKeyPressed = true;
        }

        // Retour vers le menu
        if (event.getCode() == GO_BACK_KEY) {
            if (isInSettingsMenu) {
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
        if (isKeyPressed())
            return;

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
        if (isKeyPressed())
            return;

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
        keyIsPressed = false;
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

    /**
     * @return si une touche est pressée
     */
    public boolean isKeyPressed() {
        return keyIsPressed;
    }
}
