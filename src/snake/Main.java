package snake;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Point d'entrée du jeu
 */
public class Main extends Application {

    // Largeur de la fenêtre
    public static final int WIDTH = 500;
    // Hauteur de la fenêtre
    public static final int HEIGHT = 500;
    // Couleur des contours de la fenêtre
    private static final Color BORDER_COLOR = Grid.BACKGROUND_COLOR;
//    private static final Color BORDER_COLOR = Color.WHITE;

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
                        (new Thread(loop)).start();
                    }
            }
        });

        reset();

        root.getChildren().add(canvas);

        Scene scene = new Scene(root, BORDER_COLOR);

        primaryStage.setResizable(false);
        primaryStage.setTitle("Snake");
        primaryStage.setOnCloseRequest(e -> System.exit(0));
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image("/images/icon.png"));
        primaryStage.show();

        (new Thread(loop)).start();
    }

    /**
     * Réinitialise le jeu
     */
    private void reset() {
        grid = new Grid(WIDTH, HEIGHT);
        loop = new SnakeLoop(grid, context);
        Painter.paint(grid, context);
    }
}
