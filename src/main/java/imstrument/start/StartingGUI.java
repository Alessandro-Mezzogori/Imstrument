package imstrument.start;

import imstrument.globals.WrappedCardLayout;
import imstrument.homepage.Homepage;
import imstrument.imagepage.Imagepage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class StartingGUI extends JFrame {
    /**
     * contains the JPanel with a WrapperCardLayout to be accessed at a later time
     */
    JPanel cards;

    /**
     * starting method of the Imstrument application
     */
    public StartingGUI(){
        this.setTitle("Imstrument");

        WrappedCardLayout cardsLayout = new WrappedCardLayout();
        cards = new JPanel(cardsLayout);
        JPanel homepage = new Homepage();
        JPanel imagepage = new Imagepage();

        cards.add(homepage, WrappedCardLayout.CardNames.HOMEPAGE);
        cards.add(imagepage, WrappedCardLayout.CardNames.IMAGEPAGE);

        Container container = this.getContentPane();
        container.setLayout(new BorderLayout());
        container.add(cards, BorderLayout.CENTER);


        pack();
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new NavigationRules());
        setVisible(true); // shows jframe
        requestFocus(); // requestes focus for event dispatching
    }

    /**
     * wrapper for overrides of windows listener events to create a more complex navigation experience
     */
    private class NavigationRules implements WindowListener{
        @Override
        public void windowOpened(WindowEvent e) {

        }

        @Override
        public void windowClosing(WindowEvent e) {
            WrappedCardLayout cardLayout = (WrappedCardLayout) cards.getLayout();
            switch (cardLayout.getCurrentCardName()){
                case WrappedCardLayout.CardNames.HOMEPAGE -> dispose();
                case WrappedCardLayout.CardNames.IMAGEPAGE -> cardLayout.show(cards, WrappedCardLayout.CardNames.HOMEPAGE);
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
