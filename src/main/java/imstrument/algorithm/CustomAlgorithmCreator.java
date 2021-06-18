package imstrument.algorithm;

import imstrument.globals.GlobalSetting;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class CustomAlgorithmCreator extends JFrame {
    AlgorithmCanvas drawingAlgorithmCanvas;
    UnitWindow unitWindow;
    ArrayList<AlgorithmUnit> groups;
    final boolean fromAlgorithmSelect;

    public CustomAlgorithmCreator(boolean fromAlgorithmSelect){
        /* params */
        final int algorithmNameFieldWidth = 20;
        this.fromAlgorithmSelect = fromAlgorithmSelect;
        /* create custom algorithm canvas */
        groups = new ArrayList<>();
        drawingAlgorithmCanvas = new AlgorithmCanvas(
                groups,
                () -> {
                    unitWindow.updateTable();
                }
        );
        unitWindow = new UnitWindow(
                groups,
                () -> {
                    drawingAlgorithmCanvas.repaint();
                }
        );

        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.LINE_AXIS));
        container.add(drawingAlgorithmCanvas);
        container.add(unitWindow);


        final JTextField algorithmNameField = new JTextField(algorithmNameFieldWidth);
        final JLabel algorithmNameFieldLabel = new JLabel("New Algorithm Name: ");
        algorithmNameFieldLabel.setLabelFor(algorithmNameField);
        /* create custom algorithm buttons */
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e ->{
            //TODO check that there's no other algorithm with the same name
            //TODO check that all groups have an operator
            File newFile = new File(Algorithm.CUSTOM_ALGORITHM_FOLDER.toString() + "/" + algorithmNameField.getText() + "." + Algorithm.fileExtension) ;
            try {
                boolean isCreated = newFile.createNewFile(); // TODO notify user if it doesn't go well
                StringBuilder stringBuilder = new StringBuilder();
                for(AlgorithmUnit unit : groups){
                    stringBuilder.append(unit.toString());
                }

                BufferedWriter writer = new BufferedWriter(new FileWriter(newFile));
                writer.write(stringBuilder.toString());
                writer.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            clear();
        });

        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> {
            clear();
        });

        JPanel buttonContainer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonContainer.add(algorithmNameFieldLabel);
        buttonContainer.add(algorithmNameField);
        buttonContainer.add(clearButton);
        buttonContainer.add(saveButton);

        setLayout(new BorderLayout());
        add(buttonContainer, BorderLayout.SOUTH);
        add(container, BorderLayout.CENTER);
        add(Box.createRigidArea(new Dimension(20, 20)), BorderLayout.LINE_START);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if(fromAlgorithmSelect){
                    SwingUtilities.invokeLater(ControlWindow::new);
                }
                super.windowClosing(e);
            }
        });

        setMinimumSize(GlobalSetting.MINIMUM_WINDOW_SIZE);
        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true); // shows jframe
        requestFocus(); // requestes focus for event dispatching
    }

    private void clear(){
        groups.clear();
        drawingAlgorithmCanvas.repaint();
        unitWindow.updateTable();
    }

    public interface AlgorithmCreationSynchronizer {
        void update();
    }
}
