package snake;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
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
    // Chemin vers l'icône du jeu
    private final String ICON_PATH = PATH_TO_IMAGES + "icon.png";
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

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        StackPane root = new StackPane();
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        context = canvas.getGraphicsContext2D();

        canvas.setFocusTraversable(true);

        root.getChildren().add(canvas);

        Scene scene = new Scene(root, BORDER_COLOR);

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (isInMenu) {
                    switch (event.getCode()) {
                        case LEFT:
                            startSoloGame();
                            break;
                        case RIGHT:
                            isInMultiGame = true;
                            startMultiGame();
                            break;
                    }

                } else {
                    playerOneKeyListener(event);

                    if (isInMultiGame)
                        playerTwoKeyListener(event);
                }
//                    Snake snake = grid.getPlayerOneSnake();
//                    Snake.Direction snakeDirection = snake.getSnakeDirection();
//
//                    Snake snake2 = null;
//                    if (isInMultiGame)
//                        snake2 = grid.getPlayerTwoSnake();
//
//                    Snake.Direction snakeDirection2 = Snake.Direction.NONE;
//                    if (isInMultiGame)
//                        snakeDirection2 = snake2.getSnakeDirection();
//
//                    if (isKeyPressed()) {
//                        return;
//                    }
//
//                    switch (event.getCode()) {
//                        case W:
//                            if (isInMultiGame && !isPaused()) {
//                                if (snakeDirection2 != Snake.Direction.DOWN && snakeDirection2 != Snake.Direction.UP) {
//                                    snake2.setUp();
//                                }
//                            }
//                            break;
//                        case S:
//                            if (isInMultiGame && !isPaused()) {
//                                if (snakeDirection2 != Snake.Direction.UP && snakeDirection2 != Snake.Direction.DOWN)
//                                    snake2.setDown();
//                            }
//                            break;
//                        case A:
//                            if (isInMultiGame && !isPaused()) {
//                                if (snakeDirection2 != Snake.Direction.RIGHT && snakeDirection2 != Snake.Direction.LEFT)
//                                    snake2.setLeft();
//                            }
//                            break;
//                        case D:
//                            if (isInMultiGame && !isPaused()) {
//                                if (snakeDirection2 != Snake.Direction.LEFT && snakeDirection2 != Snake.Direction.RIGHT)
//                                    snake2.setRight();
//                            }
//                            break;
//                        case UP:
//                            if (!isPaused()) {
//                                if (snakeDirection != Snake.Direction.DOWN && snakeDirection != Snake.Direction.UP)
//                                    snake.setUp();
//                            }
//                            break;
//                        case DOWN:
//                            if (!isPaused()) {
//                                if (snakeDirection != Snake.Direction.UP && snakeDirection != Snake.Direction.DOWN)
//                                    snake.setDown();
//                            }
//                            break;
//                        case LEFT:
//                            if (!isPaused()) {
//                                if (snakeDirection != Snake.Direction.RIGHT && snakeDirection != Snake.Direction.LEFT)
//                                    snake.setLeft();
//                            }
//                            break;
//                        case RIGHT:
//                            if (!isPaused()) {
//                                if (snakeDirection != Snake.Direction.LEFT && snakeDirection != Snake.Direction.RIGHT)
//                                    snake.setRight();
//                            }
//                            break;
//                        case ENTER:
//                            if (isPaused())
//                                startSoloGame();
//                            break;
//                        case Q:
//                            if (isPaused()) {
//                                isInMenu = true;
//                                Painter.paintMenu(context);
//                            }
//                            break;
//                        case ESCAPE:
//                            if (isPaused()) {
//                                primaryStage.close();
//                            }
//                            break;
//                    }
//                }
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

    private void playerOneKeyListener(KeyEvent event) {
        Snake snake = grid.getPlayerOneSnake();
        Snake.Direction snakeDirection = snake.getSnakeDirection();

        switch (event.getCode()){
            case UP:
                if (!isPaused()) {
                    if (snakeDirection != Snake.Direction.DOWN && snakeDirection != Snake.Direction.UP)
                        snake.setUp();
                }
                break;
            case DOWN:
                if (!isPaused()) {
                    if (snakeDirection != Snake.Direction.UP && snakeDirection != Snake.Direction.DOWN)
                        snake.setDown();
                }
                break;
            case LEFT:
                if (!isPaused()) {
                    if (snakeDirection != Snake.Direction.RIGHT && snakeDirection != Snake.Direction.LEFT)
                        snake.setLeft();
                }
                break;
            case RIGHT:
                if (!isPaused()) {
                    if (snakeDirection != Snake.Direction.LEFT && snakeDirection != Snake.Direction.RIGHT)
                        snake.setRight();
                }
                break;
        }
    }

    private void playerTwoKeyListener(KeyEvent event) {
        Snake snake2 = grid.getPlayerTwoSnake();
        Snake.Direction snakeDirection2 = snake2.getSnakeDirection();

        if (isKeyPressed()) {
            return;
        }

        switch (event.getCode()) {
            case W:
                if (!isPaused()) {
                    if (snakeDirection2 != Snake.Direction.DOWN && snakeDirection2 != Snake.Direction.UP) {
                        snake2.setUp();
                    }

                }
                break;
            case S:
                if (!isPaused()) {
                    if (snakeDirection2 != Snake.Direction.UP && snakeDirection2 != Snake.Direction.DOWN)
                        snake2.setDown();
                }
                break;
            case A:
                if (!isPaused()) {
                    if (snakeDirection2 != Snake.Direction.RIGHT && snakeDirection2 != Snake.Direction.LEFT)
                        snake2.setLeft();
                }
                break;
            case D:
                if (!isPaused()) {
                    if (snakeDirection2 != Snake.Direction.LEFT && snakeDirection2 != Snake.Direction.RIGHT)
                        snake2.setRight();
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
        Snake playerOneSnake = grid.getPlayerOneSnake();

        // Choisi une couleur aléatoire pour le serpent
        playerOneSnake.SNAKE_COLOR = Snake.SnakeColor.randomColor();

        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            grid.update();
            Painter.paintGame(grid, context);

            if (grid.getPlayerOneSnake().isDead())
                pause();
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        timeline.setDelay(new Duration(10.0));
        timeline.setRate(2);
    }

    /**
     * Lance une partie multijoueur
     */
    private void startMultiGame() {
        initGame();
        Snake playerOneSnake = grid.getPlayerOneSnake();
        Snake playerTwoSnake = grid.getPlayerTwoSnake();

        // Donne une couleur aux serpents
        playerOneSnake.SNAKE_COLOR = Snake.SnakeColor.randomColor();

        Snake.SnakeColor colorPlayerTwo = Snake.SnakeColor.randomColor();
        while (playerOneSnake.SNAKE_COLOR == colorPlayerTwo)
            colorPlayerTwo = Snake.SnakeColor.randomColor();

        playerTwoSnake.SNAKE_COLOR = colorPlayerTwo;

        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            grid.update();
            Painter.paintGame(grid, context);

            if (grid.getPlayerOneSnake().isDead() || grid.getPlayerTwoSnake().isDead())
                pause();
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        timeline.setDelay(new Duration(10.0));
        timeline.setRate(2);
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
