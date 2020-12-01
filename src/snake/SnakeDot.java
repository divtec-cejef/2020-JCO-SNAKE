package snake;

import javafx.scene.image.Image;

// Importation des constantes
import static snake.Constants.*;

public class SnakeDot extends Dot {

    DotType dotType;
    Snake.Direction direction;

    /**
     * Contruit un point
     *
     * @param dotType   Type du point
     * @param x         Emplacement X
     * @param y         Emplacement Y
     * @param sprite    Image du point
     * @param direction Direction du point
     */
    public SnakeDot(DotType dotType, int x, int y, Sprite sprite, Snake.Direction direction) {
        super(dotType, x, y, sprite, direction);
        this.dotType = dotType;
        this.direction = direction;
    }

    @Override
    public SnakeDot translate(int dx, int dy) {
        return new SnakeDot(this.getDotType(), this.getX() + dx, this.getY() + dy, this.getSprite(), this.getDirection());
    }


    /**
     * Modifie le sprite du serpent
     */
    public void setSnakeSprite(SnakeDot previousDot) {
        // Le chemin vers les images du serpent
        String PATH_TO_SNAKE_IMAGES = PATH_TO_IMAGES + Snake.SNAKE_COLOR.toString() + "/";

        switch (this.getDotType()) {
            case HEAD:
                this.setSprite(new Sprite(PATH_TO_SNAKE_IMAGES + "head_" + direction.toString().toLowerCase() + ".png"));
                break;
            case TAIL:
                this.setSprite(new Sprite(PATH_TO_SNAKE_IMAGES + "tail_" + direction.toString().toLowerCase() + ".png"));
                break;
            case BODY:
            default:
                if (previousDot != null)
                    this.setSprite(checkForCorner(PATH_TO_SNAKE_IMAGES, previousDot));

                String orientation;
                if (this.getDirection() == Snake.Direction.LEFT || this.getDirection() == Snake.Direction.RIGHT) {
                    orientation = "horizontal";
                } else {
                    orientation = "vertical";
                }

                this.setSprite(new Sprite(PATH_TO_SNAKE_IMAGES + "body_" + orientation + ".png"));
                break;
        }
    }

    /**
     * Défini l'image utilisé pour l'angle formé par le corps du serpent
     * @param imagesPath Chemin vers les images du corps du serpent
     * @return L'image correspondant aux paramètres fournis
     */
    public Sprite checkForCorner(String imagesPath, SnakeDot previousDot) {
//        Snake.Direction snakePreviousDirection = this.getPreviousDirection();
        if (previousDot.getPreviousDirection() != null) {
            Snake.Direction snakePreviousDirection = previousDot.getPreviousDirection();

            String oldDirection = "down";
            if (direction == Snake.Direction.UP)
                oldDirection = "up";
            else if (direction == Snake.Direction.DOWN)
                oldDirection = "down";

            String newDirection = "left";
            switch (snakePreviousDirection) {
                case RIGHT:
                    newDirection = "left";
                    break;
                case LEFT:
                    newDirection = "right";
                    break;
            }

            System.out.println("Direction précédente : " + snakePreviousDirection);
            System.out.println("Nouvelle direction : " + direction);

            boolean isValidImage = true;
//            if ((direction == Snake.Direction.RIGHT && snakePreviousDirection == Snake.Direction.DOWN) || (direction == Snake.Direction.RIGHT && snakePreviousDirection == Snake.Direction.RIGHT)) {
            if (direction == Snake.Direction.RIGHT && snakePreviousDirection == Snake.Direction.DOWN) {
                oldDirection = "up";
                newDirection = "right";
            }

//            if ((direction == Snake.Direction.RIGHT && snakePreviousDirection == Snake.Direction.UP) || (direction == Snake.Direction.DOWN && snakePreviousDirection == Snake.Direction.DOWN)) {
            if (direction == Snake.Direction.RIGHT && snakePreviousDirection == Snake.Direction.UP) {
                oldDirection = "down";
                newDirection = "right";
            }

//            if ((direction == Snake.Direction.LEFT && snakePreviousDirection == Snake.Direction.UP) || (direction == Snake.Direction.LEFT && snakePreviousDirection == Snake.Direction.LEFT)) {
            if (direction == Snake.Direction.LEFT && snakePreviousDirection == Snake.Direction.UP) {
                oldDirection = "down";
                newDirection = "left";
            }

//            if ((direction == Snake.Direction.LEFT && snakePreviousDirection == Snake.Direction.DOWN) || (direction == Snake.Direction.UP && snakePreviousDirection == Snake.Direction.UP)) {
            if (direction == Snake.Direction.LEFT && snakePreviousDirection == Snake.Direction.DOWN) {
                oldDirection = "up";
                newDirection = "left";
            }

            // Nom de l'image de l'angle
            String imageName = "corner_" + oldDirection + newDirection.substring(0, 1).toUpperCase() + newDirection.substring(1) + ".png";

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

            // Chemin vers l'image
            String spritePath = imagesPath + imageName;

            // Si l'image n'est pas valide, on la remplace par une autre image
            if (isValidImage)
                return new Sprite(spritePath);

            String orientation;
            if (direction == Snake.Direction.LEFT || direction == Snake.Direction.RIGHT) {
                orientation = "horizontal";
            } else {
                orientation = "vertical";
            }

            return new Sprite(imagesPath + "body_" + orientation + ".png");
        } else {
            String orientation;
            if (direction == Snake.Direction.LEFT || direction == Snake.Direction.RIGHT) {
                orientation = "horizontal";
            } else {
                orientation = "vertical";
            }

            return new Sprite(imagesPath + "body_" + orientation + ".png");
        }
    }
}
