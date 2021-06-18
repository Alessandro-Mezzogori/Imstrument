package imstrument.algorithm;

import imstrument.globals.GlobalSetting;
import imstrument.start.StartApp;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Objects;

public class ControlWindow extends JFrame {
    final AlgorithmDisplay algorithmDisplay;

    final ArrayList<String> algorithmNames;
    final JList<String> algorithmList;

    public ControlWindow(){
        setLayout(new BorderLayout());
        algorithmDisplay = new AlgorithmDisplay(StartApp.algorithm.getGroups());

        /* left sided of the control panel */
        algorithmNames = new ArrayList<>();
        populateAlgorithmList(Algorithm.CUSTOM_ALGORITHM_FOLDER);

        ListModel<String> operatorsListModel = new AbstractListModel<>() {
            @Override
            public int getSize() { return algorithmNames.size(); }
            @Override
            public String getElementAt(int index) { return algorithmNames.get(index); }
        };
        algorithmList = new JList<>(operatorsListModel);

        JButton createAlgorithmButton = new JButton("Create");
        createAlgorithmButton.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> { new CustomAlgorithmCreator(true); });
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
        displayPanel.setLayout(new BoxLayout(displayPanel, BoxLayout.LINE_AXIS));
        displayPanel.add(algorithmDisplay);
        displayPanel.add(Box.createRigidArea(new Dimension(50, 150)));

        descriptionPanel.add(displayPanel, BorderLayout.CENTER);
        descriptionPanel.add(new JLabel("Description"), BorderLayout.NORTH);

        JPanel buttonContainer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton selectAlgorithmButton = new JButton("Select");
        selectAlgorithmButton.addActionListener(e ->{
            //TODO aggiungi caricamento dell'algoritmo
            File selectedFile = new File(Algorithm.CUSTOM_ALGORITHM_FOLDER.toString() + "/" + algorithmList.getSelectedValue() + "." + Algorithm.fileExtension);
            try {
                StartApp.algorithm.decode(Files.readString(selectedFile.toPath()));
            } catch (IOException ioException) {
                ioException.printStackTrace(); //TODO se non riesce a leggere il file
            }
            algorithmDisplay.repaint();
        });

        buttonContainer.add(selectAlgorithmButton);
        descriptionPanel.add(buttonContainer, BorderLayout.SOUTH);
        add(descriptionPanel, BorderLayout.CENTER);

        setMinimumSize(GlobalSetting.MINIMUM_WINDOW_SIZE);
        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true); // shows jframe
        requestFocus(); // requestes focus for event dispatching
    }

    public void populateAlgorithmList(final File folder) {
        /* standard algorithms */
        for(final File fileEntry: Objects.requireNonNull())


        /* custom algorithms */
        for (final File fileEntry : Objects.requireNonNull(folder.listFiles())) {
            String name = fileEntry.getName();
            if (fileEntry.isDirectory()) {
                populateAlgorithmList(fileEntry);
            } else if (getExtension(name).equals(Algorithm.fileExtension)){
                algorithmNames.add(name.substring(0, name.length() - Algorithm.fileExtension.length() - 1));
            }
        }
    }

    public String getExtension(String filename) {
        int index = filename.lastIndexOf('.');
        return (index == -1) ? "" : filename.substring(index + 1);
    }
}
