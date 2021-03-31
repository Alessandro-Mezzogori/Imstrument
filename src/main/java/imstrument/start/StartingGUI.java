package imstrument.start;

import imstrument.globals.GlobalSetting;
import imstrument.globals.WrapperCardLayout;
import imstrument.homepage.Homepage;
import imstrument.imagepage.Imagepage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class StartingGUI extends JFrame {
    JPanel cards;

    public StartingGUI(){
        this.setTitle("Imstrument");

        WrapperCardLayout cardsLayout = new WrapperCardLayout();
        cards = new JPanel(cardsLayout);
        JPanel homepage = new Homepage(cardsLayout);
        JPanel imagepage = new Imagepage(cardsLayout);

        cards.add(homepage, WrapperCardLayout.CardId.HOMEPAGE);
        cards.add(imagepage, WrapperCardLayout.CardId.IMAGEPAGE);

        Container container = this.getContentPane();
        container.setLayout(new BorderLayout());
        container.add(cards, BorderLayout.CENTER);

        this.addWindowListener(new NavigationRules());

        pack();
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setVisible(true); // shows jframe
        requestFocus(); // requestes focus for event dispatching
    }

    private class NavigationRules implements WindowListener{

        @Override
        public void windowOpened(WindowEvent e) {

        }

        @Override
        public void windowClosing(WindowEvent e) {
            WrapperCardLayout cardLayout = (WrapperCardLayout) cards.getLayout();
            switch (cardLayout.getCurrentCardId()){
                case HOMEPAGE -> dispose();
                case IMAGEPAGE -> cardLayout.show(cards, WrapperCardLayout.CardId.HOMEPAGE);
            }
        }

        @Override
        public void windowClosed(WindowEvent e) {

        }

        @Override
        public void windowIconified(WindowEvent e) {

        }

        @Override
        public void windowDeiconified(WindowEvent e) {

        }

        @Override
        public void windowActivated(WindowEvent e) {

        }

        @Override
        public void windowDeactivated(WindowEvent e) {

        }
    }
}
