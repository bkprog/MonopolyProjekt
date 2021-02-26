package Application;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class welcomeWindow extends JFrame {


    public static void drawWindow(){
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(EXIT_ON_CLOSE);
        f.setTitle("Monopoly");



        JLabel logoGame = new JLabel("Monopoly");
        JButton playLocal = new JButton("Graj Lokalnie");
        JButton playOnline = new JButton("Graj Online");
        logoGame.setFont(new Font("Verdana", Font.PLAIN, 50));
        playLocal.setFont(new Font("Verdana", Font.PLAIN, 25));
        playOnline.setFont(new Font("Verdana", Font.PLAIN, 25));

        logoGame.setBounds(420,50,400,100);
        playLocal.setBounds(440,200,200,60);
        playOnline.setBounds(440,300,200,60);

        playLocal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                localGame localgame = new localGame();
                f.setVisible(false);
            }
        });

        f.add(logoGame);
        f.add(playLocal);
        f.add(playOnline);


        f.setSize(1080,720);
        f.setLayout(null);
        f.setVisible(true);
    }
    public welcomeWindow(){
        drawWindow();
    }
}
