package snake;

/**
 * Classe qui représente de la nourriture pour serpent
 */
public class Food {
    private Dot foodDot;

    Food(Dot dot) {
        this.foodDot = dot;
    }

    public Dot getDot() {
        return foodDot;
    }

    public void setDot(Dot dot) {
        this.foodDot = dot;
    }
}
