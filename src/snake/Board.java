package snake;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * Classe qui représente un plateau de jeu
 */
public class Board extends JPanel implements ActionListener{
    private enum GameMode {
        SOLO,
        MULTI
    }

    // Taille du plateau
    private final int BOARD_WIDTH = 500;
    private final int BOARD_HEIGHT = 500;
    // Taille d'une case / d'une pomme
    private final int TILE_SIZE = 10;
    // Nombre maximal de cases
    private final int ALL_DOTS = (BOARD_WIDTH*BOARD_HEIGHT)/(TILE_SIZE*TILE_SIZE);
    // Emplacement des composants du serpent
    private final int[] X = new int[ALL_DOTS];
    private final int[] Y = new int[ALL_DOTS];

    private boolean gameHasStarted = false;
    private boolean inMenu = true;
    private boolean restartGame = false;

    private GameMode selectedGameMode;

    private Timer timer;

    private MenuControls menuSelection = new MenuControls();
    private GameOverSelection gameOverSelection = new GameOverSelection();

    Snake serpent;

    private Image apple; // A mettre dans la classe Food
    private int apple_x; // A mettre dans la classe Food
    private int apple_y; // A mettre dans la classe Food

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
        loadImages();
        initGame();
    }

    /**
     * Récupère les images du jeu
     */
    private void loadImages() {
        // A mettre dans la classe Food
        ImageIcon imageIconApple = new ImageIcon("res/images/apple.png");
        apple = imageIconApple.getImage();
    }

    /**
     * Lance le jeu
     * - Crée le serpent
     * - Place une pomme
     * - Lance le timer
     */
    private void initGame() {

        serpent = new Snake(3, Snake.Color.GREEN);

        for (int z = 0; z < serpent.getSnakeLenght(); z++) {
            X[z] = 50 - z * 10;
            Y[z] = 50;
        }

        locateApple();

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
            g.drawImage(apple, apple_x, apple_y, this); // A mettre dans la classe Food

            serpent.draw(g, X, Y, this);

            Toolkit.getDefaultToolkit().sync();

        } else if (!inMenu) {
            gameOver(g);
        }
    }

    private void startGame(Graphics g) {
        String mainTitle = "SNAKE";
        Font font = new Font("Consolas", Font.BOLD, 20);
        FontMetrics metrics = getFontMetrics(font);

        g.setColor(Color.white);
        g.setFont(font);
        g.drawString(mainTitle, (BOARD_WIDTH - metrics.stringWidth(mainTitle)) / 2, BOARD_HEIGHT / 3);

        String solo = "< SOLO";

        g.drawString(solo, (BOARD_WIDTH - metrics.stringWidth(solo)) / 3, (int) (BOARD_HEIGHT / 1.65f));

        String multi = "MULTI >";

        g.drawString(multi, (int) ((BOARD_WIDTH - metrics.stringWidth(multi)) / 1.5f), (int) (BOARD_HEIGHT / 1.65f));

        removeKeyListener(gameOverSelection);

    }

    private void gameOver(Graphics g) {

        addKeyListener(gameOverSelection);

        String msg = "Perdu !";
        String restart = "Appuyez sur ENTER pour rejouer";
        Font font = new Font("Consolas", Font.BOLD, 20);
        FontMetrics metrics = getFontMetrics(font);

        g.setColor(Color.white);
        g.setFont(font);
        g.drawString(msg, (BOARD_WIDTH - metrics.stringWidth(msg)) / 2, (int) (BOARD_HEIGHT / 2.3f));
        g.drawString(restart, (BOARD_WIDTH - metrics.stringWidth(restart)) / 2, (int) (BOARD_HEIGHT / 1.65f));

    }

    /**
     * Vérifie si une pomme se fait manger
     */
    private void checkApple() {
        // A mettre dans la classe Food
        if ((X[0] == apple_x) && (Y[0] == apple_y)) {
            serpent.ateApple();
            timer.setDelay(serpent.getSnakeSpeed());
            locateApple();
        }
    }

    private void checkCollision() {

        // On regarde si le serpent s'est touché lui-même
        for (int z = serpent.getSnakeLenght(); z > 0; z--) {
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
        }
    }

    /**
     * On crée une nouvelle pomme, placée aléatoirement
     */
    private void locateApple() {
        // A mettre dans la classe Food
        // Utilisé pour calculer l'emplacement aléatoire d'une pomme
        int RAND_POS = 50;
        int r = (int) (Math.random() * RAND_POS);
        apple_x = ((r * TILE_SIZE));

        r = (int) (Math.random() * RAND_POS);
        apple_y = ((r * TILE_SIZE));
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (gameHasStarted) {
            checkApple();
            checkCollision();
            serpent.move(TILE_SIZE, X, Y);
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
            }

            if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
                selectedGameMode = GameMode.MULTI;
                inMenu = false;
            }

            System.out.println(selectedGameMode.toString());

        }
    }

    /**
     * Contrôles dans l'écran Game Over
     */
    private class GameOverSelection extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();

            if (key == KeyEvent.VK_ENTER) {
                gameHasStarted = false;
                inMenu = true;
            }
        }
    }

}
