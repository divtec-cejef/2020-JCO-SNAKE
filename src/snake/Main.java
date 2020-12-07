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

// Importation des constantes
import static snake.Constants.*;

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
    public static boolean isInMultiGame = false;

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
                        isInMultiGame = false;
                        startSoloGame();
                        break;
                    case RIGHT:
                        isInMultiGame = true;
                        startMultiGame();
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

        // Affiche le menu de sélection de mode de jeu
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
        Snake.Direction playerOneSnakeDirection = playerOneSnake.getSnakeDirection();

        switch (event.getCode()) {
            case UP:
                if (!isPaused()) {
                    if (playerOneSnakeDirection != Snake.Direction.DOWN && playerOneSnakeDirection != Snake.Direction.UP)
                        playerOneSnake.setUp();
                }
                break;
            case LEFT:
                if (!isPaused()) {
                    if (playerOneSnakeDirection != Snake.Direction.RIGHT && playerOneSnakeDirection != Snake.Direction.LEFT)
                        playerOneSnake.setLeft();
                }
                break;
            case DOWN:
                if (!isPaused()) {
                    if (playerOneSnakeDirection != Snake.Direction.UP && playerOneSnakeDirection != Snake.Direction.DOWN)
                        playerOneSnake.setDown();
                }
                break;
            case RIGHT:
                if (!isPaused()) {
                    if (playerOneSnakeDirection != Snake.Direction.LEFT && playerOneSnakeDirection != Snake.Direction.RIGHT)
                        playerOneSnake.setRight();
                }
                break;
            case ENTER:
                if (isPaused())
                    if (isInMultiGame)
                        startMultiGame();
                    else {
                        isInMultiGame = false;
                        startSoloGame();
                    }
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
        Snake.Direction playerTwoSnakeDirection = playerTwoSnake.getSnakeDirection();

        switch (event.getCode()) {
            case W:
                if (!isPaused()) {
                    if (playerTwoSnakeDirection != Snake.Direction.DOWN && playerTwoSnakeDirection != Snake.Direction.UP) {
                        playerTwoSnake.setUp();
                    }

                }
                break;
            case A:
                if (!isPaused()) {
                    if (playerTwoSnakeDirection != Snake.Direction.RIGHT && playerTwoSnakeDirection != Snake.Direction.LEFT)
                        playerTwoSnake.setLeft();
                }
                break;
            case S:
                if (!isPaused()) {
                    if (playerTwoSnakeDirection != Snake.Direction.UP && playerTwoSnakeDirection != Snake.Direction.DOWN)
                        playerTwoSnake.setDown();
                }
                break;
            case D:
                if (!isPaused()) {
                    if (playerTwoSnakeDirection != Snake.Direction.LEFT && playerTwoSnakeDirection != Snake.Direction.RIGHT)
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
     * Lance une partie solo
     */
    private void startSoloGame() {
        initGame();
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            if (!isPaused()) {
                grid.update();
                Painter.paintGame(grid, context);

                if (grid.getPlayerOneSnake().isDead())
                    pause();
            } else
                Painter.paintPause(context);
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        timeline.setRate(10);
    }

    /**
     * Lance une partie multijoueur
     */
    private void startMultiGame() {
        initGame();
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            if (!isPaused()) {
                grid.update();
                Painter.paintGame(grid, context);

                if (grid.getPlayerOneSnake().isDead() || grid.getPlayerTwoSnake().isDead())
                    pause();
            } else
                Painter.paintPause(context);
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        timeline.setRate(10);
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
