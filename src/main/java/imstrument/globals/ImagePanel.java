package imstrument.globals;

import imstrument.sound.openal.AudioThread;
import imstrument.sound.waves.SawToothWave;
import imstrument.sound.waves.SineWave;
import imstrument.sound.waves.Wave;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

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
    private boolean shouldGenerate;
    private final Wave wave;
    public ImagePanel(URL url, Dimension margins, Point startingPoint, boolean centerimage) {
        /* initialize audio thread*/
        //wave = new SineWave(Short.MAX_VALUE, 440);
        wave = new SawToothWave(Short.MAX_VALUE, 440);
        audioThread = new AudioThread(()->
            {
                if(!shouldGenerate){
                    return null;
                }
                return wave.generate(AudioThread.BUFFER_SIZE,AudioThread.SAMPLE_RATE);
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
        } catch (IllegalArgumentException e){
            //TODO temporary stack trace and system print
            //e.printStackTrace();
            this.image = null;
        }

        this.addMouseListener(new ImageMouseListener());

        /* initialize synth */
    }

    public ImagePanel(){
        this(null, new Dimension(0, 0), new Point(0, 0), true);
    }

    public ImagePanel(URL url){
        this(url, new Dimension(0, 0), new Point(0, 0), true);
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        if(image != null) {
            Dimension parentSize = this.getSize();
            Dimension imageSize = this.getPreferredSize();
            this.currentStartCorner.x = this.startingPoint.x;
            this.currentStartCorner.y = this.startingPoint.y;
            DimensionComparator dimensionComparator = new DimensionComparator();
            System.out.println(parentSize);
            System.out.println(imageSize);

            if(dimensionComparator.isBigger(imageSize, parentSize)) {

                imageSize = this.getScaledSize(true);
                System.out.println(imageSize);
            }

            if (this.centerImage) {
                this.currentStartCorner.x = (parentSize.width - imageSize.width)/2 - this.margins.width;
                this.currentStartCorner.y = (parentSize.height - imageSize.height)/2 - this.margins.height;
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
     * @param respectRatio if true it returns with the same ratio of the image
     * @return scaled dimension of the image
     */
    public Dimension getScaledSize(boolean respectRatio){
        Dimension scaledDimension = new Dimension();
        Dimension panelDimension = this.getSize();
        Dimension imageDimension = this.getPreferredSize();

        int widthDifference = imageDimension.width - panelDimension.width;
        int heightDifference = imageDimension.height - panelDimension.height;

        if(respectRatio){
            double aspectRatio = ((double) imageDimension.width)/imageDimension.height;
            if(widthDifference <= heightDifference){
                scaledDimension.height = imageDimension.height - heightDifference;
                scaledDimension.width = (int) (aspectRatio*scaledDimension.height);
            }
            else{
                scaledDimension.width = imageDimension.width - widthDifference;
                scaledDimension.height = (int) (scaledDimension.width / aspectRatio);
            }
        }
        else{
            scaledDimension.height = imageDimension.height - heightDifference;
            scaledDimension.width = imageDimension.width - widthDifference;
        }
        return scaledDimension;
    }

    /**
     * sets new image of ImagePanel and updates the current shown image
     * @param image image to be shown in the ImagePanel
     */
    public void setImage(BufferedImage image) {
        this.image = image;
        this.repaint();
    }

    private class ImageMouseListener extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            if(image != null) {
                //Point p = e.getPoint();
                //p.x -= currentStartCorner.x;
                //p.y -= currentStartCorner.y;
                //Color pixelColor = new Color(image.getRGB(p.x, p.y));

                if(!audioThread.isRunning()) {
                    shouldGenerate = true;
                    audioThread.triggerPlayback();
                }
            }
        }

        @Override
        public void mouseReleased(MouseEvent e){
            shouldGenerate = false;
        }
    }

    /* audio thread cleanup on closing*/
    public void closeAudioThread(){
        audioThread.close();
    }
}
