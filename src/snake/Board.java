package snake;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * Classe qui représente un plateau de jeu
 */
public class Board extends JPanel implements ActionListener{
    public enum GameMode {
        SOLO,
        MULTI
    }

    // Taille du plateau
    public static final int BOARD_WIDTH = 500;
    public static final int BOARD_HEIGHT = 500;
    // Taille d'une case / d'une pomme
    public static final int TILE_SIZE = 10;
    // Nombre maximal de cases
    public static final int ALL_DOTS = (BOARD_WIDTH * BOARD_HEIGHT) / (TILE_SIZE * TILE_SIZE);
    // Liste de tous les points sur le plateau de jeu
    public static final int[] X = new int[ALL_DOTS];
    public static final int[] Y = new int[ALL_DOTS];

    private boolean gameHasStarted = false;
    private boolean inMenu = true;

    // Mode de jeu choisi par le joueur
    private GameMode selectedGameMode;

    // Timer qui gère la vitesse de déplacement du serpent
    private Timer timer;

    // Contrôles dans les fenêtres
    private final MenuControls menuSelection = new MenuControls();
//    private final GameOverSelection gameOverSelection = new GameOverSelection();

    // Objets du jeu
    Snake serpent;
    Food pomme;

    /**
     * Crée un plateau de jeu
     */
    public Board() {
        initBoard();
    }

    /**
     * Initialise le jeu
     */
    private void initBoard() {

        addKeyListener(menuSelection);
        setBackground(Color.black);
        setFocusable(true);

        setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
        initGame();
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

        timer = new Timer(serpent.getSnakeSpeed(), this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);
    }

    private void doDrawing(Graphics g) {
        if (inMenu) {
            startGame(g);
        }

        if (serpent.isAlive && gameHasStarted) {

            removeKeyListener(menuSelection);
            addKeyListener(new SoloGameControls());

            pomme.draw(g, this);
            serpent.draw(g, this);

            Toolkit.getDefaultToolkit().sync();

        }
    }

    private void startGame(Graphics g) {
        String mainTitle = "SNAKE";
        String solo = "< SOLO";
        String multi = "MULTI >";

        Font titleFont = new Font("Consolas", Font.BOLD, 28);
        Font font = new Font("Consolas", Font.BOLD, 20);

        FontMetrics metrics = getFontMetrics(font);
        FontMetrics titleMetrics = getFontMetrics(titleFont);

        g.setColor(Color.white);

        g.setFont(titleFont);

        if (!serpent.isAlive)
            mainTitle = "Perdu !";

        g.drawString(mainTitle, (BOARD_WIDTH - titleMetrics.stringWidth(mainTitle)) / 2, BOARD_HEIGHT / 3);

        g.setFont(font);
        g.drawString(solo, (BOARD_WIDTH - metrics.stringWidth(solo)) / 3, (int) (BOARD_HEIGHT / 1.65f));
        g.drawString(multi, (int) ((BOARD_WIDTH - metrics.stringWidth(multi)) / 1.5f), (int) (BOARD_HEIGHT / 1.65f));

    }

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
            addKeyListener(menuSelection);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (gameHasStarted) {
            pomme.checkApple(serpent, timer);
            checkCollision();
            serpent.move();
        }

        repaint();
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
