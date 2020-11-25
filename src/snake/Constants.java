package snake;

import javafx.scene.paint.Color;

/**
 * Constantes du programme
 */
public class Constants {
    /**
     * Largeur de la fenêtre
     */
    public static final int WIDTH = 500;

    /**
     * Hauteur de la fenêtre
     */
    public static final int HEIGHT = 500;

    /**
     * La taille d'une case de la grille
     */
    public static final int TILE_SIZE = 10;

    /**
     * Couleur de fond de la grille
     */
    public static final Color BACKGROUND_COLOR = new Color(0.1, 0.1, 0.1, 1);

    /**
     * Couleur des textes
     */
    public static final Color TEXT_COLOR = Color.BEIGE;

    /**
     * Chemin vers les images dans l'arborescence de fichiers
     */
    public static final String PATH_TO_IMAGES = "/images/";

    /**
     * Vitesse du jeu
     */
    public static double INITIAL_TIMELINE_RATE = 5;

    /**
     * Augmentation de la vitesse du jeu
     */
    public static double TIMELINE_RATE_INCREASE = 3;

    /**
     * Longueur du serpent en début de partie
     */
    public static final int INITIAL_SNAKE_LENGTH = 3;

    /**
     * Direction initiale du serpent
     */
    public static final Snake.Direction INITIAL_SNAKE_DIRECTION = Snake.Direction.LEFT;
}