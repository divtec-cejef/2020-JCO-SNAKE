package snake;

import javafx.scene.input.KeyCode;
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
     * Vitesse du jeu
     */
    public static final int FPS = 10;

    /**
     * CHEMINS
     */

    // Chemin vers les images dans l'arborescence de fichiers
    public static final String PATH_TO_IMAGES = "/images/";

    // Chemin vers l'icône du jeu
    public static final String ICON_PATH = PATH_TO_IMAGES + "icon.png";

    /**
     * COMMANDES
     */

    // Touche de fermeture de la fenêtre
    public static final KeyCode CLOSE_GAME_KEY = KeyCode.Q;

    // Touche de redémarrage du jeu
    public static final KeyCode RESTART_KEY = KeyCode.ENTER;

    // Touche de changement de mode de jeu
    public static final KeyCode CHANGE_GAME_MODE_KEY = KeyCode.C;

    // Touche de sélection d'option
    public static final KeyCode TOGGLE_OPTION_KEY = KeyCode.SPACE;

    // Touche de séléection de l'option suivante
    public static final KeyCode NEXT_OPTION_KEY = KeyCode.TAB;


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
