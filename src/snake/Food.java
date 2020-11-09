package snake;

import javafx.scene.paint.Color;

/**
 * Classe qui représente de la nourriture pour serpent
 */
public class Food {

    // Remplacer par un sprite
    public static final Color FOOD_COLOR = Color.RED;

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

//    private Image apple;
//    private int apple_x;
//    private int apple_y;
//
//    /**
//     * Crée une nouvelle pomme
//     */
//    public Food() {
//        getImages();
//    }
//
//    /**
//     * Initialise le sprite de la pomme
//     */
//    private void getImages(){
//        apple = new Image("/images/apple.png");
//    }
//
//    /**
//     * Dessine la pomme sur le plateau de jeu
//     * @param g GraphicsContext
//     */
//    public void draw(GraphicsContext g){
//        g.drawImage(apple, apple_x, apple_y);
//    }
//
//    /**
//     * Vérifie si une pomme se fait manger
//     * @param serpent Serpent qui a mangé la pomme
//     * @param timer Timer du jeu
//     */
//    public void checkApple(Snake serpent, Timeline timer) {
//        if ((Game.X[0] == apple_x) && (Game.Y[0] == apple_y)) {
//            serpent.ateApple();
//
//            Duration duration = new Duration(serpent.getSnakeSpeed());
//            timer.setDelay(duration);
//            locateApple();
//        }
//    }
//
//    /**
//     * On crée une nouvelle pomme, placée aléatoirement
//     */
//    public void locateApple() {
//        // Utilisé pour calculer l'emplacement aléatoire d'une pomme
//        int RANDOM_POSITION = 50;
//
//        int r = (int) (Math.random() * RANDOM_POSITION);
//        apple_x = ((r * Game.TILE_SIZE));
//
//        r = (int) (Math.random() * RANDOM_POSITION);
//        apple_y = ((r * Game.TILE_SIZE));
//    }
}
