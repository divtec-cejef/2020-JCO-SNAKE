package snake;

// Importation des constantes
import static snake.Constants.*;

public class SnakeDot extends Dot {

    DotType dotType;
    Snake.Direction direction;
    Snake.SnakeColor color;

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
        this.dotType = dotType;
        this.direction = direction;
        this.color = color;
    }

    @Override
    public SnakeDot translate(int dx, int dy) {
        return new SnakeDot(this.getDotType(), this.getX() + dx, this.getY() + dy, this.color, this.getSprite(), this.getDirection());
    }

    /**
     * @return La couleur du point
     */
    public Snake.SnakeColor getColor() {
        return color;
    }

    /**
     * Modifie le sprite du serpent
     */
    public void setSnakeSprite(SnakeDot previousDot) {
        // Le chemin vers les images du serpent
        String PATH_TO_SNAKE_IMAGES = PATH_TO_IMAGES + color.toString() + "/";

        switch (this.getDotType()) {
            case HEAD:
                this.setSprite(new Sprite(PATH_TO_SNAKE_IMAGES + "head_" + direction.toString().toLowerCase() + ".png"));
                break;
            case TAIL:
                this.setSprite(new Sprite(PATH_TO_SNAKE_IMAGES + "tail_" + direction.toString().toLowerCase() + ".png"));
                break;
            case BODY:
            default:
                if (previousDot != null && previousDot.getPreviousDirection() != null)
                    this.setSprite(checkForCorner(PATH_TO_SNAKE_IMAGES, previousDot.getPreviousDirection()));
                else {
                    String orientation;
                    if (direction == Snake.Direction.LEFT || direction == Snake.Direction.RIGHT) {
                        orientation = "horizontal";
                    } else {
                        orientation = "vertical";
                    }

                    this.setSprite(new Sprite(PATH_TO_SNAKE_IMAGES + "body_" + orientation + ".png"));
                }
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
//        System.out.println("checkForCorner()");
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
}
