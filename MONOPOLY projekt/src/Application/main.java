package Application;

import javax.swing.*;
import java.util.ArrayList;

public class main {
    public static void main(String[] args){
//        System.out.print("Hello Monopoly");
//        System.out.print("...");
        ArrayList<field> fields = new ArrayList<field>();
        field pole1;
        pole1 = new field("Londyn");
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JButton b = new JButton(pole1.FieldName);
        b.setBounds(100,210,200,40);

        f.add(b);

        f.setSize(400,500);
        f.setLayout(null);
        f.setVisible(true);
    }
}
