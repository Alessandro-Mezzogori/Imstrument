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

        for (int i =0; i<2; i++) {
            //creating the notes panel
            JPanel notesPanel = createNotes();
            /* adding to the main panel*/
            mainPanel.add(notesPanel);
            mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

            /* Creating the button for switching octaves*/
            JButton switchRight = createSwitch('r');
            JButton switchLeft  = createSwitch('l');

            /* Creating the pianokeyboard */
            JLayeredPane pianoKeyboard = createKeyboard();

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
    private JLayeredPane createKeyboard() {

        // Init
        int x = 55;
        int y = 0;
        int whiteDistance = 37;

        // Create the layerPane
        JLayeredPane keyBoard = new JLayeredPane();
        keyBoard.setPreferredSize(new Dimension(350, 160));
        keyBoard.add(Box.createRigidArea(new Dimension(x, 0)));

        // Adding all the white buttons in the keyboard
        for (int i = 0; i < whiteKeyNumber; i++) {
            keys[i] = new PianoKey(i, PianoKey.KeyColor.WHITE);
            keys[i].addMouseListener(new PianoKeyAdapter());
            keys[i].setBounds(x, y, 35, 150);
            keyBoard.add(keys[i], JLayeredPane.DEFAULT_LAYER);
            if (i != whiteKeyNumber -1) {
                keyBoard.add(Box.createRigidArea(new Dimension(2, 0)));
            }
            x += whiteDistance;
        }

        keyBoard.add(Box.createRigidArea(new Dimension(x, 0)));

        // Creating and adding black keys
            x = 77;
        int blackStandardDistance = 38;
        int blackModifiedDistance = 73;
        for (int i= whiteKeyNumber; i < keyNumber; ++i) {
            keys[i] = new PianoKey(i, PianoKey.KeyColor.BLACK);
            keys[i].addMouseListener(new PianoKeyAdapter());
            keys[i].setBounds(x,y,25,85);
            if (i != whiteKeyNumber+1){
                x += blackStandardDistance;
            }
            else{
                x += blackModifiedDistance;
            }
            keyBoard.add(keys[i], JLayeredPane.PALETTE_LAYER);
        }

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
                e.printStackTrace();
            }
        }
        else {
            try {
                Image img = ImageIO.read(getClass().getResource("/imstrument/globals/imstrument_leftarrow.jpg"));
                img.getScaledInstance(35,10, java.awt.Image.SCALE_SMOOTH);
                switchOctave.setIcon(new ImageIcon(img));
            } catch (IOException e) {
                e.printStackTrace();
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
