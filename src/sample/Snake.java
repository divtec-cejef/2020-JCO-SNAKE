package sample;

import java.awt.EventQueue;
import javax.swing.JFrame;

/**
 * Classe qui représente un serpent
 */
public class Snake extends JFrame {
    public Snake() {
        initUI();
    }

    private void initUI() {

        add(new Board());

        setResizable(false);
        setSize(1500, 1500);
        pack();

        setTitle("Snake");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {
            JFrame ex = new Snake();
            ex.setVisible(true);
        });
    }
}
