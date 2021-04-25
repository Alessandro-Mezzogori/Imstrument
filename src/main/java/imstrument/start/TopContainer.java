package imstrument.start;

import imstrument.globals.GlobalSetting;
import imstrument.homepage.Homepage;
import imstrument.imagepage.Imagepage;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class TopContainer extends JFrame {
    public final static String HOMEPAGE = "Homepage";
    public final static String IMAGEPAGE = "Imagepage";

    JPanel cards;

    public TopContainer(){
        cards = new JPanel(new CardLayout());
        cards.add(new Homepage(), HOMEPAGE);
        cards.add(new Imagepage(), IMAGEPAGE);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                StartApp.audioThread.close();
            }
        });

        setLayout(new BorderLayout());
        add(cards, BorderLayout.CENTER);

        setMinimumSize(GlobalSetting.MINIMUM_WINDOW_SIZE);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true); // shows jframe
        requestFocus(); // requestes focus for event dispatching
    }

    public void changeCard(String cardID){
        CardLayout cardLayout = (CardLayout) cards.getLayout();
        cardLayout.show(cards, cardID);
    }
}
