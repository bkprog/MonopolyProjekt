package Application;

import javax.swing.*;

public class welcomeWindow {
    public static void drawWindow(){
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setTitle("Monopoly");
        JLabel info = new JLabel("Wpisz swój nick");
        JTextField tf = new JTextField("Player");

        info.setBounds(100,120,200,40);
        tf.setBounds(100,160,200,40);

        f.add(tf);
        f.add(info);

        f.setSize(400,400);
        f.setLayout(null);
        f.setVisible(true);
    }
    public welcomeWindow(){
        drawWindow();
    }
}
