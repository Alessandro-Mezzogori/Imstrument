package imstrument.sound.wavetables;

import imstrument.algorithm.Algorithm;
import imstrument.algorithm.AlgorithmDisplay;
import imstrument.globals.GlobalSetting;
import imstrument.sound.waves.Soundwave;
import imstrument.sound.waves.WaveManager;
import imstrument.start.StartApp;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.Objects;

public class WavetableSelector extends JFrame {
    /**
     * list of all the created algorithms inside the storing folder
     */
    final ArrayList<String> wavetableNames;
    /**
     * component showing list of names
     */
    final JList<String> wavetableList;

    /**
     * currently selected wavetable
     */
    public static String activeWavetableName = Wavetable.DEFAULT_WAVETABLE;

    public WavetableSelector(){
        setLayout(new BorderLayout());

        /* left sided of the control panel */
        wavetableNames = new ArrayList<>();
        // retrieve all the algorithm names from the storage folder
        populateAlgorithmList(Wavetable.WAVETABLE_FOLDER);

        // create the visuals for the name list
        DefaultListModel<String> wavetableListModel = new DefaultListModel<>();
        wavetableListModel.addAll(0, wavetableNames);
        wavetableList = new JList<>(wavetableListModel);
        // add the list to the center
        add(wavetableList, BorderLayout.CENTER);

        // set a

        // create the selection button
        JButton select = new JButton("Select");
        select.addActionListener(a -> {
            // save the current wavetable name
            activeWavetableName = wavetableList.getSelectedValue();

            // change the wavetable of the mouse soundwave to the selected one
            StartApp.waveManager.soundwaves.get(WaveManager.MOUSE_SOUNDWAVE_INDEX).setWavetable(new Wavetable(activeWavetableName));
        });

        add(select, BorderLayout.SOUTH);
        setMinimumSize(new Dimension(200, 300));
        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true); // shows jframe
        requestFocus(); // requestes focus for event dispatching
        setTitle("Wavetable Selector");
    }

    /**
     * Populates the wavetable name array list of string with the name of files
     * with the extension Wavetable.fileExtensdion given a folder location
     * @param folder folder location in which to look for the files
     */
    private void populateAlgorithmList(final File folder) {
        for (final File fileEntry : Objects.requireNonNull(folder.listFiles())) {
            String name = fileEntry.getName();
            if (fileEntry.isDirectory()) {
                populateAlgorithmList(fileEntry);
            } else if (getExtension(name).equals(Wavetable.fileExtension)){
                wavetableNames.add(name.substring(0, name.length() - Wavetable.fileExtension.length() - 1));
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
