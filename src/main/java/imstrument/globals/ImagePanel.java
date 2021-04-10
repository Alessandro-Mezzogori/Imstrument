package imstrument.globals;

import imstrument.sound.openal.AudioThread;
import imstrument.sound.waves.Envelope;
import imstrument.sound.waves.Wave;
import imstrument.sound.waves.WaveSummer;
import imstrument.sound.waves.WaveType;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * JPanel specialized to show images
 */
public class ImagePanel extends JPanel {
    private BufferedImage image;
    private final Dimension margins;
    private final Point startingPoint;
    private final Point currentStartCorner;
    private final boolean centerImage;

    /* audio thread params */
    AudioThread audioThread;
    private final WaveSummer waveSummer;

    public ImagePanel(URL url, Dimension margins, Point startingPoint, boolean centerimage) {
        /*
            roba temporanea per i test sulla generazione del suono
            verrano sostituiti dalle manipolazioni create dettate
            dall'algoritmo scelto
         */
        ArrayList<Wave> waves = new ArrayList<Wave>();
        Wave wave1 = new Wave(Short.MAX_VALUE, 440, new Envelope(0.5, 0.1, 0.2, 1));
        wave1.setWaveform(WaveType.SINE);

        //TODO check for attack time not being registered in the modulating wave
        Wave modulating = new Wave((short)1, 80, new Envelope( 1, 0, 0.2, 1));
        modulating.setWaveform(WaveType.SINE);

        wave1.setModulatingWave(modulating, 2);
        /* initialize audio thread and WaveSummer*/
        waves.add(wave1);
        waveSummer = new WaveSummer(waves);
        audioThread = new AudioThread(() -> {
                if(waveSummer.isShouldGenerate()) {
                    short[] samples = new short[AudioThread.BUFFER_SIZE];
                    for (int i = 0; i < AudioThread.BUFFER_SIZE; i++) {
                        samples[i] = waveSummer.generateSample();
                    }
                    return samples;
                }
                return null;
            }
        );

        /* set default coordinates of image in panel*/
        this.startingPoint = startingPoint;
        /* initialize currentStartCorner of the rendering*/
        this.currentStartCorner = new Point();
        /* set other params of imagepanel*/
        this.centerImage = centerimage;
        this.margins = margins;
        /* attempt to retrieve image from path */
        try {
            this.image = ImageIO.read(url);
        } catch (IOException e) {
            //TODO temporary stack trace and system print
            e.printStackTrace();
            System.out.println("Image not found at path: " + url.toString());

            this.image = null;
        } catch (IllegalArgumentException e) {
            //TODO temporary stack trace and system print
            //e.printStackTrace();
            this.image = null;
        }

        this.addMouseListener(new ImageMouseListener());

        /* initialize synth */
    }

    public ImagePanel() {
        this(null, new Dimension(0, 0), new Point(0, 0), true);
    }

    public ImagePanel(URL url) {
        this(url, new Dimension(0, 0), new Point(0, 0), true);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            Dimension parentSize = this.getSize();
            Dimension imageSize = this.getPreferredSize();
            this.currentStartCorner.x = this.startingPoint.x;
            this.currentStartCorner.y = this.startingPoint.y;
            DimensionComparator dimensionComparator = new DimensionComparator();
            System.out.println(parentSize);
            System.out.println(imageSize);

            if (dimensionComparator.isBigger(imageSize, parentSize)) {

                imageSize = this.getScaledSize(true);
                System.out.println(imageSize);
            }

            if (this.centerImage) {
                this.currentStartCorner.x = (parentSize.width - imageSize.width) / 2 - this.margins.width;
                this.currentStartCorner.y = (parentSize.height - imageSize.height) / 2 - this.margins.height;
            }
            //TODO rimpiazzare con OPENCV per migliore qualità e velocità
            g.drawImage(image, this.currentStartCorner.x, this.currentStartCorner.y, imageSize.width, imageSize.height, this);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return image != null ? new Dimension(image.getWidth(), image.getHeight()) : super.getPreferredSize();
    }

    @Override
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }

    /**
     * Returns the Dimension of the image scaled to the Dimension of the JPanel
     * if respectRatio is true it chooses the side with the biggest difference
     * from the parent to resize correctly.
     *
     * @param respectRatio if true it returns with the same ratio of the image
     * @return scaled dimension of the image
     */
    public Dimension getScaledSize(boolean respectRatio) {
        Dimension scaledDimension = new Dimension();
        Dimension panelDimension = this.getSize();
        Dimension imageDimension = this.getPreferredSize();

        int widthDifference = imageDimension.width - panelDimension.width;
        int heightDifference = imageDimension.height - panelDimension.height;

        if (respectRatio) {
            double aspectRatio = ((double) imageDimension.width) / imageDimension.height;
            if (widthDifference <= heightDifference) {
                scaledDimension.height = imageDimension.height - heightDifference;
                scaledDimension.width = (int) (aspectRatio * scaledDimension.height);
            } else {
                scaledDimension.width = imageDimension.width - widthDifference;
                scaledDimension.height = (int) (scaledDimension.width / aspectRatio);
            }
        } else {
            scaledDimension.height = imageDimension.height - heightDifference;
            scaledDimension.width = imageDimension.width - widthDifference;
        }
        return scaledDimension;
    }

    /**
     * sets new image of ImagePanel and updates the current shown image
     *
     * @param image image to be shown in the ImagePanel
     */
    public void setImage(BufferedImage image) {
        this.image = image;
        this.repaint();
    }

    private class ImageMouseListener extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            if (image != null) {
                //Point p = e.getPoint();
                //p.x -= currentStartCorner.x;
                //p.y -= currentStartCorner.y;
                //Color pixelColor = new Color(image.getRGB(p.x, p.y));
                if (!audioThread.isRunning()) {
                    waveSummer.start();
                    audioThread.triggerPlayback();
                }

            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            waveSummer.stop();
        }
    }

    /* audio thread cleanup on closing*/
    public void closeAudioThread(){
        audioThread.close();
    }
}
