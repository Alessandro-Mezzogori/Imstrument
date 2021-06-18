package imstrument.algorithm;

import org.lwjgl.system.CallbackI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class CustomAlgorithmCreator extends JPanel {
    AlgorithmCanvas drawingAlgorithmCanvas;
    UnitWindow unitWindow;
    ArrayList<AlgorithmUnit> groups;


    public CustomAlgorithmCreator(){
        /* params */
        final int algorithmNameFieldWidth = 20;

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
            File newFile = new File(Algorithm.algorithmFolder.toString() + "/" + algorithmNameField.getText());
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
//        add(drawingAlgorithmCanvas, BorderLayout.CENTER);
        add(container, BorderLayout.CENTER);
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
