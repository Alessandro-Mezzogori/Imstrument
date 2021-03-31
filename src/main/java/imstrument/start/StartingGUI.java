package imstrument.start;

import imstrument.globals.CardsPanel;
import imstrument.globals.GlobalSetting;
import imstrument.homepage.Homepage;
import imstrument.imagepage.Imagepage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class StartingGUI extends JFrame {
    JPanel cards;
    private GlobalSetting.CardId currentCardId;

    public StartingGUI(){
        this.setTitle("Imstrument");

        JPanel homepage = new Homepage();
        JPanel imagepage = new Imagepage();

        cards = new JPanel(new CardLayout());
        cards.add(homepage, GlobalSetting.CardId.HOMEPAGE.toString());
        cards.add(imagepage, GlobalSetting.CardId.IMAGEPAGE.toString());
        showCard(GlobalSetting.CardId.HOMEPAGE);

        Container container = this.getContentPane();
        container.setLayout(new BorderLayout());
        container.add(cards, BorderLayout.CENTER);

        this.addWindowListener(new NavigationRules());

        pack();
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setVisible(true); // shows jframe
        requestFocus(); // requestes focus for event dispatching
    }

    public void showCard(GlobalSetting.CardId cardId){
        this.currentCardId = cardId;
        CardLayout cardLayout = (CardLayout) cards.getLayout();
        cardLayout.show(cards, this.currentCardId.toString());
    }

    private class NavigationRules implements WindowListener{

        @Override
        public void windowOpened(WindowEvent e) {

        }

        @Override
        public void windowClosing(WindowEvent e) {
            switch (currentCardId){
                case HOMEPAGE -> dispose();
                case IMAGEPAGE -> showCard(GlobalSetting.CardId.HOMEPAGE);
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
