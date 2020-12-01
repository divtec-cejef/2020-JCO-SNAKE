package snake;

// Importation des constantes
import static snake.Constants.*;

/**
 * Classe qui représente de la nourriture pour serpent
 */
public class Food extends Dot {

    /**
     * Crée une nouvelle nourriture pour serpent
     */
    public Food() {
        super(DotType.FOOD, 0, 0, new Sprite(PATH_TO_IMAGES + "apple.png"), Snake.Direction.NONE);
    }

    /**
     * Crée une nouvelle nourriture pour serpent
     * @param x coordonnée x de cette nourriture
     * @param y coordonnée y de cette nourriture
     */
    public Food (int x, int y) {
        super(DotType.FOOD, x, y, new Sprite(PATH_TO_IMAGES + "apple.png"), Snake.Direction.NONE);
    }

}
