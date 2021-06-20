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

public class ControlWindow extends JFrame {
    final AlgorithmDisplay algorithmDisplay;

    final ArrayList<String> algorithmNames;
    final JList<String> algorithmList;

    public ControlWindow(){
        setLayout(new BorderLayout());
        algorithmDisplay = new AlgorithmDisplay(StartApp.algorithm.getUnits());

        /* left sided of the control panel */
        algorithmNames = new ArrayList<>();
        populateAlgorithmList(Algorithm.ALGORITHM_FOLDER);

        DefaultListModel<String> algorithmListModel = new DefaultListModel<>();
        algorithmListModel.addAll(0, algorithmNames);
        algorithmList = new JList<>(algorithmListModel);

        JButton createAlgorithmButton = new JButton("Create");
        createAlgorithmButton.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> new CustomAlgorithmCreator(true));
            dispose();
        });
        createAlgorithmButton.setVerticalAlignment(JButton.BOTTOM);

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
                /* TODO catch*/
            }

            if(loaded) algorithmDisplay.repaint();
        });
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

    private void selectCurrentAlgorithm(){
        String currentAlgorithm = StartApp.algorithm.getCurrentName();
        if(currentAlgorithm.equals("")) return;

        algorithmList.setSelectedValue(currentAlgorithm, false);
        algorithmDisplay.repaint();
    }

    private String getExtension(String filename) {
        int index = filename.lastIndexOf('.');
        return (index == -1) ? "" : filename.substring(index + 1);
    }
}
