package sample;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Board extends JPanel implements ActionListener{
    /*              Constantes           */
    // Taille du plateau
    private final int BOARD_WIDTH = 500;
    private final int BOARD_HEIGHT = 500;
    // Taille d'une case / d'une pomme
    private final int TILE_SIZE = 10;
    // Nombre maximal de cases
    private final int ALL_DOTS = (BOARD_WIDTH*BOARD_HEIGHT)/(TILE_SIZE*TILE_SIZE);


    // Vitesse du serpent
    private int snakeSpeed = 140;
    // Emplacement des composants du serpent
    private final int[] x = new int[ALL_DOTS];
    private final int[] y = new int[ALL_DOTS];

    private int snakeDots;
    private int apple_x;
    private int apple_y;

    private boolean leftDirection = false;
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;
    private boolean inSoloGame = false;
    private boolean inMultiGame = false;
    private boolean inMenu = true;
    private boolean restartGame = false;

    private Timer timer;

    // Images du jeu
    private Image body;
    private Image apple;
    private Image head;
    private Image tail;

    MenuControls menuSelection = new MenuControls();
    GameOverSelection gameOverSelection = new GameOverSelection();


    /**
     * Crée un plateau de jeu
     */
    public Board() {

        initBoard();
    }

    /**
     *
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

        ImageIcon imageIconBody = new ImageIcon("res/images/snake_body_horizontal_x10.png");
        body = imageIconBody.getImage();

        ImageIcon imageIconApple = new ImageIcon("res/images/snake_food_x10.png");
        apple = imageIconApple.getImage();

        ImageIcon imageIconHead = new ImageIcon("res/images/snake_head_right_x10.png");
        head = imageIconHead.getImage();

        ImageIcon imageIconTail = new ImageIcon("res/images/snake_tail_right_x10.png");
        tail = imageIconTail.getImage();
    }

    /**
     * Lance le jeu
     * - Crée le serpent
     * - Place une pomme
     * - Lance le timer
     */
    private void initGame() {

        snakeDots = 30;

        for (int z = 0; z < snakeDots; z++) {
            x[z] = 50 - z * 10;
            y[z] = 50;
        }

        locateApple();

        timer = new Timer(snakeSpeed, this);
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

        if (inSoloGame) {

            removeKeyListener(menuSelection);
            addKeyListener(new SoloGameControls());
            g.drawImage(apple, apple_x, apple_y, this);

            for (int z = 0; z < snakeDots; z++) {
                if (z == 0) {
                    g.drawImage(head, x[z], y[z], this);
                } else if (z == snakeDots - 1) {
                    g.drawImage(tail, x[z], y[z], this);
                } else {
                    g.drawImage(body, x[z], y[z], this);
                }
            }

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

        while (restartGame) {
            doDrawing(g);
        }
    }

    /**
     * Vérifie si une pomme se fait manger
     */
    private void checkApple() {

        if ((x[0] == apple_x) && (y[0] == apple_y)) {
            snakeDots++;
            if (snakeSpeed > 10)
                snakeSpeed -= 10;
            timer.setDelay(snakeSpeed);
            System.out.println(snakeSpeed);
            locateApple();
        }
    }

    private void move() {

        for (int z = snakeDots; z > 0; z--) {
            x[z] = x[(z - 1)];
            y[z] = y[(z - 1)];
        }

        ImageIcon imageIconHead = new ImageIcon("res/images/snake_head_right_x10.png");
        ImageIcon imageIconBody = new ImageIcon("res/images/snake_body_horizontal_x10.png");
        ImageIcon imageIconTail = new ImageIcon("res/images/snake_tail_right_x10.png");

        // Déplace la tête du serpent vers la gauche
        if (leftDirection) {
            x[0] -= TILE_SIZE;
            imageIconHead = new ImageIcon("res/images/snake_head_left_x10.png");
            imageIconTail = new ImageIcon("res/images/snake_tail_left_x10.png");
        }

        // Déplace la tête du serpent vers la droite
        if (rightDirection) {
            x[0] += TILE_SIZE;
        }

        // Déplace la tête du serpent vers le haut
        if (upDirection) {
            y[0] -= TILE_SIZE;
            imageIconHead = new ImageIcon("res/images/snake_head_up_x10.png");
            imageIconBody = new ImageIcon("res/images/snake_body_vertical_x10.png");
            imageIconTail = new ImageIcon("res/images/snake_tail_up_x10.png");
        }

        // Déplace la tête du serpent vers le bas
        if (downDirection) {
            y[0] += TILE_SIZE;
            imageIconHead = new ImageIcon("res/images/snake_head_down_x10.png");
            imageIconBody = new ImageIcon("res/images/snake_body_vertical_x10.png");
            imageIconTail = new ImageIcon("res/images/snake_tail_down_x10.png");
        }

        head = imageIconHead.getImage();
        body = imageIconBody.getImage();
        tail = imageIconTail.getImage();

    }

    private void checkCollision() {

        // On regarde si le serpent s'est touché lui-même
        for (int z = snakeDots; z > 0; z--) {
            if ((z > 4) && (x[0] == x[z]) && (y[0] == y[z])) {
                inSoloGame = false;
                break;
            }
        }

        // On regarde si le serpent a touché un mur
        if (y[0] >= BOARD_HEIGHT || y[0] < 0 || x[0] >= BOARD_WIDTH || x[0] < 0) {
            inSoloGame = false;
        }

        // Si le serpent a touché un mur ou lui-même, la partie s'arrête
        if (!inSoloGame) {
            timer.stop();
        }
    }

    /**
     * On crée une nouvelle pomme, placée aléatoirement
     */
    private void locateApple() {

        // Utilisé pour calculer l'emplacement aléatoire d'une pomme
        int RAND_POS = 50;
        int r = (int) (Math.random() * RAND_POS);
        apple_x = ((r * TILE_SIZE));

        r = (int) (Math.random() * RAND_POS);
        apple_y = ((r * TILE_SIZE));
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (inSoloGame) {
            checkApple();
            checkCollision();
            move();
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

            if ((key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) && (!rightDirection)) {
                leftDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if ((key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) && (!leftDirection)) {
                rightDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if ((key == KeyEvent.VK_UP || key == KeyEvent.VK_W) && (!downDirection)) {
                upDirection = true;
                rightDirection = false;
                leftDirection = false;
            }

            if ((key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) && (!upDirection)) {
                downDirection = true;
                rightDirection = false;
                leftDirection = false;
            }
        }
    }

    /**
     * Contrôle dans le menu
     */
    private class MenuControls extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();

            if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
                System.out.println("Solo");
                inMenu = false;
                inSoloGame = true;
            }

            if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
                System.out.println("Multi");
                inMenu = false;
                inMultiGame = true;
            }

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
                inSoloGame = false;
                inMenu = true;
            }

        }
    }

}
