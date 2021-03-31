package imstrument.start;

import imstrument.homepage.Homepage;
import imstrument.imagepage.Imagepage;

import javax.swing.*;
import java.awt.*;

public class StartingGUI extends JFrame {
    public StartingGUI(){
        Container container = getContentPane();
        container.setLayout(new CardLayout());

        JPanel homepage = new Homepage();
        JPanel imagepage = new Imagepage();
        container.add(homepage, "Homepage");
        container.add(imagepage, "Imagepage");

        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true); // shows jframe
        requestFocus(); // requestes focus for event dispatching
    }
}
