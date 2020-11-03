package snake;

import com.sun.istack.internal.Nullable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * Représente un point du corps d'un serpent
 */
public class SnakeDot {

    // Types de points possibles
    public enum DotType {
        Head,
        Body,
        Tail
    }

    /* Images du serpent */
    // Corps du serpent
    private Image bodyHorizonzal;
    private Image bodyVertical;
    // Tête du serpent
    private Image headLeft;
    private Image headRight;
    private Image headUp;
    private Image headDown;
    // Queue du serpent
    private Image tailLeft;
    private Image tailRight;
    private Image tailUp;
    private Image tailDown;
    // Angles
    private Image bodyDown2Right;
    private Image bodyRight2Down;
    private Image bodyRight2Up;
    private Image bodyUp2Right;

    // Type de partie de serpent
    private final DotType dotType;

    // Direction du serpent
    private Snake.Direction dotDirection;
    // Direction précédente du serpent
    private Snake.Direction dotPreviousDirection;

    // Couleur du serpent
    private final Snake.Color dotColor;

    /**
     * Crée un point de serpent
     * @param dotType Le type de point que l'on veut créer
     * @param dotColor La couleur de ce point
     * @param dotDirection La direction de ce point
     */
    public SnakeDot(DotType dotType, Snake.Color dotColor, Snake.Direction dotDirection) {
        this.dotType = dotType;
        this.dotColor = dotColor;
        this.dotDirection = dotDirection;
        this.dotPreviousDirection = dotDirection;

        getImages();
    }

    /**
     * Initialise les sprites du point selon son type
     */
    private void getImages() {
        // Chemin vers les images
        String pathToImages = "/images/";
        switch (dotType) {
            // Le point est une tête
            case Head:
                headLeft = new Image(pathToImages + dotColor.toString() + "/head_left.png");
                headRight = new Image(pathToImages + dotColor.toString() + "/head_right.png");
                headUp = new Image(pathToImages + dotColor.toString() + "/head_up.png");
                headDown = new Image(pathToImages + dotColor.toString() + "/head_down.png");

                break;
            // Le point fait partie du corps
            case Body:
                bodyHorizonzal = new Image(pathToImages + dotColor.toString() + "/body_horizontal.png");
                bodyVertical = new Image(pathToImages + dotColor.toString() + "/body_vertical.png");

                bodyDown2Right = new Image(pathToImages + "RED/body_down2right.png");
                bodyRight2Down = new Image(pathToImages + "RED/body_right2down.png");
                bodyRight2Up = new Image(pathToImages + "RED/body_right2up.png");
                bodyUp2Right = new Image(pathToImages + "RED/body_up2right.png");

                break;
            // Le point est une queue
            case Tail:
                tailLeft = new Image(pathToImages + dotColor.toString() + "/tail_left.png");
                tailRight = new Image(pathToImages + dotColor.toString() + "/tail_right.png");
                tailUp = new Image(pathToImages + dotColor.toString() + "/tail_up.png");
                tailDown = new Image(pathToImages + dotColor.toString() + "/tail_down.png");

                break;
        }
    }

    /**
     * Dessine le morceau de serpent sur le plateau de jeu
     * @param g GraphicsContext
     * @param index Emplacement où l'image doit être placée
     */
    public void draw(GraphicsContext g, int index){
        g.drawImage(setSprite(), Game.X[index], Game.Y[index]);
    }

    public void draw(GraphicsContext g, Image sprite ) {
        g.drawImage(setSprite(), Game.X[0], Game.Y[0]);
    }

    private Image setSprite() {
        return setSprite(null, null);
    }

    /**
     * Défini le sprite utilisé par ce point, selon son type et sa direction
     * @return Le sprite choisi
     */
    public Image setSprite(@Nullable Snake.Direction headDirection, @Nullable Snake.Direction headPreviousDirection) {
        Image head = null;
        Image tail = null;
        Image body = null;

        switch (dotDirection) {
            case LEFT:
                head = headLeft;
                tail = tailLeft;
                break;
            case RIGHT:
                head = headRight;
                tail = tailRight;
                break;
            case UP:
                head = headUp;
                tail = tailUp;
                break;
            case DOWN:
                head = headDown;
                tail = tailDown;
                break;
        }
        if (dotDirection == Snake.Direction.LEFT || dotDirection == Snake.Direction.RIGHT) {
            body = bodyHorizonzal;
        }

        if (dotDirection == Snake.Direction.UP || dotDirection == Snake.Direction.DOWN) {
            body = bodyVertical;
        }

        if (headDirection != null && headPreviousDirection != null) {
            switch (headPreviousDirection) {
                case RIGHT:
                    if (headDirection == Snake.Direction.UP) {
                        body = bodyRight2Up;
                        System.out.println("r2u");
                    } else if (headDirection == Snake.Direction.DOWN) {
                        body = bodyRight2Down;
                        System.out.println("r2d");
                    }
                case UP:
                    if (headDirection == Snake.Direction.RIGHT) {
                        body = bodyUp2Right;
                        System.out.println("u2r");
                    }
                case DOWN:
                    if (headDirection == Snake.Direction.RIGHT) {
                        body = bodyDown2Right;
                        System.out.println("d2r");
                    }
            }

            return body;
        } else {
            switch (dotType) {
                case Head:
                    return head;
                case Body:
                    return body;
                case Tail:
                    return tail;
                default:
                    return null;
            }
        }
    }

    /**
     * @return Le type de ce point
     */
    public DotType getDotType() {
        return dotType;
    }

    /**
     * @return La direction de ce point
     */
    public Snake.Direction getDotDirection() {
        return dotDirection;
    }

    /**
     * Défini la direction du point
     * @param dotDirection Nouvelle direction du point
     */
    public void setDotDirection(Snake.Direction dotDirection) {
        this.dotDirection = dotDirection;
    }

    public Snake.Direction getDotPreviousDirection() {
        return dotPreviousDirection;
    }

    public void setDotPreviousDirection(Snake.Direction dotPreviousDirection) {
        this.dotPreviousDirection = dotPreviousDirection;
    }
}
