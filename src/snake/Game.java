package snake;

import com.sun.javafx.tk.FontMetrics;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Classe qui représente un écran de jeu
 */
public class Game extends Canvas implements EventHandler<ActionEvent> {
    // Modes de jeu possibles
    public enum GameMode {
        SOLO,
        MULTI
    }

    private static final int BOARD_WIDTH = Main.BOARD_WIDTH;
    private static final int BOARD_HEIGHT = Main.BOARD_HEIGHT;
    // Taille d'une case / d'une pomme
    public static final int TILE_SIZE = 10;
    // Nombre maximal de cases
    private static final int ALL_DOTS = (BOARD_WIDTH * BOARD_HEIGHT) / (TILE_SIZE * TILE_SIZE);
    // Liste toutes les lignes du plateau de jeu
    public static final int[] X = new int[ALL_DOTS];
    // Liste toutes les colonnes du plateau de jeu
    public static final int[] Y = new int[ALL_DOTS];

    // Indique si le jeu a commencé
    private boolean gameHasStarted = false;

    // Indique si le joueur est dans un menu
    private boolean inMenu = true;

    final GraphicsContext graphicsContext;

    // Mode de jeu choisi par le joueur
    private GameMode selectedGameMode;

    // Timer qui gère la vitesse de déplacement du serpent
    private Timeline timer;

    // Contrôles dans les fenêtres
    private final MenuControls menuSelection = new MenuControls();

    // Objets du jeu
    static Snake serpent;
    Food pomme;

    /**
     * Crée un plateau de jeu
     */
    public Game(int w, int h) {
        super(w, h);

//        final Image testImage = new Image("/images/apple.png");

        graphicsContext = this.getGraphicsContext2D();

//        graphicsContext.drawImage(testImage, 50, 50);
//        graphicsContext.fillText(mainTitle, (BOARD_WIDTH / 2), (BOARD_HEIGHT / 3));

        gameMenu(graphicsContext);
//        initGame();
    }

    @Override
    public void handle(ActionEvent event) {
        if (gameHasStarted) {
            pomme.checkApple(serpent, timer);
            checkCollision();
            serpent.move();
        }

    }

    /**
     * Lance le jeu
     * - Crée le serpent et sa nourriture
     * - Place une pomme
     * - Lance le timer
     */
    private void initGame() {

        serpent = new Snake(Snake.Color.GREEN);
        pomme = new Food();

        for (int z = 0; z < serpent.getSnakeLength(); z++) {
            X[z] = 50 - z * TILE_SIZE;
            Y[z] = 50;
        }

        pomme.locateApple();

        timer = new Timeline();
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
        doDrawing(graphicsContext);
//        timer.start();
    }

    /**
     * Dessine les éléments graphiques dans la fenêtre
     * @param g Graphique
     */
    private void doDrawing(GraphicsContext g) {
        gameMenu(g);
//        if (inMenu) {
//            gameMenu(g);
//        }
//
//        if (serpent.isAlive && gameHasStarted) {
//            pomme.draw(g);
//            serpent.draw(g);
//
////            Toolkit.getDefaultToolkit().sync();
//
//        }
    }

    public static Snake getSnake(){
        return serpent;
    }

    /**
     * Menu de sélection de mode de jeu
     * @param g GraphicsContext
     */
    private void gameMenu(GraphicsContext g) {
        String mainTitle = "SNAKE";
        String solo = "< SOLO";
        String multi = "MULTI >";

//        graphicsContext.setFont(Font.font("Retro Gaming", FontWeight.BOLD, FontPosture.REGULAR, 28));
        Font titleFont = Font.font("Retro Gaming", FontWeight.BOLD, FontPosture.REGULAR, 28);
        Font font = Font.font("Retro Gaming", FontWeight.BOLD, FontPosture.REGULAR, 26);

        g.setFont(titleFont);
        g.setFill(Color.WHITE);

//        if (!serpent.isAlive)
//            mainTitle = "Perdu !";

        g.fillText(mainTitle, BOARD_WIDTH / 2.5f, BOARD_HEIGHT / 3.0f);

        g.setFont(font);
        g.fillText(solo, BOARD_WIDTH / 5.0f, BOARD_HEIGHT / 1.6f);
        g.fillText(multi, BOARD_WIDTH / 1.6f, BOARD_HEIGHT / 1.6f);

    }

    /**
     * Vérifie si le serpent est entré en collision avec un mur ou lui-même
     */
    private void checkCollision() {

        // On regarde si le serpent s'est touché lui-même
        for (int z = serpent.getSnakeLength(); z > 0; z--) {
            if ((z > 4) && (X[0] == X[z]) && (Y[0] == Y[z])) {
                serpent.isAlive = false;
                break;
            }
        }

        // On regarde si le serpent a touché un mur
        if (Y[0] >= BOARD_HEIGHT || Y[0] < 0 || X[0] >= BOARD_WIDTH || X[0] < 0) {
            serpent.isAlive = false;
        }

        // Si le serpent a touché un mur ou lui-même, la partie s'arrête
        if (!serpent.isAlive) {
            timer.stop();
            inMenu = true;
            System.out.println("Game Over");
        }
    }

    /**
     * Contrôles du serpent
     */
    private class SoloGameControls extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();

            Snake.Direction snakeDirection = serpent.getSnakeDirection();

            if ((key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) && (snakeDirection != Snake.Direction.RIGHT)) {
                serpent.setSnakeDirection(Snake.Direction.LEFT);
            }

            if ((key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) && (snakeDirection != Snake.Direction.LEFT)) {
                serpent.setSnakeDirection(Snake.Direction.RIGHT);
            }

            if ((key == KeyEvent.VK_UP || key == KeyEvent.VK_W) && (snakeDirection != Snake.Direction.DOWN)) {
                serpent.setSnakeDirection(Snake.Direction.UP);
            }

            if ((key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) && (snakeDirection != Snake.Direction.UP)) {
                serpent.setSnakeDirection(Snake.Direction.DOWN);
            }
        }
    }

    /**
     * Contrôles dans le menu
     */
    private class MenuControls extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();

            if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
                selectedGameMode = GameMode.SOLO;
                inMenu = false;
                gameHasStarted = true;
                if (!serpent.isAlive) {
                    initGame();
                }
            }

            if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
                selectedGameMode = GameMode.MULTI;
                inMenu = false;
            }

        }
    }

}
