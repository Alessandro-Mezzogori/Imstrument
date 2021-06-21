package imstrument.virtualkeyboard;

import imstrument.sound.waves.WaveManager;
import imstrument.start.StartApp;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.*;


public class VirtualKeyboardVisualizer extends JFrame {
    /**
     * array containing all the keys rendered in a virtual keyboard
     */
    private final PianoKey[] keys;

    /**
     *  flags telling if the current key is a white key or a black one
     *  must be the same lenght as WaveManager.OCTAVE_KEY_COUNT
     */
    private final boolean[] isWhite = new boolean[]{true, false, true, false, true, true, false, true, false, true, false, true};
    private final JLabel[] entryBox;

    private final String[] noteNameLookup = new String[]{"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};

    public VirtualKeyboardVisualizer() {
        //Create the GUI
        setTitle("Imstrument Piano Keyboard");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        /* Init Array for the PianoKeys */
        keys= new PianoKey[WaveManager.OCTAVE_KEY_COUNT * 2];

        /* Create the main panel for the keyboard */
        Container mainPanel = getContentPane();
        mainPanel.setLayout((new BoxLayout(mainPanel, BoxLayout.Y_AXIS)));
        mainPanel.setForeground(Color.WHITE);
        mainPanel.setBackground(Color.GRAY);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        final int numberOfKeyboards = 2;
        entryBox = new JLabel[numberOfKeyboards];
        for (int keyboardIndex = 0; keyboardIndex < numberOfKeyboards; keyboardIndex++) {
            //creating the notes panel
            JPanel notesPanel = createNotes(keyboardIndex);

            /* adding to the main panel*/
            mainPanel.add(notesPanel);
            mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

            /* Creating the button for switching octaves*/
            JButton switchRight = new SwitchButton(true, keyboardIndex);
            JButton switchLeft  = new SwitchButton(false, keyboardIndex);

            /* Creating the pianokeyboard */
            JLayeredPane pianoKeyboard = createKeyboard(keyboardIndex);

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
            // i + WaveManager.OCTAVE_KEY_COUNT * keyboardIndex + 1 -> links to the corrisponding wavesummer in the wave manager
            int currentKeyIndex = i + WaveManager.OCTAVE_KEY_COUNT * keyboardIndex;
            keys[currentKeyIndex] = new PianoKey(currentKeyIndex + 1, keyColor);
            keys[currentKeyIndex].setLocation(point);
            keys[currentKeyIndex].setSize(size);
            keys[currentKeyIndex].addMouseListener(new PianoKeyAdapter());
            keyBoard.add(keys[currentKeyIndex], layer);

            if(addRigidArea){
                keyBoard.add(Box.createRigidArea(new Dimension(2, 0)));
            }

        }
        keyBoard.add(Box.createRigidArea(new Dimension(55, 0)));
        return keyBoard;
    }
    private JPanel createNotes(int keyboardIndex){
        //Creating the note panel that needs to be next to the last white key
        JPanel notesPanel = new JPanel();
        notesPanel.setLayout(new BoxLayout(notesPanel,BoxLayout.X_AXIS));
        notesPanel.setForeground(Color.WHITE);
        notesPanel.setBackground(Color.BLACK);
        notesPanel.add(Box.createRigidArea(new Dimension(50,0)));

        //Creating note label
        JLabel notesLabel = new JLabel("Note:");
        notesLabel.setForeground(Color.WHITE);
        notesLabel.setBackground(Color.BLACK);
        notesLabel.setHorizontalAlignment(JLabel.LEFT);
        notesPanel.add(notesLabel);
        notesPanel.add(Box.createRigidArea(new Dimension(10, 0)));

        //create entry box
        entryBox[keyboardIndex] = new JLabel("Le note compariranno qui");
        entryBox[keyboardIndex].setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        entryBox[keyboardIndex].setFont(new Font("Ariel", Font.BOLD, 14));
        entryBox[keyboardIndex].setForeground(Color.WHITE);
        entryBox[keyboardIndex].setBackground(new Color (170,180,254));


        //adding to the JPanel
        notesPanel.add(entryBox[keyboardIndex]);
        notesPanel.add(Box.createRigidArea(new Dimension(50, 0)));

        return notesPanel;
    }


    public void setPressed(int index, boolean value){
        index -= 1;
        if(index < keys.length){
            keys[index].setPressed(value);
            setEntryText(index);
        }
    }

    public void setEntryText(int index){
        entryBox[index/isWhite.length].setText(noteNameLookup[index%isWhite.length]);
    }

    private class PianoKeyAdapter extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            PianoKey pianoKey = (PianoKey) e.getSource();
            setPressed(pianoKey.getId(), true);
            StartApp.waveManager.triggerWaveGeneration(pianoKey.getId());
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            PianoKey pianoKey = (PianoKey) e.getSource();
            setPressed(pianoKey.getId(),false);
            StartApp.waveManager.setShouldGenerate(false, pianoKey.getId());
        }
    }


}
