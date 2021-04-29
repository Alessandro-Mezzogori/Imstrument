package imstrument.virtualkeyboard;

import imstrument.start.StartApp;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class VirtualKeyboard extends JFrame {
    private PianoKey[] keys;
    private final int whiteKeyNumber = 7;
    private final int blackKeyNumber = 5;
    private final int keyNumber = whiteKeyNumber+blackKeyNumber;
    public VirtualKeyboard() {
        //Create the GUI
        setTitle("Imstrument Piano Keyboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        /* Init Array for the PianoKeys */
        keys= new PianoKey[keyNumber];

        /* Create the main panel for the keyboard */
        Container mainPanel = getContentPane();
        mainPanel.setLayout((new BoxLayout(mainPanel, BoxLayout.Y_AXIS)));
        mainPanel.setForeground(Color.WHITE);
        mainPanel.setBackground(Color.GRAY);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        /* Piano Keys Panel
        Call the create keyboard method */
        JLayeredPane pianoKeyboard = createKeyboard();
        //Add at the main panel to the keyboard
        mainPanel.add(pianoKeyboard);

        setVisible(true);
        setResizable(false);
        setSize(500, 200);

    }

    /**
     * Creates the panel containing all the piano keys
     *
     * @return the panel with the keys
     */
    private JLayeredPane createKeyboard() {

        // Init
        int x = 55;
        int y = 0;
        int whiteDistance = 37;

        // Create the layerPane
        JLayeredPane keyBoard = new JLayeredPane();
        keyBoard.setPreferredSize(new Dimension(900, 162));
        keyBoard.add(Box.createRigidArea(new Dimension(x, 0)));

        // Adding all the white buttons in the keyboard
        for (int i = 0; i < whiteKeyNumber; i++) {
            keys[i] = new PianoKey(i, PianoKey.KeyColor.WHITE);
            keys[i].addMouseListener(new PianoKeyAdapter());
            keys[i].setBounds(x, y, 35, 162);
            keyBoard.add(keys[i], 1);
            if (i != whiteKeyNumber - 1) {
                keyBoard.add(Box.createRigidArea(new Dimension(2, 0)));
            }
            x += whiteDistance;
        }

        keyBoard.add(Box.createRigidArea(new Dimension(60, 0)));
        // Creating and adding black keys
            x = 77;
        int blackStandardDistance = 38;
        int blackModifiedDistance = 73;
        for (int i= whiteKeyNumber; i < keyNumber; ++i) {
            keys[i] = new PianoKey(i, PianoKey.KeyColor.BLACK);
            keys[i].addMouseListener(new PianoKeyAdapter());
            keys[i].setBounds(x,y,25,95);
            if (i != whiteKeyNumber+1){
                x += blackStandardDistance;
            }
            else{
                x += blackModifiedDistance;
            }
            keyBoard.add(keys[i], 2);
        }

        return keyBoard;
    }

    public void setPressed(int index, boolean value){
        if(index < keys.length){
            keys[index].setPressed(value);
        }
        System.out.println("Implement second row");
    }

    private class PianoKeyAdapter extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
            PianoKey pianoKey = (PianoKey) e.getSource();
            pianoKey.setPressed(true);
            StartApp.waveManager.triggerWaveGeneration(pianoKey.getId());
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            PianoKey pianoKey = (PianoKey) e.getSource();
            pianoKey.setPressed(false);
            StartApp.waveManager.startWaveRelease(pianoKey.getId());
        }
    }
}
