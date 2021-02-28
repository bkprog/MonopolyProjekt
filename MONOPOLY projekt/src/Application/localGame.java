package Application;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class localGame extends JFrame{
    public void drawWindow(){
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(EXIT_ON_CLOSE);

        field pole;
        pole = new field();


        window.add(pole);
        pole.drawing();

        window.setSize(1080,720);
        window.setLayout(null);
        window.setVisible(true);
    }
    public localGame(){
        drawWindow();
    }
}
