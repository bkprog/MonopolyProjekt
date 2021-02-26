package Application;

import javax.swing.*;
import java.awt.*;

public class localGame extends JFrame{
    public static void drawWindow(){
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(EXIT_ON_CLOSE);

        window.setSize(1080,720);
        window.setLayout(null);
        window.setVisible(true);
    }
    public localGame(){
        drawWindow();
    }
}
