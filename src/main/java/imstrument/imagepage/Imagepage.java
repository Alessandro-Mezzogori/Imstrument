package imstrument.imagepage;

/* imstrument packages */

import imstrument.globals.ImagePanel;

import javax.swing.*;
import java.awt.*;

/*
    TODO temporary Jpanel to test card layout
 */

public class Imagepage extends JPanel {
    Label credits; // used to display creators and maybe copyright
    ImagePanel imstrumentLogo;
    JButton startButton;
    public Imagepage(){
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
        buttonConstraints.gridx = 1;
        buttonConstraints.gridy = 1;
        buttonConstraints.gridwidth = 1;
        buttonConstraints.gridheight = 1;
        buttonConstraints.weighty = 0.25;
        buttonConstraints.weightx = 0.5;
        buttonConstraints.anchor = GridBagConstraints.CENTER;
        buttonConstraints.fill = GridBagConstraints.HORIZONTAL;
        buttonConstraints.insets = new Insets(100, 200, 50, 200);
        startButton = new JButton("Start");
        this.add(startButton, buttonConstraints);

        /* credits label definition */
        GridBagConstraints creditsConstraints = new GridBagConstraints();
        creditsConstraints.gridx = 0;
        creditsConstraints.gridy = buttonConstraints.gridy + 1; // always after last button
        creditsConstraints.weightx = 1.0;
        creditsConstraints.weighty = 0.2;
        creditsConstraints.gridwidth = 3;
        credits = new Label("imagepage created by Villani Luca and Mezzogori Alessandro");
        this.add(credits, creditsConstraints);


    }
}
