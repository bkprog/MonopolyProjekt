package Application;

import java.awt.*;
import javax.swing.*;


public class field extends JPanel{
//    String FieldName;

//    public field(String name){
//        FieldName = name;
//    }

    public void drawing(){
        setVisible(true);

        repaint();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawRect(20, 20, 200, 200);
        g.setColor(Color.BLUE);
        g.fillRect(20, 20, 200, 200);
    }


}
