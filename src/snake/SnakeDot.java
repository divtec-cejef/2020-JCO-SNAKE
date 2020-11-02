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

    // Chemin vers les images
    private final String pathToImages = "res/images/";

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

    // Crée un point de serpent
    public SnakeDot(DotType dotType, Snake.Color dotColor) {
        this.dotType = dotType;
        this.dotColor = dotColor;

        setImages(dotColor);
    }

    /**
     * Initialise les sprites du point
     * @param dotColor Couleur utilisée pour les sprites
     */
    private void setImages(Snake.Color dotColor) {
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

    public void draw(Graphics g, ImageObserver imageObserver, int index){
//        System.out.println(index);
        g.drawImage(setSprite(), Board.X[index], Board.Y[index], imageObserver);
    }

    private Image setSprite() {
        if (dotType == DotType.Head) {
            if (dotDirection == Snake.Direction.LEFT) {
                return headLeft;
            }

            if (dotDirection == Snake.Direction.RIGHT) {
                return headRight;
            }

            if (dotDirection == Snake.Direction.UP) {
                return headUp;
            }

            if (dotDirection == Snake.Direction.DOWN) {
                return headDown;
            }

        } else if (dotType == DotType.Tail) {
            if (dotDirection == Snake.Direction.LEFT) {
                return tailLeft;
            }

            if (dotDirection == Snake.Direction.RIGHT) {
                return tailRight;
            }

            if (dotDirection == Snake.Direction.UP) {
                return tailUp;
            }

            if (dotDirection == Snake.Direction.DOWN) {
                return tailDown;
            }

        } else {
            if (dotDirection == Snake.Direction.LEFT || dotDirection == Snake.Direction.RIGHT) {
                return bodyHorizonzal;
            }

            if (dotDirection == Snake.Direction.UP || dotDirection == Snake.Direction.DOWN) {
                return bodyVertical;
            }

        }

        return null;
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
}
