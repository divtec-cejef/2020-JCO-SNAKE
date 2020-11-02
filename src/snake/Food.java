package snake;

import javax.swing.*;
import java.awt.*;
import java.awt.image.ImageObserver;

/**
 * Classe qui représente de la nourriture pour serpent
 */
public class Food {

    private Image apple;
    private int apple_x;
    private int apple_y;

    /**
     * Crée une nouvelle pomme
     */
    public Food() {
        setImages();
    }

    /**
     * Initialise le sprite de la pomme
     */
    private void setImages(){
        ImageIcon imageIconApple = new ImageIcon("res/images/apple.png");
        apple = imageIconApple.getImage();
    }

    /**
     * Dessine la pomme sur le plateau de jeu
     * @param g Graphics
     * @param imageObserver Observer d'image
     */
    public void draw(Graphics g, ImageObserver imageObserver){
        g.drawImage(apple, apple_x, apple_y, imageObserver);
    }

    /**
     * Vérifie si une pomme se fait manger
     * @param serpent Serpent qui a mangé la pomme
     * @param timer Timer du jeu
     */
    public void checkApple(Snake serpent, Timer timer) {
        if ((Board.X[0] == apple_x) && (Board.Y[0] == apple_y)) {
            serpent.ateApple();
            timer.setDelay(serpent.getSnakeSpeed());
            locateApple();
        }
    }

    /**
     * On crée une nouvelle pomme, placée aléatoirement
     */
    public void locateApple() {
        // Utilisé pour calculer l'emplacement aléatoire d'une pomme
        int RANDOM_POSITION = 50;

        int r = (int) (Math.random() * RANDOM_POSITION);
        apple_x = ((r * Board.TILE_SIZE));

        r = (int) (Math.random() * RANDOM_POSITION);
        apple_y = ((r * Board.TILE_SIZE));
    }
}
