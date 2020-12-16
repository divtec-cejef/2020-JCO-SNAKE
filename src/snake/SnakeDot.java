package snake;

// Importation des constantes
import static snake.Constants.*;

public class SnakeDot extends Dot {

    Snake.Direction direction;
    Snake.SnakeColor color;

    // Le chemin vers les images du serpent
    String PATH_TO_SNAKE_IMAGES;

    /**
     * Contruit un point faisant partie du serpent
     *
     * @param dotType   Type du point
     * @param x         Emplacement X
     * @param y         Emplacement Y
     * @param color     Couleur du point
     * @param sprite    Image du point
     * @param direction Direction du point
     */
    public SnakeDot(DotType dotType, int x, int y, Snake.SnakeColor color, Sprite sprite, Snake.Direction direction) {
        super(dotType, x, y, sprite, direction);
        this.direction = direction;
        this.color = color;

        PATH_TO_SNAKE_IMAGES = PATH_TO_IMAGES + color.toString() + "/";
    }

    /**
     * @param dx différence de coordonnée X
     * @param dy différence de coordonnée Y
     * @return un point avec les nouvelles coordonnées
     */
    @Override
    public SnakeDot translate(int dx, int dy) {
        return new SnakeDot(this.getDotType(), this.getX() + dx, this.getY() + dy, this.color, this.getSprite(), this.getDirection());
    }

    /**
     * Modifie le sprite du serpent
     *
     * @param previousDot Point précédent ce point
     */
    public void setSnakeSprite(SnakeDot previousDot) {
        DotType bodyType = this.getDotType();
        switch (bodyType) {
            case HEAD:
            case TAIL:
                this.setSprite(new Sprite(PATH_TO_SNAKE_IMAGES + bodyType.toString().toLowerCase() + "_" + direction.toString().toLowerCase() + ".png"));
                break;
            case BODY:
            default:
                Sprite bodySprite;
                if (previousDot != null && previousDot.getPreviousDirection() != null) {
                    // Emplacement correct de l'angle
                    Painter.paintDot(HIGHLIGHTED_FALSE_TEXT_COLOR, TILE_SIZE * previousDot.getX(), TILE_SIZE *  previousDot.getY());

                    bodySprite = checkForCorner(PATH_TO_SNAKE_IMAGES, previousDot.getPreviousDirection());
                } else {
                    String orientation;
                    if (direction == Snake.Direction.LEFT || direction == Snake.Direction.RIGHT)
                        orientation = "horizontal";
                    else
                        orientation = "vertical";

                    bodySprite = new Sprite(PATH_TO_SNAKE_IMAGES + "body_" + orientation + ".png");
                }

                this.setSprite(bodySprite);
                break;
        }
    }

    /**
     * Défini l'image utilisé pour l'angle formé par le corps du serpent
     *
     * @param imagesPath Chemin vers les images du corps du serpent
     * @return L'image correspondant aux paramètres fournis
     */
    public Sprite checkForCorner(String imagesPath, Snake.Direction previousDirection) {
        boolean isValidImage = true;

        String upOrDown = "";
        String leftOrRight = "";

        // corner_upLeft.png
        if ((previousDirection == Snake.Direction.UP && direction == Snake.Direction.RIGHT) ||
            (previousDirection == Snake.Direction.LEFT && direction == Snake.Direction.DOWN)) {
            upOrDown = "up";
            leftOrRight = "Left";
        }

        // corner_upRight.png
        if ((previousDirection == Snake.Direction.UP && direction == Snake.Direction.LEFT) ||
            (previousDirection == Snake.Direction.RIGHT && direction == Snake.Direction.DOWN)) {
            upOrDown = "up";
            leftOrRight = "Right";
        }

        // corner_downLeft.png
        if ((previousDirection == Snake.Direction.LEFT && direction == Snake.Direction.UP) ||
            (previousDirection == Snake.Direction.DOWN && direction == Snake.Direction.RIGHT)) {
            upOrDown = "down";
            leftOrRight = "Left";
        }

        // corner_downRight.png
        if ((previousDirection == Snake.Direction.RIGHT && direction == Snake.Direction.UP) ||
            (previousDirection == Snake.Direction.DOWN && direction == Snake.Direction.LEFT)) {
            upOrDown = "down";
            leftOrRight = "Right";
        }

        String imageName = "corner_" + upOrDown + leftOrRight + ".png";

        // Vérification de l'existence de l'image
        switch (imageName) {
            case "corner_downLeft.png":
            case "corner_downRight.png":
            case "corner_upLeft.png":
            case "corner_upRight.png":
                break;
            default:
                System.out.println(imageName + " : n'existe pas");
                isValidImage = false;
        }

        if (!isValidImage) {
            String orientation;
            if (this.direction == Snake.Direction.LEFT || this.direction == Snake.Direction.RIGHT)
                orientation = "horizontal";
            else
                orientation = "vertical";

            return new Sprite(imagesPath + "body_" + orientation + ".png");
        }

        return new Sprite(imagesPath + imageName);

    }


    // ******************************  GETTER  ****************************** //

    /**
     * @return La couleur du point
     */
    public Snake.SnakeColor getColor() {
        return color;
    }


    // ******************************  SETTER  ****************************** //

    /**
     * Change la couleur de ce point
     * @param color Nouvelle couleur
     */
    public void setColor(Snake.SnakeColor color) {
        this.color = color;
        PATH_TO_SNAKE_IMAGES = PATH_TO_IMAGES + color.toString() + "/";
    }

}
