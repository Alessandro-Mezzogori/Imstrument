package imstrument.algorithm;

import imstrument.globals.GlobalSetting;
import org.lwjgl.system.CallbackI;

import javax.swing.*;
import java.awt.*;

public class ControlWindow extends JFrame {
    JPanel cards;
    JButton createButton;
    JButton selectButton;

    public ControlWindow(){
        cards = new JPanel(new CardLayout());

        createButton = new JButton("Create");
        selectButton = new JButton("Select");
        selectButton.setPreferredSize(createButton.getPreferredSize());
        selectButton.setMinimumSize(createButton.getMinimumSize());

        JPanel buttonContainer = new JPanel();
        buttonContainer.setLayout(new BoxLayout(buttonContainer, BoxLayout.PAGE_AXIS));
        buttonContainer.add(selectButton);
        buttonContainer.add(Box.createRigidArea(new Dimension(1, 40)));
        buttonContainer.add(createButton);

        buttonContainer.setAlignmentY(Component.BOTTOM_ALIGNMENT);

        setLayout(new BorderLayout());
        add(buttonContainer, BorderLayout.LINE_START);
        add(Box.createRigidArea(new Dimension(300, 300)), BorderLayout.CENTER);

        setMinimumSize(GlobalSetting.MINIMUM_WINDOW_SIZE);
        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true); // shows jframe
        requestFocus(); // requestes focus for event dispatching
    }
}
