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

/**
 * Window dedicated to the creation of new Algorithms
 */
public class CustomAlgorithmCreator extends JFrame {
    /**
     * used to draw the rectangles of each unit
     */
    AlgorithmCanvas drawingAlgorithmCanvas;

    /**
     * Shows the corrisponding table to the created algorithm units
     */
    UnitWindow unitWindow;

    /**
     * Current Algorithm units
     */
    ArrayList<AlgorithmUnit> groups;

    /**
     * Tells if the Jframe was opene from the Controls Jframe
     * if it's true when this frame will be close it will automatically open the controls JFrame
     */
    final boolean fromAlgorithmSelect;

    /**
     * Input JTextField to save the created algorithm with a name
     */
    final JTextField algorithmNameField;

    public CustomAlgorithmCreator(boolean fromAlgorithmSelect){
        /* params */
        // set the algorithm name input field width
        final int algorithmNameFieldWidth = 20;

        // save if it came from the algorithm select window
        this.fromAlgorithmSelect = fromAlgorithmSelect;

        /* initialize the array list for the algo units */
        groups = new ArrayList<>();
        // create the canvas to draw the rectangles for the units
        // and give it an update function to call
        drawingAlgorithmCanvas = new AlgorithmCanvas(groups, () -> unitWindow.updateTable());
        // create the algorithm units window and five it an update function to call
        unitWindow = new UnitWindow(groups, () -> drawingAlgorithmCanvas.repaint());

        // component placement and jframe layout
        JPanel container = new JPanel();
        container.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;

        container.add(drawingAlgorithmCanvas, constraints);
        constraints.gridx = 1;
        container.add(unitWindow, constraints);

        // algorithm name field to save the created algorithm
        algorithmNameField = new JTextField(algorithmNameFieldWidth);
        // algorithm name field label so that it tells what is it for
        final JLabel algorithmNameFieldLabel = new JLabel("New Algorithm Name: ");
        algorithmNameFieldLabel.setLabelFor(algorithmNameField);

        // create the save button
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> saveAlgorithm(algorithmNameField.getText()));

        // go back to a blank slate button
        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> clear());

        // visual formatting
        JPanel buttonContainer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonContainer.add(algorithmNameFieldLabel);
        buttonContainer.add(algorithmNameField);
        buttonContainer.add(clearButton);
        buttonContainer.add(saveButton);

        setLayout(new BorderLayout());
        add(buttonContainer, BorderLayout.SOUTH);
        add(container, BorderLayout.CENTER);
        add(Box.createRigidArea(new Dimension(20, 20)), BorderLayout.LINE_START);

        // if the window is closing and it came from the algorithm select -> open a control window
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if(fromAlgorithmSelect){
                    SwingUtilities.invokeLater(ControlWindow::new);
                }
                super.windowClosing(e);
            }
        });

        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true); // shows jframe
        requestFocus(); // requestes focus for event dispatching
        setResizable(false);
        setTitle("Algorithm Creator");
    }

    /**
     * Saves the current algorithm to a file and resest
     * the algorithm creation environment
     * @param name the name of the file
     */
    private void saveAlgorithm(String name){
        /* check that all groups have an operator */
        for(AlgorithmUnit unit : groups){
            if(unit.operator == null){
                JOptionPane.showMessageDialog(null, "Please give all the units an operator");
                return;
            }
        }

        File newFile = new File(Algorithm.ALGORITHM_FOLDER.toString() + "/" + name + "." + Algorithm.fileExtension) ;
        // create / open the file and write the algorithm to it
        try {
            // create file if it exists then ask to overwrite it
            if(!newFile.createNewFile()){
                if( JOptionPane.showConfirmDialog(null, name + " already present, overwrite it ?") != JOptionPane.OK_OPTION ) {
                    return;
                }
            }

            // create a string builder object to concatenate all the unit string representation
            StringBuilder stringBuilder = new StringBuilder();
            for(AlgorithmUnit unit : groups){
                stringBuilder.append(unit.toString());
            }

            // create buffer write from the opened filed
            BufferedWriter writer = new BufferedWriter(new FileWriter(newFile, false));
            // write the algorithm string representation
            writer.write(stringBuilder.toString());
            // close and save file
            writer.close();

            // reset the algorithm creation environment only if there wasn't any exception
            clear();
        } catch (IOException ioException) {
            JOptionPane.showMessageDialog(null, "An error has occurred during the saving of the algorithm, retry");
        }

    }

    /**
     * Resets the creation environment
     */
    private void clear(){
        groups.clear();
        drawingAlgorithmCanvas.repaint();
        unitWindow.updateTable();
    }

    /**
     * Dummy interface to pass an update function used only for synchronizastion
     * purposes between the unit window and the algorithm canvas,
     * the update functions should tell the other that the caller was updated
     */
    public interface AlgorithmCreationSynchronizer {
        void update();
    }
}
