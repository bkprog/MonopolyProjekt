import javax.swing.*;

public class main {
    public static void main(String[] args){
//        System.out.print("Hello Monopoly");
//        System.out.print("...");
        JFrame f = new JFrame();

        JButton b = new JButton("kliknij mnie!");
        b.setBounds(100,210,200,40);

        f.add(b);

        f.setSize(400,500);
        f.setLayout(null);
        f.setVisible(true);
    }
}
