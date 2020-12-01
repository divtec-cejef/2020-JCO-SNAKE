package snake;

// Importation des constantes
import static snake.Constants.*;

/**
 * Représente un point
 */
public class Dot {

    /**
     * Types de point possibles
     */
    public enum DotType {
        HEAD,
        BODY,
        TAIL,
        FOOD
    }

    // Coordonnée X du point
    private int x;
    // Coordonnée Y du point
    private int y;

    private Snake.Direction direction;
    private Snake.Direction previousDirection = null;

    private Sprite sprite;

    // Type du point
    private DotType dotType;

    /**
     * Contruit un point
     * @param dotType Type du point
     * @param x Emplacement X
     * @param y Emplacement Y
     * @param sprite Image du point
     * @param direction Direction du point
     */
    Dot(DotType dotType, int x, int y, Sprite sprite, Snake.Direction direction) {
        this.dotType = dotType;
        this.x = x;
        this.y = y;
        this.sprite = sprite;
        this.direction = direction;
    }

    /**
     * @return Coordonnée X du point
     */
    public int getX() {
        return x;
    }

    /**
     * @return Coordonnée Y du point
     */
    public int getY() {
        return y;
    }

    /**
     * Change la coordonnée x du point
     * @param x Nouvelle coordonnée x
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Change la coordonnée x du point
     * @param y Nouvelle coordonnée y
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * @return Le type du point
     */
    public DotType getDotType() {
        return dotType;
    }

    /**
     * Change le type du point
     * @param newDotType Le nouveau type du point
     */
    public void setDotType(DotType newDotType) {
        this.dotType = newDotType;
    }

    /**
     * @param dx différence de coordonnée X
     * @param dy différence de coordonnée Y
     * @return un point avec les nouvelles coordonnées
     */
    public Dot translate(int dx, int dy) {
        return new Dot(dotType, x + dx, y + dy, sprite, direction);
    }

    /**
     * @param other Le point qui va être comparé
     * @return {@code true} si l'autre Object est une instance de Dot et à les mêmes coordonnées
     */
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Dot)) return false;
        Dot dot = (Dot) other;
        return x == dot.x && y == dot.y;
    }

    /**
     * @return la direction attribuée à ce point
     */
    public Snake.Direction getDirection() {
        return direction;
    }

    /**
     * @return la précédente direction de ce point
     */
    public Snake.Direction getPreviousDirection() {
        return previousDirection;
    }

    /**
     * Modifie la direction du point
     * @param direction Nouvelle direction attribuée à ce point
     */
    public void setDirection(Snake.Direction direction) {
        this.previousDirection = this.direction;
        this.direction = direction;
    }

    /**
     * @return le sprite de ce point
     */
    public Sprite getSprite() {
        return sprite;
    }

    /**
     * Modifie le sprite de ce point
     * @param sprite Nouveau sprite du point
     */
    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public Dot getDot() {
        return this;
    }

}
