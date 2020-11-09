package snake;

/**
 * Représente un point
 */
public class Dot {

    public enum DotType {
        HEAD,
        BODY,
        TAIL,
        FOOD
    }

    // Coordonnée X du point
    private final int x;
    // Coordonnée Y du point
    private final int y;

    // Type du point
    private DotType dotType;

    /**
     * Contruit un point
     *
     * @param dotType Type du point
     * @param x Emplacement X
     * @param y Emplacement Y
     */
    Dot(final DotType dotType, final int x, final int y) {
        this.dotType = dotType;
        this.x = x;
        this.y = y;
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
    public Dot translate(DotType dotType, int dx, int dy) {
        return new Dot(dotType, x + dx, y + dy);
    }

    /**
     * @param other Le point qui va être comparé
     * @return {@code true} si l'autre Object est une instance de Dot et à les mêmes coordonnées
     */
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Dot)) return false;
        Dot dot = (Dot) other;
        return x == dot.x & y == dot.y;
    }

    /**
     * @return Les coordonnées du point sous forme de texte
     */
    public String toString() {
        return dotType + ", " + x + ", " + y;
    }
}
