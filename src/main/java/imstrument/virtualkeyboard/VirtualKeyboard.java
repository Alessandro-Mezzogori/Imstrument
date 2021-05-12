package imstrument.virtualkeyboard;

import imstrument.start.StartApp;
import org.lwjgl.system.CallbackI;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class VirtualKeyboard extends JFrame implements ActionListener {
    private PianoKey[] keys;
    final boolean[] isWhite = new boolean[]{true, false, true, false, true, true, false, true, false, true, false, true};

    public VirtualKeyboard() {
        //Create the GUI
        setTitle("Imstrument Piano Keyboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        /* Init Array for the PianoKeys */
        keys= new PianoKey[isWhite.length];

        /* Create the main panel for the keyboard */
        Container mainPanel = getContentPane();
        mainPanel.setLayout((new BoxLayout(mainPanel, BoxLayout.Y_AXIS)));
        mainPanel.setForeground(Color.WHITE);
        mainPanel.setBackground(Color.GRAY);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        int numberOfKeyboards = 2;
        for (int i = 0; i < numberOfKeyboards; i++) {
            //creating the notes panel
            JPanel notesPanel = createNotes();

            /* adding to the main panel*/
            mainPanel.add(notesPanel);
            mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

            /* Creating the button for switching octaves*/
            JButton switchRight = createSwitch('r');
            JButton switchLeft  = createSwitch('l');

            /* Creating the pianokeyboard */
            JLayeredPane pianoKeyboard = createKeyboard(i);

            /* Creating the panel where we can put the piano and the two buttons*/
            JPanel pianoPanel = new JPanel();
            pianoPanel.setLayout(new BoxLayout(pianoPanel,BoxLayout.X_AXIS));
            pianoPanel.setBackground(Color.GRAY);
            pianoPanel.add(switchLeft);
            pianoPanel.add(Box.createRigidArea(new Dimension(10,0)));
            pianoPanel.add(pianoKeyboard);
            pianoPanel.add(Box.createRigidArea(new Dimension(10,0)));
            pianoPanel.add(switchRight);

            mainPanel.add(pianoPanel);
            mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        setVisible(true);
        setResizable(false);
        setSize(570, 500);

    }

    /**
     * Creates the panel containing all the piano keys
     *
     * @return the panel with the keys
     */
    private JLayeredPane createKeyboard(int keyboardIndex) {
        // Create the layerPane
        JLayeredPane keyBoard = new JLayeredPane();
        keyBoard.setPreferredSize(new Dimension(350, 160));

        /* coordinates parameters for rendering */
        final int y = 0;

        int whiteX = 55; // starting white X coord
        final int whiteDistance = 37; // distance between the top left corners of white piano keys

        int blackX = 77; // starting black X coord
        final int blackStandardDistance = 38; // distance between the top left corners of black piano keys
        final int blackModifiedDistance = 73; // jump between the 2-group and 3-group of black keys
        int blackCount = 0; // index used keep track of how many black keys have been rendered

        keyBoard.add(Box.createRigidArea(new Dimension(55, 0)));
        for(int i = 0; i < isWhite.length; i++){
            /* parameters for white button */
            Dimension size = new Dimension(35, 150);
            Point point = new Point(whiteX, y);
            Integer layer = JLayeredPane.DEFAULT_LAYER;
            PianoKey.KeyColor keyColor = PianoKey.KeyColor.WHITE;
            boolean addRigidArea = false;

            if(isWhite[i]){
                /* if current button is white update white coordinates without changing parameters*/
                if (i != isWhite.length - 1) {
                    addRigidArea = true;
                }
                whiteX += whiteDistance;
            }
            else{
                /* if current button is black change rendering parameters */
                keyColor = PianoKey.KeyColor.BLACK;
                size = new Dimension(25, 85);
                point = new Point(blackX, y);
                layer = JLayeredPane.PALETTE_LAYER;

                /* update black coordinates */
                if (++blackCount != 2){
                    blackX += blackStandardDistance;
                }
                else{
                    // if the current black key is the second being rendered jump to the third group of black keys
                    blackX += blackModifiedDistance;
                }
            }
            /* create new piano key */
            keys[i] = new PianoKey(i, keyColor);
            keys[i].setLocation(point);
            keys[i].setSize(size);
            keys[i].addMouseListener(new PianoKeyAdapter());
            keyBoard.add(keys[i], layer);

            if(addRigidArea){
                keyBoard.add(Box.createRigidArea(new Dimension(2, 0)));
            }

        }
        keyBoard.add(Box.createRigidArea(new Dimension(55, 0)));
        return keyBoard;
    }
    private JPanel createNotes(){
        //Creating the note panel that needs to be next to the last white key
        JPanel notesPanel = new JPanel();
        notesPanel.setLayout(new BoxLayout(notesPanel,BoxLayout.X_AXIS));
        notesPanel.setForeground(Color.WHITE);
        notesPanel.setBackground(Color.BLACK);
        notesPanel.add(Box.createRigidArea(new Dimension(50,0)));

        //Creating note label
        JLabel notesLabel = new JLabel("Notes:");
        notesLabel.setForeground(Color.WHITE);
        notesLabel.setBackground(Color.BLACK);
        notesLabel.setHorizontalAlignment(JLabel.LEFT);
        notesPanel.add(notesLabel);
        notesPanel.add(Box.createRigidArea(new Dimension(10, 0)));

        //create entry box
        JTextArea entryBox = new JTextArea();
        entryBox.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        entryBox.setFont(new Font("Ariel", Font.BOLD, 14));
        entryBox.setForeground(Color.BLACK);
        entryBox.setBackground(new Color (170,180,254));

        //adding to the JPanel
        notesPanel.add(entryBox);
        notesPanel.add(Box.createRigidArea(new Dimension(50, 0)));

        return notesPanel;
    }

    private JButton createSwitch(char c){
        JButton switchOctave = new JButton();
        if (c == 'r') {
            try {
                Image img = ImageIO.read(getClass().getResource("/imstrument/globals/imstrument_rightarrow.jpg"));
                img.getScaledInstance(35,10, java.awt.Image.SCALE_SMOOTH);
                switchOctave.setIcon(new ImageIcon(img));
            } catch (IOException e) {
                e.printStackTrace(); //TODO aggiungi alternative
            }
        }
        else {
            try {
                Image img = ImageIO.read(getClass().getResource("/imstrument/globals/imstrument_leftarrow.jpg"));
                img.getScaledInstance(35,10, java.awt.Image.SCALE_SMOOTH);
                switchOctave.setIcon(new ImageIcon(img));
            } catch (IOException e) {
                e.printStackTrace(); //TODO aggiungi alternative
            }
        }
        switchOctave.setForeground(Color.WHITE);
        switchOctave.setBackground(Color.BLACK);
        switchOctave.addActionListener(this);

        return switchOctave;
    }

    public void setPressed(int index, boolean value){
        if(index < keys.length){
            keys[index].setPressed(value);
        }
        System.out.println("Implement second row");
    }

    @Override
    public void actionPerformed(ActionEvent e) {

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
