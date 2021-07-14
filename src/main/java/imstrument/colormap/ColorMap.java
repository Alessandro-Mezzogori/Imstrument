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
    ImagePanel imagePanel;
    BufferedImage colormap;
    JProgressBar progressBar;

    JTextField colorMapName;
    JButton saveButton;

    public static File DEFAULT_FOLDER = new File(StartApp.defaultFolder + "/colormaps/");

    static{
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

        imagePanel = new ImagePanel();
        progressBar = new JProgressBar(JProgressBar.HORIZONTAL, 0, 100);
        progressBar.setStringPainted(true);
        progressBar.setValue(0);

        SwingWorker<Void, Void> task = new ColorMapComputeTask(source);
        task.addPropertyChangeListener(evt -> {
            if ("progress".equals(evt.getPropertyName())) {
                int progress = (Integer) evt.getNewValue();
                progressBar.setValue(progress);
            }
        });
        task.execute();

        setLayout(new BorderLayout());
        add(progressBar, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        colorMapName = new JTextField(40);
        // algorithm name field label so that it tells what is it for
        final JLabel colorMapNameLabel = new JLabel("New Algorithm Name: ");
        colorMapNameLabel.setLabelFor(colorMapName);
        // save button
        saveButton = new JButton("Save");
        saveButton.setEnabled(false);
        saveButton.addActionListener(e ->{
            SaveColorMapTask saveColorMapTask = new SaveColorMapTask();
            saveColorMapTask.execute();
        });


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
            ColorMap.this.remove(progressBar);
            ColorMap.this.add(imagePanel, BorderLayout.CENTER);
            ColorMap.this.validate();

            imagePanel.setImage(colormap);
            saveButton.setEnabled(true);
        }
    }

    private class SaveColorMapTask extends  SwingWorker<Void, Void>{
        boolean interrupted;
        String colorMapFilename;

        @Override
        protected Void doInBackground() {
            interrupted = false;
            colorMapFilename = DEFAULT_FOLDER + "/" + colorMapName.getText() + ".png";
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
            if(!interrupted){
                JOptionPane.showMessageDialog(null, "The colormap has been save to file: " + colorMapFilename);
            }
        }
    }
}
