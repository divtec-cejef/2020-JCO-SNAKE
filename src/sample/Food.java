package sample;

/**
 * Classe qui représente de la nourriture pour serpent
 */
public class Food {
    private int x;
    private int y;
    private boolean hasBeenEaten = false;

    public Food(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void getEaten() {
        hasBeenEaten = true;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
