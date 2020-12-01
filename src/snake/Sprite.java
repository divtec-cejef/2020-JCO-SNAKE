package snake;

import javafx.scene.image.Image;

// Importation des constantes
import static snake.Constants.*;

public class Sprite extends Image {

    public Sprite(String url) {
        super(url, TILE_SIZE, TILE_SIZE, true, false);
    }
}
