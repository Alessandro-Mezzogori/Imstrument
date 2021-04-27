package imstrument.imagepage;

/* imstrument packages */

import imstrument.algorithm.HorizontalAlgorithm;
import imstrument.sound.utils.SoundImagePanel;
import imstrument.start.StartApp;
import imstrument.start.TopContainer;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * specialized JPanel to show the imagepage of the application Imstrument
 */
public class Imagepage extends JPanel {
    /* GUI components */
    JMenuBar menuBar;
    SoundImagePanel soundImagePanel;

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
                    // TODO better method to open new jframe
                    JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor((JComponent) e.getSource());
                    if(topFrame instanceof TopContainer){
                        TopContainer topContainer = (TopContainer) topFrame;
                        topContainer.changeCard(TopContainer.HOMEPAGE);
                    }
                }
        );

        menuBar.add(logoIcon);

        /* import menu */
        // TODO import video
        JMenuItem importImage = new JMenuItem("Image");
        importImage.addActionListener(e -> importImage());

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
        visualizeMenu.add(new JMenuItem("Keyboard"));

        menuBar.add(visualizeMenu);

        /* mp3 management menu */
        //TODO record playing
        JMenu mp3Menu = new JMenu("MP3");
        menuBar.add(mp3Menu);

        /* image panel */
        this.soundImagePanel = new SoundImagePanel();
        this.soundImagePanel.setSoundAlgorithm(new HorizontalAlgorithm());

        /* jframe settings and params */
        this.setLayout(new BorderLayout());
        this.add(menuBar, BorderLayout.NORTH);
        this.add(soundImagePanel, BorderLayout.CENTER);

        setKeyboardBindings();
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
                this.soundImagePanel.setImage(ImageIO.read(imageChooser.getSelectedFile()));
            } catch (IOException e) {
                //TODO notifica utente in caso di errore e sopprimi chiusura
                e.printStackTrace();
            }
            //System.out.println(imageChooser.getSelectedFile().getName());
        }
    }

    private void setKeyboardBindings(){
        // TODO add stop sound when application is out of focus

        InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, false), "CT_T");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_3, 0, false), "C#T_T");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_E, 0, false), "DT_T");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_4, 0, false), "D#T_T");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_R, 0, false), "ET_T");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_T, 0, false), "FT_T");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_6, 0, false), "F#T_T");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_Y, 0, false), "GT_T");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_7, 0, false), "G#T_T");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_U, 0, false), "AT_T");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_8, 0, false), "A#T_T");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_I, 0, false), "BT_T");

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, 0, false), "CB_T");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, false), "C#B_T");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_X, 0, false), "DB_T");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, false), "D#B_T");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_C, 0, false), "EB_T");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_V, 0, false), "FB_T");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_G, 0, false), "F#B_T");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_B, 0, false), "GB_T");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_H, 0, false), "G#B_T");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_N, 0, false), "AB_T");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_J, 0, false), "A#B_T");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_M, 0, false), "BB_T");

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, true), "CT_R");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_3, 0, true), "C#T_R");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_E, 0, true), "DT_R");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_4, 0, true), "D#T_R");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_R, 0, true), "ET_R");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_T, 0, true), "FT_R");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_6, 0, true), "F#T_R");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_Y, 0, true), "GT_R");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_7, 0, true), "G#T_R");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_U, 0, true), "AT_R");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_8, 0, true), "A#T_R");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_I, 0, true), "BT_R");

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, 0, true), "CB_R");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, true), "C#B_R");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_X, 0, true), "DB_R");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, true), "D#B_R");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_C, 0, true), "EB_R");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_V, 0, true), "FB_R");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_G, 0, true), "F#B_R");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_B, 0, true), "GB_R");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_H, 0, true), "G#B_R");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_N, 0, true), "AB_R");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_J, 0, true), "A#B_R");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_M, 0, true), "BB_R");

        Action onPressed = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!StartApp.audioThread.isRunning()) {
                    StartApp.audioThread.triggerPlayback();
                }

                switch (e.getActionCommand()){
                    case "w" -> StartApp.waveManager.triggerWaveGeneration(1);
                    case "3" -> StartApp.waveManager.triggerWaveGeneration(2);
                    case "e" -> StartApp.waveManager.triggerWaveGeneration(3);
                    case "4" -> StartApp.waveManager.triggerWaveGeneration(4);
                    case "r" -> StartApp.waveManager.triggerWaveGeneration(5);
                    case "t" -> StartApp.waveManager.triggerWaveGeneration(6);
                    case "6" -> StartApp.waveManager.triggerWaveGeneration(7);
                    case "y" -> StartApp.waveManager.triggerWaveGeneration(8);
                    case "7" -> StartApp.waveManager.triggerWaveGeneration(9);
                    case "u" -> StartApp.waveManager.triggerWaveGeneration(10);
                    case "8" -> StartApp.waveManager.triggerWaveGeneration(11);
                    case "i" -> StartApp.waveManager.triggerWaveGeneration(12);
                    case "z" -> StartApp.waveManager.triggerWaveGeneration(13);
                    case "s" -> StartApp.waveManager.triggerWaveGeneration(14);
                    case "x" -> StartApp.waveManager.triggerWaveGeneration(15);
                    case "d" -> StartApp.waveManager.triggerWaveGeneration(16);
                    case "c" -> StartApp.waveManager.triggerWaveGeneration(17);
                    case "v" -> StartApp.waveManager.triggerWaveGeneration(18);
                    case "g" -> StartApp.waveManager.triggerWaveGeneration(19);
                    case "b" -> StartApp.waveManager.triggerWaveGeneration(20);
                    case "h" -> StartApp.waveManager.triggerWaveGeneration(21);
                    case "n" -> StartApp.waveManager.triggerWaveGeneration(22);
                    case "j" -> StartApp.waveManager.triggerWaveGeneration(23);
                    case "m" -> StartApp.waveManager.triggerWaveGeneration(24);
                }
            }
        };

        Action onRelease = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switch (e.getActionCommand()){
                    case "w" -> StartApp.waveManager.startWaveRelease(1);
                    case "3" -> StartApp.waveManager.startWaveRelease(2);
                    case "e" -> StartApp.waveManager.startWaveRelease(3);
                    case "4" -> StartApp.waveManager.startWaveRelease(4);
                    case "r" -> StartApp.waveManager.startWaveRelease(5);
                    case "t" -> StartApp.waveManager.startWaveRelease(6);
                    case "6" -> StartApp.waveManager.startWaveRelease(7);
                    case "y" -> StartApp.waveManager.startWaveRelease(8);
                    case "7" -> StartApp.waveManager.startWaveRelease(9);
                    case "u" -> StartApp.waveManager.startWaveRelease(10);
                    case "8" -> StartApp.waveManager.startWaveRelease(11);
                    case "i" -> StartApp.waveManager.startWaveRelease(12);
                    case "z" -> StartApp.waveManager.startWaveRelease(13);
                    case "s" -> StartApp.waveManager.startWaveRelease(14);
                    case "x" -> StartApp.waveManager.startWaveRelease(15);
                    case "d" -> StartApp.waveManager.startWaveRelease(16);
                    case "c" -> StartApp.waveManager.startWaveRelease(17);
                    case "v" -> StartApp.waveManager.startWaveRelease(18);
                    case "g" -> StartApp.waveManager.startWaveRelease(19);
                    case "b" -> StartApp.waveManager.startWaveRelease(20);
                    case "h" -> StartApp.waveManager.startWaveRelease(21);
                    case "n" -> StartApp.waveManager.startWaveRelease(22);
                    case "j" -> StartApp.waveManager.startWaveRelease(23);
                    case "m" -> StartApp.waveManager.startWaveRelease(24);
                }
            }
        };

        ActionMap actionMap = getActionMap();

        actionMap.put("CT_T", onPressed);
        actionMap.put("C#T_T", onPressed);
        actionMap.put("DT_T", onPressed);
        actionMap.put("D#T_T", onPressed);
        actionMap.put("ET_T", onPressed);
        actionMap.put("FT_T", onPressed);
        actionMap.put("F#T_T", onPressed);
        actionMap.put("GT_T", onPressed);
        actionMap.put("G#T_T", onPressed);
        actionMap.put("AT_T", onPressed);
        actionMap.put("A#T_T", onPressed);
        actionMap.put("BT_T", onPressed);

        actionMap.put("CB_T", onPressed);
        actionMap.put("C#B_T", onPressed);
        actionMap.put("DB_T", onPressed);
        actionMap.put("D#B_T", onPressed);
        actionMap.put("EB_T", onPressed);
        actionMap.put("FB_T", onPressed);
        actionMap.put("F#B_T", onPressed);
        actionMap.put("GB_T", onPressed);
        actionMap.put("G#B_T", onPressed);
        actionMap.put("AB_T", onPressed);
        actionMap.put("A#B_T", onPressed);
        actionMap.put("BB_T", onPressed);

        actionMap.put("CT_R", onRelease);
        actionMap.put("C#T_R", onRelease);
        actionMap.put("DT_R", onRelease);
        actionMap.put("D#T_R", onRelease);
        actionMap.put("ET_R", onRelease);
        actionMap.put("FT_R", onRelease);
        actionMap.put("F#T_R", onRelease);
        actionMap.put("GT_R", onRelease);
        actionMap.put("G#T_R", onRelease);
        actionMap.put("AT_R", onRelease);
        actionMap.put("A#T_R", onRelease);
        actionMap.put("BT_R", onRelease);

        actionMap.put("CB_R", onRelease);
        actionMap.put("C#B_R", onRelease);
        actionMap.put("DB_R", onRelease);
        actionMap.put("D#B_R", onRelease);
        actionMap.put("EB_R", onRelease);
        actionMap.put("FB_R", onRelease);
        actionMap.put("F#B_R", onRelease);
        actionMap.put("GB_R", onRelease);
        actionMap.put("G#B_R", onRelease);
        actionMap.put("AB_R", onRelease);
        actionMap.put("A#B_R", onRelease);
        actionMap.put("BB_R", onRelease);
    }
}
