package Application;

import javax.swing.*;
import java.util.ArrayList;

public class main {
    public static void main(String[] args){
//        System.out.print("Hello Monopoly");
//        System.out.print("...");
//        ArrayList<field> fields = new ArrayList<field>();
//        field pole1;
        ArrayList<player> players = new ArrayList<player>();

        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton button1 = new JButton("Add Player");
        JButton button2 = new JButton("Add Player");
        JButton button3 = new JButton("Add Player");
        JButton button4 = new JButton("Add Player");

        JTextField tf = new JTextField("wpisz cos");

        button1.setBounds(100,210,200,40);
        button2.setBounds(300,210,200,40);
        button3.setBounds(500,210,200,40);
        button4.setBounds(700,210,200,40);
        tf.setBounds(350,410,200,40);

        f.add(button1);
        f.add(button2);
        f.add(button3);
        f.add(button4);
        f.add(tf);


//        button2.setVisible(false);
//        button3.setVisible(false);
//        button4.setVisible(false);



        f.setSize(1080,720);
        f.setLayout(null);
        f.setVisible(true);
    }
}
