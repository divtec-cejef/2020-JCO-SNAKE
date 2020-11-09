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

/**
 * Point d'entrée du jeu
 */
public class Main extends Application {

    // Nom du jeu
    private static final String GAME_NAME = "SNAKE";
    public static double INITIAL_TIMELINE_RATE = 6;
    public static double timelineRate = INITIAL_TIMELINE_RATE;
    // Chemin vers l'icône du jeu
    private final String ICON_PATH = "/images/icon.png";

    // Largeur de la fenêtre
    public static final int WIDTH = 500;
    // Hauteur de la fenêtre
    public static final int HEIGHT = 500;
    // Couleur des contours de la fenêtre
    private static final Color BORDER_COLOR = Grid.BACKGROUND_COLOR;

    private Timeline timeline;
    private SnakeLoop loop;
    private Grid grid;
    private GraphicsContext context;

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
            if (loop.isKeyPressed()) {
                return;
            }
            switch (e.getCode()) {
                case UP:
                    snake.setUp();
                    break;
                case DOWN:
                    snake.setDown();
                    break;
                case LEFT:
                    snake.setLeft();
                    break;
                case RIGHT:
                    snake.setRight();
                    break;
                case ENTER:
                    if (loop.isPaused()) {
                        reset();
                    }
            }
        });

        reset();

        root.getChildren().add(canvas);

        Scene scene = new Scene(root, BORDER_COLOR);

        primaryStage.setResizable(false);
        primaryStage.setTitle(GAME_NAME);
        primaryStage.setOnCloseRequest(e -> System.exit(0));
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image(ICON_PATH));
        primaryStage.show();

        update();
    }

    /**
     * Réinitialise le jeu
     */
    private void reset() {
        grid = new Grid(WIDTH, HEIGHT);
        loop = new SnakeLoop(grid, context);
        timelineRate = INITIAL_TIMELINE_RATE;
        update();
    }

    /**
     * Mets à jour le jeu
     */
    private void update() {
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            grid.update();
            Painter.paint(grid, context);

            if (grid.getSnake().isDead()) {
                timeline.pause();
                loop.pause();
                Painter.paintResetMessage(context);
            }

        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        timeline.setRate(timelineRate);
    }
}
