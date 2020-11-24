package snake;

/**
 * Classe qui représente de la nourriture pour serpent
 */
public class Food {
    private Dot foodDot;

    /**
     * Crée une nouvelle nourriture pour serpent
     * @param dot Point représentant cette nourriture
     */
    Food(Dot dot) {
        this.foodDot = dot;
    }

    /**
     * @return un point de type nourriture
     */
    public Dot getDot() {
        return foodDot;
    }

    /**
     * Modifie un point de nourriture
     * @param dot Nouveau point
     */
    public void setDot(Dot dot) {
        this.foodDot = dot;
    }
}
