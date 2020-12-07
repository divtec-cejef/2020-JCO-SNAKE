package snake;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.List;

// Importation des constantes
import static snake.Constants.*;

// Importation des enums
import static snake.Snake.Direction;
import static snake.Snake.SnakeColor;

/**
 * Point d'entrée du jeu
 */
public class Main extends Application {

    // Paramètres possibles pour le jeu
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

        public boolean isActivated() {
            return isActivated;
        }

        public void setActivated(boolean activated) {
            isActivated = activated;
        }
    }

    // Couleur des contours de la fenêtre
    private final Color BORDER_COLOR = BACKGROUND_COLOR;

    // Boucle du jeu
    private Timeline timeline;
    // Jeu sur pause
    private boolean paused;

    private boolean isInMenu = true;
    public static boolean isInMultiGame;
    public static boolean hasWalls = false;

    private Grid grid;
    private GraphicsContext context;
    private boolean keyIsPressed;

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

        root.getChildren().add(canvas);

        Scene scene = new Scene(root, BORDER_COLOR);

        scene.setOnKeyPressed(event -> {
            if (isInMenu) {
                switch (event.getCode()) {
                    case LEFT:
                        startGame(false);
                        break;
                    case RIGHT:
                        startGame(true);
                        break;
                    case ESCAPE:
                        stageToClose.close();
                        break;
                }
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

        List<Settings> gameSettings = new ArrayList<>();

        // Affiche le menu de sélection de mode de jeu
//        Painter.paintConfigMenu(gameSettings, context);
        Painter.paintMenu(context);
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
            case ENTER:
                if (isPaused())
                    startGame(isInMultiGame);
                break;
            case Q:
                if (isPaused()) {
                    isInMenu = true;
                    Painter.paintMenu(context);
                }
                break;
            case ESCAPE:
                if (isPaused()) {
                    stageToClose.close();
                }
                break;
            case P:
                paused = !isPaused();
                break;
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
                    pause();
            } else
                Painter.paintPause(context);
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        timeline.setRate(FPS);
    }

    /**
     * Mets le jeu sur pause
     */
    public void pause() {
        paused = true;
        timeline.pause();
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
