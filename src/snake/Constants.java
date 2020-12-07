package snake;

import javafx.scene.paint.Color;

/**
 * Constantes du programme
 */
public class Constants {
    /**
     * Nom du jeu
     */
    public static final String GAME_NAME = "SNAKE";

    /**
     * CHEMINS
     */

    // Chemin vers les images dans l'arborescence de fichiers
    public static final String PATH_TO_IMAGES = "/images/";

    // Chemin vers l'icône du jeu
    public static final String ICON_PATH = PATH_TO_IMAGES + "icon.png";

    /**
     * GRILLE
     */

    // Largeur de la fenêtre
    public static final int WIDTH = 500;

    // Hauteur de la fenêtre
    public static final int HEIGHT = 500;

    // La taille d'une case de la grille
    public static final int TILE_SIZE = 10;

    // Couleur de fond de la grille
    public static final Color BACKGROUND_COLOR = new Color(0.1, 0.1, 0.1, 1);

    // Couleur des textes
    public static final Color TEXT_COLOR = Color.BEIGE;


    /**
     * SERPENT
     */

     // Vitesse de base du serpent
    public static final int INITIAL_SNAKE_VELOCITY = 1;

    // Augmentation de la vitesse du serpent
    public static final int SNAKE_VELOCITY_INCREASE = 2;

    // Longueur du serpent en début de partie
    public static final int INITIAL_SNAKE_LENGTH = 3;

    // Direction initiale du serpent
    public static final Snake.Direction INITIAL_SNAKE_DIRECTION = Snake.Direction.LEFT;

}
