package snake;

import javax.swing.*;
import java.awt.*;
import java.awt.image.ImageObserver;

/**
 * Représente un point du corps d'un serpent
 */
public class SnakeDot {
    // Types de point possibles
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

    // Direction par défaut du serpent
    private final Snake.Direction INITIAL_DOT_DIRECTION = Snake.Direction.RIGHT;

    // Type de partie de serpent
    private final DotType dotType;

    // Direction du serpent
    private Snake.Direction dotDirection = INITIAL_DOT_DIRECTION;
    // Précédente direction du serpent
    private Snake.Direction dotPreviousDirection = dotDirection;

    // Couleur du serpent
    private final Snake.Color dotColor;

    /**
     * Crée un point de serpent
     * @param dotType Le type de point que l'on veut créer
     * @param dotColor La couleur de ce point
     */
    public SnakeDot(DotType dotType, Snake.Color dotColor) {
        this.dotType = dotType;
        this.dotColor = dotColor;

        setImages();
    }

    /**
     * Initialise les sprites du point
     */
    private void setImages() {
        // Chemin vers les images
        String pathToImages = "res/images/";
        switch (dotType) {
            // Le point est une tête
            case Head:
                ImageIcon imageIconHeadLeft = new ImageIcon(pathToImages + dotColor.toString() + "/head_left.png");
                headLeft = imageIconHeadLeft.getImage();
                ImageIcon imageIconHeadRight = new ImageIcon(pathToImages + dotColor.toString() + "/head_right.png");
                headRight = imageIconHeadRight.getImage();
                ImageIcon imageIconHeadUp = new ImageIcon(pathToImages + dotColor.toString() + "/head_up.png");
                headUp = imageIconHeadUp.getImage();
                ImageIcon imageIconHeadDown = new ImageIcon(pathToImages + dotColor.toString() + "/head_down.png");
                headDown = imageIconHeadDown.getImage();
                break;
            // Le point fait partie du corps
            case Body:
                ImageIcon imageIconBodyHorizontal = new ImageIcon(pathToImages + dotColor.toString() + "/body_horizontal.png");
                bodyHorizonzal = imageIconBodyHorizontal.getImage();
                ImageIcon imageIconBodyVertical = new ImageIcon(pathToImages + dotColor.toString() + "/body_vertical.png");
                bodyVertical = imageIconBodyVertical.getImage();

                ImageIcon imageIconBodyDown2Right = new ImageIcon(pathToImages + dotColor.toString() + "/body_down2right.png");
                bodyDown2Right = imageIconBodyDown2Right.getImage();
                ImageIcon imageIconBodyRight2Down = new ImageIcon(pathToImages + dotColor.toString() + "/body_right2down.png");
                bodyRight2Down = imageIconBodyRight2Down.getImage();
                ImageIcon imageIconBodyRight2Up = new ImageIcon(pathToImages + dotColor.toString() + "/body_right2up.png");
                bodyRight2Up = imageIconBodyRight2Up.getImage();
                ImageIcon imageIconBodyUp2Right = new ImageIcon(pathToImages + dotColor.toString() + "/body_up2right.png");
                bodyUp2Right = imageIconBodyUp2Right.getImage();

                break;
            // Le point est une queue
            case Tail:
                ImageIcon imageIconTailLeft = new ImageIcon(pathToImages + dotColor.toString() + "/tail_left.png");
                tailLeft = imageIconTailLeft.getImage();
                ImageIcon imageIconTailRight = new ImageIcon(pathToImages + dotColor.toString() + "/tail_right.png");
                tailRight = imageIconTailRight.getImage();
                ImageIcon imageIconTailUp = new ImageIcon(pathToImages + dotColor.toString() + "/tail_up.png");
                tailUp = imageIconTailUp.getImage();
                ImageIcon imageIconTailDown = new ImageIcon(pathToImages + dotColor.toString() + "/tail_down.png");
                tailDown = imageIconTailDown.getImage();
                break;
        }

    }

    /**
     * Dessine le morceau de serpent sur le plateau de jeu
     * @param g Graphics
     * @param imageObserver Observer d'image
     * @param index Emplacement où l'image doit être placée
     */
    public void draw(Graphics g, ImageObserver imageObserver, int index){
        g.drawImage(setSprite(), Board.X[index], Board.Y[index], imageObserver);
    }

    /**
     * Défini le sprite utilisé par ce point, selon son type et sa direction
     * @return Le sprite choisi
     */
    private Image setSprite() {
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
        if (dotDirection != dotPreviousDirection && this.dotDirection != dotPreviousDirection)
            this.dotPreviousDirection = this.dotDirection;
        this.dotDirection = dotDirection;
        System.out.println("Direction précédente de " + dotType.toString() + " : " + dotPreviousDirection);
        System.out.println("Direction actuelle de "   + dotType.toString() + " : " + dotDirection + "\n");
    }
}
