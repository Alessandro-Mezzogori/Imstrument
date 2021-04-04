package imstrument.homepage;

/* imstrument packages */
import imstrument.globals.*;
import imstrument.imagepage.Imagepage;

/* general packages*/
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * specialized JPanel to show the homepage of the application Imstrument
 */
public class Homepage extends JFrame { /* Logic attributes */
     /* GUI components */
    Label credits; // used to display creators and maybe copyright
    ImagePanel imstrumentLogo;
    JButton startButton;
    public Homepage(){
        super();
        /* initialize components */

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
        imstrumentLogo = new ImagePanel(this.getClass().getResource("/imstrument/globals/imstrument_image.png"));
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
                    SwingUtilities.invokeLater(Imagepage::new);
                    dispose();
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

        setMinimumSize(GlobalSetting.MINIMUM_WINDOW_SIZE);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true); // shows jframe
        requestFocus(); // requestes focus for event dispatching

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                imstrumentLogo.closeAudioThread();
                super.windowClosing(e);
            }


        });
    }
}
