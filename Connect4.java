import java.awt.*;
import javax.swing.*;

import org.example.GameGUI;

public class Connect4 {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameGUI gui = new GameGUI();
            gui.setVisible(true);
        });
    }
}

