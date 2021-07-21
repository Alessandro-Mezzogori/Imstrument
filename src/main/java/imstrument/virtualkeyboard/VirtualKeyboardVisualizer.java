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

        setKeyboardBindings();

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

    /**
     * function that setups all the keyboard bindings to play the top and bottom virtual keyboards
     */
    private void setKeyboardBindings(){
        InputMap inputMap = this.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        // TOP ROW PRESSED
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

        // BOTTOM ROW PRESSED
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

        // TOP ROW RELEASE
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

        // BOTTOM ROW RELEASE
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

        // BUTTON PRESSED ACTION
        Action onPressed = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // get the index of the associated soundwave
                int index = convertBindingToIndex(e.getActionCommand());

                if(index != -1){
                    if(StartApp.waveManager != null) {
                        // start playing the associated soundwave
                        StartApp.waveManager.triggerWaveGeneration(index);
                    }

                    VirtualKeyboardVisualizer.this.setPressed(index , true);
                }
            }
        };

        // BUTTON RELEASE ACTION
        Action onRelease = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // get the index of the associated soundwave
                int index = convertBindingToIndex(e.getActionCommand());

                if(index != -1){
                    if(StartApp.waveManager != null) {
                        // stop the soundwave generation
                        StartApp.waveManager.setShouldGenerate(false, index);
                    }

                    VirtualKeyboardVisualizer.this.setPressed(index , false);
                }
            }
        };

        ActionMap actionMap = this.getRootPane().getActionMap();
        /* link all the possibile bindings the the action */
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

    /**
     * Retrieves the associated soundwave index from a keyboard action command
     * @param actionCommand keyboard action command es. 'w' '3' 'u'...
     * @return the associated soundwave index
     */
    private int convertBindingToIndex(String actionCommand) {
        return switch (actionCommand) {
            case "w" -> 1;
            case "3" -> 2;
            case "e" -> 3;
            case "4" -> 4;
            case "r" -> 5;
            case "t" -> 6;
            case "6" -> 7;
            case "y" -> 8;
            case "7" -> 9;
            case "u" -> 10;
            case "8" -> 11;
            case "i" -> 12;
            case "z" -> 13;
            case "s" -> 14;
            case "x" -> 15;
            case "d" -> 16;
            case "c" -> 17;
            case "v" -> 18;
            case "g" -> 19;
            case "b" -> 20;
            case "h" -> 21;
            case "n" -> 22;
            case "j" -> 23;
            case "m" -> 24;
            default -> -1;
        };

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
