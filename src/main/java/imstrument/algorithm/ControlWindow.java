package imstrument.algorithm;

import imstrument.globals.GlobalSetting;
import imstrument.start.StartApp;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Jframe used to select an already created algorithm and to access the algorithm creation window
 */
public class ControlWindow extends JFrame {
    /**
     * component displaying the current selected algorithm
     */
    final AlgorithmDisplay algorithmDisplay;

    /**
     * list of all the created algorithms inside the storing folder
     */
    final ArrayList<String> algorithmNames;
    /**
     * component showing list of names
     */
    final JList<String> algorithmList;

    public ControlWindow(){
        setLayout(new BorderLayout());
        algorithmDisplay = new AlgorithmDisplay(StartApp.algorithm.getUnits());

        /* left sided of the control panel */
        algorithmNames = new ArrayList<>();
        // retrieve all the algorithm names from the storage folder
        populateAlgorithmList(Algorithm.ALGORITHM_FOLDER);

        // create the visuals for the name list
        DefaultListModel<String> algorithmListModel = new DefaultListModel<>();
        algorithmListModel.addAll(0, algorithmNames);
        algorithmList = new JList<>(algorithmListModel);

        // button used to access the algorithm creation menu
        JButton createAlgorithmButton = new JButton("Create");
        createAlgorithmButton.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> new CustomAlgorithmCreator(true));
            dispose(); // dispose of the current jframe so it doesn't clutter the view
        });
        createAlgorithmButton.setVerticalAlignment(JButton.BOTTOM);

        // add everything to a panel that is placed on the left side of the jframe
        JPanel algorithmListPanel = new JPanel(new BorderLayout());
        algorithmListPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 50));
        algorithmListPanel.add(new JScrollPane(algorithmList), BorderLayout.CENTER);
        algorithmListPanel.add(createAlgorithmButton, BorderLayout.SOUTH);
        add(algorithmListPanel, BorderLayout.LINE_START);

        /* right side of the control panel */
        JPanel descriptionPanel = new JPanel(new BorderLayout());

        JPanel displayPanel = new JPanel();
        displayPanel.setLayout(new GridBagLayout()); // grid bag layout for component alignment and preventing resizing
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        JLabel displayPanelTitle  = new JLabel("Algorithm Description");
        displayPanelTitle.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        displayPanel.add(displayPanelTitle, constraints);
        constraints.gridy = 1;
        displayPanel.add(algorithmDisplay, constraints);
        constraints.gridx = 1;
        displayPanel.add(new OperatorColorLegend(), constraints);

        descriptionPanel.add(displayPanel, BorderLayout.CENTER);

        // description panel buttons
        // select button
        JPanel buttonContainer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton selectAlgorithmButton = new JButton("Select");
        selectAlgorithmButton.addActionListener(e ->{
            boolean loaded = false;
            String selectedAlgorithm = algorithmList.getSelectedValue();
            File selectedFile = new File(Algorithm.ALGORITHM_FOLDER.toString() + "/" + selectedAlgorithm + "." + Algorithm.fileExtension);
            try {
                StartApp.algorithm.decode(selectedAlgorithm, Files.readString(selectedFile.toPath()));
                loaded = true;
            } catch (NoSuchFileException noSuchFileExceptionException) {
                /* if there's no file do nothing */
            } catch (IOException ioException){
                /* if there's an exception set loaded will be set to false so do nothing*/
            }

            if(loaded) algorithmDisplay.repaint();
        });

        // delete button
        JButton deleteAlgorithmButton = new JButton("Delete");
        deleteAlgorithmButton.addActionListener(e ->{
            String selectedAlgorithm = algorithmList.getSelectedValue();
            File selectedFile = new File(Algorithm.ALGORITHM_FOLDER.toString() + "/" + selectedAlgorithm + "." + Algorithm.fileExtension);
            boolean deletedResult = selectedFile.delete();

            if(deletedResult){
                /* update gui */
                ((DefaultListModel<String>) algorithmList.getModel()).remove(algorithmList.getSelectedIndex());
            }
        });

        buttonContainer.add(selectAlgorithmButton);
        buttonContainer.add(deleteAlgorithmButton);
        descriptionPanel.add(buttonContainer, BorderLayout.SOUTH);
        add(descriptionPanel, BorderLayout.CENTER);

        setMinimumSize(GlobalSetting.MINIMUM_WINDOW_SIZE);
        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true); // shows jframe
        setResizable(false);
        setTitle("Algorithm Selector");
        requestFocus(); // requestes focus for event dispatching
    }

    /**
     * Populates the algorithmName array list of string with the name of files
     * with the extension Algorithm.fileExtensdion given a folder location
     * @param folder folder location in which to look for the files
     */
    private void populateAlgorithmList(final File folder) {
        for (final File fileEntry : Objects.requireNonNull(folder.listFiles())) {
            String name = fileEntry.getName();
            if (fileEntry.isDirectory()) {
                populateAlgorithmList(fileEntry);
            } else if (getExtension(name).equals(Algorithm.fileExtension)){
                algorithmNames.add(name.substring(0, name.length() - Algorithm.fileExtension.length() - 1));
            }
        }
    }

    /**
     * Gets the extension of the filename
     * @param filename from which to extract the extension
     * @return the extension of the given filename string
     */
    private String getExtension(String filename) {
        int index = filename.lastIndexOf('.');
        return (index == -1) ? "" : filename.substring(index + 1);
    }
}
