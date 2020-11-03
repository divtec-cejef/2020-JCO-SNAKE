package snake;

import javafx.application.Application;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {

    // Taille du plateau
    public static final int BOARD_WIDTH = 500;
    public static final int BOARD_HEIGHT = 500;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setResizable(false);
        primaryStage.setTitle("Snake");

        BorderPane root = new BorderPane();
        root.getChildren().add(new Game(BOARD_WIDTH, BOARD_HEIGHT));
        Board board = new Board(root, BOARD_WIDTH, BOARD_HEIGHT, Color.BLACK);

        primaryStage.setScene(board);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
