package imstrument.imagepage;

/* imstrument packages */

import imstrument.globals.ImagePanel;
import imstrument.homepage.Homepage;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * specialized JPanel to show the imagepage of the application Imstrument
 */
public class Imagepage extends JFrame {
    /* GUI components */
    JMenuBar menuBar;
    ImagePanel imagePanel;
    public Imagepage(){
        /* initialize components */
        /* create menubar */
        menuBar = new JMenuBar();

        JButton logoIcon = new JButton();
        logoIcon.setBorderPainted(false);
        logoIcon.setBorder(null);
        logoIcon.setMargin(new Insets(0, 0, 0, 0));
        logoIcon.setContentAreaFilled(false);
        try {
            /*
             * TODO per imstrument_logo -> mettere il background bianco trasparente tramite convertitori online, photoshop o paint.net
             */
            BufferedImage logoImage = ImageIO.read(this.getClass().getResource("/imstrument/globals/imstrument_logo.png"));
            logoIcon.setIcon(new ImageIcon(logoImage));
        } catch (IOException e) {
            logoIcon.setText("Homepage");
        }
        logoIcon.addActionListener(
                e -> {
                    SwingUtilities.invokeLater(Homepage::new);
                    dispose();
                }
        );
        menuBar.add(logoIcon);

        /* import menu */
        // TODO import video
        JMenuItem importImage = new JMenuItem("Image");
        importImage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                importImage();
            }
        });

        JMenu importMenu = new JMenu("Import");
        importMenu.add(importImage);

        menuBar.add(importMenu);

        /* algorythms menu */
        // TODO lista degli algoritmi
        JMenu algorithmsMenu = new JMenu("Algorithms");
        menuBar.add(algorithmsMenu);

        /* visualize menu */
        // TODO visualizzatore note
        // TODO mappa note
        JMenu visualizeMenu = new JMenu("Visualize");
        menuBar.add(visualizeMenu);

        /* mp3 management menu */
        //TODO record playing
        JMenu mp3Menu = new JMenu("MP3");
        menuBar.add(mp3Menu);

        /* image panel */
        this.imagePanel = new ImagePanel();
        /* jframe settings and params */
        this.setLayout(new BorderLayout());
        this.add(menuBar, BorderLayout.NORTH);
        this.add(imagePanel, BorderLayout.CENTER);

        setPreferredSize(new Dimension(800, 500));
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true); // shows jframe
        requestFocus(); // requestes focus for event dispatching
    }

    private void importImage(){
        //TODO compatibilità gif
        JFileChooser imageChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "JPG & PNG", "jpg", "png"
        );
        imageChooser.setFileFilter(filter);
        int choosingResult = imageChooser.showOpenDialog(this);
        if(choosingResult == JFileChooser.APPROVE_OPTION){
            try {
                this.imagePanel.setImage(ImageIO.read(imageChooser.getSelectedFile()));
            } catch (IOException e) {
                //TODO notifica utente in caso di errore e sopprimi chiusura
                e.printStackTrace();
            }
            System.out.println(imageChooser.getSelectedFile().getName());
        }
    }
}
