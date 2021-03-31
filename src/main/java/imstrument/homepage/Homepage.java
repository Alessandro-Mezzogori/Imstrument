package imstrument.homepage;

/* imstrument packages */
import imstrument.globals.*;
import imstrument.imagepage.Imagepage;
import imstrument.start.StartingGUI;

/* general packages*/
import javax.imageio.ImageIO;
import javax.sound.midi.SysexMessage;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.net.URL;

public class Homepage extends JPanel {
    /* Logic attributes */
    String name;

    /* GUI components */
    Label credits; // used to display creators and maybe copyright
    ImagePanel imstrumentLogo;
    JButton startButton;
    public Homepage(){
        /* set jframe params */
        this.setLayout(new GridBagLayout());
        /* create components*/
        /*
        * bottom component
        * contains the credits of the creators and TODO copyright of software application
        * */


        /* logo definition */
        GridBagConstraints imageConstraints = new GridBagConstraints();
        imageConstraints.gridx = 0;
        imageConstraints.gridy = 0;
        imageConstraints.gridwidth = 3;
        imageConstraints.gridheight = 1;
        imageConstraints.weightx = 1;
        imageConstraints.weighty = 0.33;
        imageConstraints.anchor = GridBagConstraints.CENTER;
        imageConstraints.fill = GridBagConstraints.BOTH;
        imstrumentLogo = new ImagePanel(this.getClass().getResource("/imstrument/globals/imstrument_logo.png"));
        this.add(imstrumentLogo, imageConstraints);

        /* button definition */
        GridBagConstraints buttonConstraints = new GridBagConstraints();
        buttonConstraints.anchor = GridBagConstraints.CENTER;
        buttonConstraints.fill = GridBagConstraints.HORIZONTAL;

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        startButton = new JButton("Start");
        startButton.addActionListener(
            new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // TODO better method to open new jframe
                    StartingGUI startingGUI = (StartingGUI) SwingUtilities.getWindowAncestor(getParent());
                    startingGUI.showCard(GlobalSetting.CardId.IMAGEPAGE);
                }
            }
        );
        buttonPanel.add(startButton, buttonConstraints);

        buttonConstraints.gridx = 1;
        buttonConstraints.gridy = 1;
        buttonConstraints.weightx = 1.0;
        buttonConstraints.weighty = 0.33;
        this.add(buttonPanel, buttonConstraints);

        /* credits label definition */
        GridBagConstraints creditsConstraints = new GridBagConstraints();
        creditsConstraints.gridx = 0;
        creditsConstraints.gridy = buttonConstraints.gridy + 1; // always after last button
        creditsConstraints.weightx = 1.0;
        creditsConstraints.weighty = 0.2;
        creditsConstraints.gridwidth = 3;
        credits = new Label("created by Villani Luca and Mezzogori Alessandro");
        this.add(credits, creditsConstraints);
    }
}
