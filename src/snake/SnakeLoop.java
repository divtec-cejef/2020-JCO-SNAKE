package snake;

import javafx.scene.canvas.GraphicsContext;

//public class SnakeLoop implements Runnable {
//public class SnakeLoop {
public class SnakeLoop {

    private final Grid grid;
    private final GraphicsContext context;
//    private int frameRate;
//    private float interval;
    private boolean running;
    private boolean paused;
    private boolean keyIsPressed;

    /**
     * Crée une boucle
     * @param grid Grille du jeu
     * @param context GraphicsContext
     */
    public SnakeLoop(final Grid grid, final GraphicsContext context) {
        this.grid = grid;
        this.context = context;
//        frameRate = 60;
//        interval = 8000.0f / frameRate; // 1000 ms en une seconde
        running = true;
        paused = false;
        keyIsPressed = false;
    }

//    @Override
//    public void run() {
//        while (running && !paused) {
//            float time = System.currentTimeMillis();
//
//            keyIsPressed = false;
//            grid.update();
//            Painter.paint(grid, context);
//
//            if (grid.getSnake().isDead()) {
//                pause();
//                Painter.paintResetMessage(context);
//                break;
//            }
//
//            time = System.currentTimeMillis() - time;
//
//            if (time < interval) {
//                try {
//                    Thread.sleep((long) (interval - time));
//                } catch (InterruptedException ignore) {
//                }
//            }
//        }
//    }

    /**
     * Stop le jeu
     */
    public void stop() {
        running = false;
    }

    /**
     * @return si une touche est pressée
     */
    public boolean isKeyPressed() {
        return keyIsPressed;
    }

    /**
     * Défini qu'une touche est pressée
     */
    public void setKeyPressed() {
        keyIsPressed = true;
    }

    /**
     * Relance le jeu
     */
    public void resume() {
        paused = false;
    }

    /**
     * Mets le jeu sur pause
     */
    public void pause() {
        paused = true;
    }

    /**
     * @return si le jeu est en pause
     */
    public boolean isPaused() {
        return paused;
    }

    /**
     * @return si le jeu est en cours
     */
    public boolean isRunning() {
        return running;
    }

}
