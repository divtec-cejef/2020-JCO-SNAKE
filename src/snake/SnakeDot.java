package snake;

import com.sun.istack.internal.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.image.ImageObserver;

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

//                ImageIcon imageIconBodyDown2Right = new ImageIcon(pathToImages + dotColor.toString() + "/body_down2right.png");
//                bodyDown2Right = imageIconBodyDown2Right.getImage();
//                ImageIcon imageIconBodyRight2Down = new ImageIcon(pathToImages + dotColor.toString() + "/body_right2down.png");
//                bodyRight2Down = imageIconBodyRight2Down.getImage();
//                ImageIcon imageIconBodyRight2Up = new ImageIcon(pathToImages + dotColor.toString() + "/body_right2up.png");
//                bodyRight2Up = imageIconBodyRight2Up.getImage();
//                ImageIcon imageIconBodyUp2Right = new ImageIcon(pathToImages + dotColor.toString() + "/body_up2right.png");
//                bodyUp2Right = imageIconBodyUp2Right.getImage();
                ImageIcon imageIconBodyDown2Right = new ImageIcon(pathToImages + "RED/body_down2right.png");
                bodyDown2Right = imageIconBodyDown2Right.getImage();
                ImageIcon imageIconBodyRight2Down = new ImageIcon(pathToImages + "RED/body_right2down.png");
                bodyRight2Down = imageIconBodyRight2Down.getImage();
                ImageIcon imageIconBodyRight2Up = new ImageIcon(pathToImages + "RED/body_right2up.png");
                bodyRight2Up = imageIconBodyRight2Up.getImage();
                ImageIcon imageIconBodyUp2Right = new ImageIcon(pathToImages + "RED/body_up2right.png");
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

    public void draw(Graphics g, ImageObserver imageObserver, Image sprite ) {
        g.drawImage(setSprite(), Board.X[0], Board.Y[0], imageObserver);
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
