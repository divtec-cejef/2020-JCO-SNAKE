package snake;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import static snake.Constants.HEIGHT;
import static snake.Constants.WIDTH;
import static snake.Constants.INITIAL_TIMELINE_RATE;

/**
 * Point d'entrée du jeu
 */
public class Main extends Application {
    // Nom du jeu
    private static final String GAME_NAME = "SNAKE";
    // Chemin vers l'icône du jeu
    private final String ICON_PATH = Constants.PATH_TO_IMAGES + "icon.png";
    // Couleur des contours de la fenêtre
    private static final Color BORDER_COLOR = Constants.BACKGROUND_COLOR;

    private Timeline timeline;
    public static double timelineRate = INITIAL_TIMELINE_RATE;
    private boolean paused;

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
        canvas.setOnKeyPressed(e -> {
            Snake snake = grid.getSnake();
            if (isKeyPressed()) {
                return;
            }

            switch (e.getCode()) {
                case UP:
                    if (!isPaused()) {
                        if (snake.getSnakeDirection() != Snake.Direction.DOWN)
                            snake.setUp();
                    }
                    break;
                case DOWN:
                    if (!isPaused()) {
                        if (snake.getSnakeDirection() != Snake.Direction.UP)
                            snake.setDown();
                    }
                    break;
                case LEFT:
                    if (!isPaused()) {
                        if (snake.getSnakeDirection() != Snake.Direction.RIGHT)
                            snake.setLeft();
                    }
                    break;
                case RIGHT:
                    if (!isPaused()) {
                        if (snake.getSnakeDirection() != Snake.Direction.LEFT)
                            snake.setRight();
                    }
                    break;
                case ENTER:
                    if (isPaused()) {
                        startGame();
                    }
            }
        });

        root.getChildren().add(canvas);

        Scene scene = new Scene(root, BORDER_COLOR);

        primaryStage.setResizable(false);
        primaryStage.setTitle(GAME_NAME);
        primaryStage.setOnCloseRequest(e -> System.exit(0));
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image(ICON_PATH));
        primaryStage.show();

        startGame();

    }

    /**
     * Lance le jeu
     */
    private void startGame() {
        grid = new Grid();
        timelineRate = INITIAL_TIMELINE_RATE;
        paused = false;
        keyIsPressed = false;

        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            grid.update();
            Painter.paint(grid, context);
            timeline.setRate(timelineRate);

            if (grid.getSnake().isDead()) {
                pause();
                Painter.paintResetMessage(context);
            }

        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        timeline.setRate(timelineRate);
    }

    /**
     * Augmente la vitesse du jeu
     * @param rateIncrease Vitesse à ajouter
     */
    public static void addToRate(double rateIncrease) {
        timelineRate += rateIncrease;
    }

//    /**
//     * Change la vitesse du jeu
//     * @param newRate Nouvelle vitesse
//     */
//    public static void setRate(double newRate) {
//        timelineRate = newRate;
//    }

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
