package imstrument.imagepage;

/* imstrument packages */

import imstrument.globals.ImagePanel;
import imstrument.globals.WrapperCardLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Imagepage extends JPanel {
    /* Logic attributes */
    private final WrapperCardLayout containerLayout;
    /* GUI components */
    Label credits; // used to display creators and maybe copyright
    ImagePanel imstrumentLogo;
    public Imagepage(final WrapperCardLayout containerLayout){
        /* initialize components */
        this.containerLayout = containerLayout;

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



        /* credits label definition */
        GridBagConstraints creditsConstraints = new GridBagConstraints();
        creditsConstraints.gridx = 0;
        creditsConstraints.gridy = 1; // always after last button
        creditsConstraints.weightx = 1.0;
        creditsConstraints.weighty = 0.2;
        creditsConstraints.gridwidth = 3;
        credits = new Label("created by Villani Luca and Mezzogori Alessandro");
        this.add(credits, creditsConstraints);
    }
}
