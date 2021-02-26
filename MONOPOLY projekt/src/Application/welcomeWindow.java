package Application;

import javax.swing.*;

public class welcomeWindow {
    public static void drawWindow(){
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setTitle("Monopoly nazwa");

        JTextField tf = new JTextField("wpisz cos");

        tf.setBounds(350,410,200,40);

        f.add(tf);

        f.setSize(1080,720);
        f.setLayout(null);
        f.setVisible(true);
    }
    public welcomeWindow(){
        drawWindow();
    }
}
