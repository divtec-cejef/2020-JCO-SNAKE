package snake;

/**
 * Représente un point
 */
public class Dot {

    // Coordonnée X du point
    private final int x;
    // Coordonnée Y du point
    private final int y;

    /**
     * Contruit un point
     *
     * @param x Emplacement X
     * @param y Emplacement Y
     */
    Dot(final int x, final int y) {
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
     * @param dx différence de coordonnée X
     * @param dy différence de coordonnée Y
     * @return un point avec les nouvelles coordonnées
     */
    public Dot translate(int dx, int dy) {
        return new Dot(x + dx, y + dy);
    }

    /**
     * @param other Le point qui va être comparé
     * @return {@code true} si l'autre Object est une instance de Dot et à les mêmes coordonnées
     */
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Dot)) return false;
        Dot point = (Dot) other;
        return x == point.x & y == point.y;
    }

    /**
     * @return Les coordonnées du point sous forme de texte
     */
    public String toString() {
        return x + ", " + y;
    }
}
