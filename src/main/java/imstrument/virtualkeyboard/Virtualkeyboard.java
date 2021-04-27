package imstrument.virtualkeyboard;

import javax.swing.*;
import java.awt.*;

public class Virtualkeyboard extends JFrame {
    public Virtualkeyboard() {
        //Create the GUI
        setTitle("Imstrument Piano Keyboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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

        // Create the layerPane
        JLayeredPane keyBoard = new JLayeredPane();
        keyBoard.setPreferredSize(new Dimension(900, 162));
        keyBoard.add(Box.createRigidArea(new Dimension(x, 0)));

        // Adding all the white buttons in the keyboard
        for (int j = 0; j < 11; j++) {
            JButton whiteButton = new WhiteButton();
            whiteButton.setBounds(x, y, 35, 162);
            keyBoard.add(whiteButton, 1);
            if (j != 10) {
                keyBoard.add(Box.createRigidArea(new Dimension(2, 0)));
            }
            x += 37;
        }

        // Creating and adding black keys


        JButton blackButton1 = new BlackButton();
        JButton blackButton2 = new BlackButton();
        JButton blackButton3 = new BlackButton();
        JButton blackButton4 = new BlackButton();
        JButton blackButton5 = new BlackButton();
        JButton blackButton6 = new BlackButton();
        JButton blackButton7 = new BlackButton();

        blackButton1.setBounds(77, y, 25, 95);
        keyBoard.add(blackButton1, 2);

        blackButton2.setBounds(115, y, 25, 95);
        keyBoard.add(blackButton2, 2);

        blackButton3.setBounds(188, y, 25, 95);
        keyBoard.add(blackButton3, 2);

        blackButton4.setBounds(226, y, 25, 95);
        keyBoard.add(blackButton4, 2);

        blackButton5.setBounds(264, y, 25, 95);
        keyBoard.add(blackButton5, 2);

        blackButton6.setBounds(337, y, 25, 95);
        keyBoard.add(blackButton6, 2);

        blackButton7.setBounds(375, y, 25, 95);
        keyBoard.add(blackButton7, 2);

        return keyBoard;
    }
}
