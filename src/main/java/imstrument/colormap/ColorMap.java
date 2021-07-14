package imstrument.colormap;

import imstrument.algorithm.Algorithm;
import imstrument.algorithm.AlgorithmUnit;
import imstrument.globals.GlobalSetting;
import imstrument.sound.utils.ImagePanel;
import imstrument.start.StartApp;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * JFrame to manage everything that concerns the ColorMap from the image
 */
public class ColorMap extends JFrame {

    // panel used to show the computed image
    private ImagePanel imagePanel;
    // computed image
    private BufferedImage colormap;
    // progress bar for the generation of the colormap
    private JProgressBar progressBar;

    /* saving the colormap */
    // color map name input field
    JTextField colorMapName;
    // save the colormap with the given name
    JButton saveButton;

    /**
     * Default save folder for the generated colormaps
     */
    public static File DEFAULT_FOLDER = new File(StartApp.defaultFolder + "/colormaps/");

    static{
        // create the default folder if it doesn't exists
        if(!DEFAULT_FOLDER.exists()){
            boolean mkdir = DEFAULT_FOLDER.mkdir();
        }
    }

    public ColorMap(BufferedImage source){
        // if there's no loaded image notify user and stop execution
        if(source == null){
            JOptionPane.showMessageDialog(null, "Cannot generate the colormap: no image was loaded");
            return;
        }

        // if no algorithm was selected notify user and stop execution
        if(StartApp.algorithm.getCurrentName().equals(Algorithm.EMPTY_ALGORITHM_NAME)){
            JOptionPane.showMessageDialog(null, "Cannot generate the colormap: no algorithm was selected");
            return;
        }

        // create the container for the generated image
        imagePanel = new ImagePanel();
        // create the progress bar showing the progress of the computation
        progressBar = new JProgressBar(JProgressBar.HORIZONTAL, 0, 100);
        progressBar.setStringPainted(true);
        progressBar.setValue(0);

        // create the background task handleing the generation of the image
        SwingWorker<Void, Void> task = new ColorMapComputeTask(source);
        // when the progress of the task is changed update the progress of the progress bar
        task.addPropertyChangeListener(evt -> {
            if ("progress".equals(evt.getPropertyName())) {
                int progress = (Integer) evt.getNewValue();
                progressBar.setValue(progress);
            }
        });
        // start task execution in background
        task.execute();

        // put the progress bar at the center of the border layout
        setLayout(new BorderLayout());
        add(progressBar, BorderLayout.CENTER);

        // create the button panel to handle the saving of the generated image
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        colorMapName = new JTextField(40);
        // algorithm name field label so that it tells what is it for
        final JLabel colorMapNameLabel = new JLabel("New Algorithm Name: ");
        colorMapNameLabel.setLabelFor(colorMapName);
        // save button
        saveButton = new JButton("Save");
        saveButton.setEnabled(false);
        saveButton.addActionListener(e ->{
            // create a background to save the generated image to file
            SaveColorMapTask saveColorMapTask = new SaveColorMapTask();
            saveColorMapTask.execute();
        });

        // add everything to the layout
        buttonPanel.add(colorMapNameLabel);
        buttonPanel.add(colorMapName);
        buttonPanel.add(saveButton);
        add(buttonPanel, BorderLayout.SOUTH);

        setMinimumSize(GlobalSetting.MINIMUM_WINDOW_SIZE);
        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true); // shows jframe
        requestFocus();
        setTitle("Color Map Manager");
    }



    private class ColorMapComputeTask extends SwingWorker<Void, Void> {
        BufferedImage source;

        public ColorMapComputeTask(BufferedImage source) {
            super();
            this.source = source;
        }

        @Override
        protected Void doInBackground() {
            // retrieve the width and height to lessen the overhead
            int width = source.getWidth(), height = source.getHeight();
            // create the image to be rendered
            colormap = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            // foreach pixel compute a color
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    // set the active state of the units (check if the rectangle is all inside the source image
                    for (AlgorithmUnit unit : StartApp.algorithm.getUnits()) {
                        int[] rect = unit.getRect();
                        unit.setActive(
                                (x + rect[0] > 0 ) && (y + rect[1] > 0 ) && (x + rect[0] + rect[2] < width) && (y + rect[1] + rect[3] < height)
                        );
                    }
                    // average out the values compute by the algorithm
                    double avg = averageValues(StartApp.algorithm.compute(source, new Point(x, y)));
                    // map the [0.0, 1.0] range of values to a RGB color trough the HSB color model
                    Color currentPixel = Color.getHSBColor((float) avg, 1.0f, 0.8f);
                    // set the current colormap pixel to the compute color
                    colormap.setRGB(x, y, currentPixel.getRGB());
                }
                // update the progress each percentage
                if( x % (width / 100) == 0) {
                    setProgress(x * 100 / width);
                }
            }
            return null;
        }

        /**
         * returns the average value from an array
         * @param values array of doubles
         * @return the average double
         */
        private double averageValues(double[] values){
            double avg = 0.0;

            if(values != null){
                for(double value : values)
                    avg += value;
                avg /= values.length;
            }

            return avg;
        }

        @Override
        protected void done() {
            // remove the progress bar
            ColorMap.this.remove(progressBar);
            // add the imagepanel where the progress bar was
            ColorMap.this.add(imagePanel, BorderLayout.CENTER);
            // valide the jframe so it updated the gui
            ColorMap.this.validate();

            // set the shown image of the image panel to the generated image
            imagePanel.setImage(colormap);
            // enable saving
            saveButton.setEnabled(true);
        }
    }

    private class SaveColorMapTask extends  SwingWorker<Void, Void>{
        /**
         * tells if the process was stopped by the user voluntarily
         * used to change the done message of the SwingWorker
         */
        boolean interrupted;
        /**
         * path of the saved generated image
         */
        String colorMapFilename;

        @Override
        protected Void doInBackground() {
            // set default of interrupted
            interrupted = false;
            // create the pathname for the generated image file
            colorMapFilename = DEFAULT_FOLDER + "/" + colorMapName.getText() + ".png";
            // create the file given its pathname
            File newFile = new File(colorMapFilename);

            // if there's a filename that has the same name ask to overwrite it
            if(newFile.exists()){
                if(JOptionPane.showConfirmDialog(null, "A file has already the existing name, do you wanna overwrite it?") != JOptionPane.OK_OPTION){
                    interrupted = true;
                    return null;
                }
            }

            // write the buffered image in the image panel to the created filename
            try {
                boolean newFileCreated = newFile.createNewFile();
                ImageIO.write(imagePanel.getImage(), "png", newFile);
            } catch (IOException ioException) {
                JOptionPane.showMessageDialog(null, "An error has occured in the saving process of the color map, retry");
            }

            return null;
        }

        @Override
        protected void done() {
            super.done();
            // if the task wasn't interrupted by the user show then the saving process has been completed without erro
            if(!interrupted){
                JOptionPane.showMessageDialog(null, "The colormap has been save to file: " + colorMapFilename);
            }
        }
    }
}
