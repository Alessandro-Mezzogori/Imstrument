package imstrument.sound.utils;

import imstrument.globals.DimensionComparator;
import imstrument.start.StartApp;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 *  specialized JPanel to show images with responsive capabilities
 */
public class ImagePanel extends JPanel {
    /**
     * image to be displayed
     */
    private BufferedImage image;

    /**
     * actual image that is displayed;
     */
    protected BufferedImage scaledImage;

    /**
     * margins from the border of the ImagePanel container
     */
    protected final Dimension margins;
    /**
     *  top-left corner of the image
     */
    protected final Point startingPoint;

    /**
     * point used for centering
     */
    protected final Point currentStartCorner;

    protected Dimension currentImageSize;

    /**
     * flag, if true it will center the image inside it's container
     */
    protected final boolean centerImage;

    /**
     * construct a new ImagePanel object
     * @param url path to image file
     * @param margins margins from the border of the imagepanel container
     * @param startingPoint top-left corner of the image (0,0) to render in the top left of the container
     * @param centerimage if true it will center the image in the container
     */
    public ImagePanel(URL url, Dimension margins, Point startingPoint, boolean centerimage) {
        /* set default coordinates of image in panel*/
        this.startingPoint = startingPoint;
        /* initialize currentStartCorner of the rendering*/
        this.currentStartCorner = new Point();
        /* set other params of imagepanel*/
        this.centerImage = centerimage;
        this.margins = margins;
        this.currentImageSize = getPreferredSize();
        /* attempt to retrieve image from path */
        try {
            this.image = ImageIO.read(url);
        } catch (IOException | IllegalArgumentException e) {
            // if an error has occurred just don't load the initial image
            this.image = null;
        }
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
            /* preparing objects to do the centering math and/or rendering of the image*/
            Dimension parentSize = this.getSize();
            currentImageSize = this.getPreferredSize();
            this.currentStartCorner.x = this.startingPoint.x;
            this.currentStartCorner.y = this.startingPoint.y;
            DimensionComparator dimensionComparator = new DimensionComparator();

            /* if the image is bigger of the parent size it gets resized */
            scaledImage = image;
            if (dimensionComparator.isBigger(currentImageSize, parentSize)) {
                currentImageSize = this.getScaledSize(true);

                scaledImage = new BufferedImage(currentImageSize.width, currentImageSize.height, BufferedImage.TYPE_INT_ARGB);
                Graphics scaledImageGraphics = scaledImage.getGraphics();
                scaledImageGraphics.drawImage(image, 0, 0, currentImageSize.width, currentImageSize.height, null);
                scaledImageGraphics.dispose();
            }

            /* centers the image in the parent container */
            if (this.centerImage) {
                this.currentStartCorner.x = (parentSize.width - currentImageSize.width) / 2 - this.margins.width;
                this.currentStartCorner.y = (parentSize.height - currentImageSize.height) / 2 - this.margins.height;
            }

            g.drawImage(scaledImage, this.currentStartCorner.x, this.currentStartCorner.y, currentImageSize.width, currentImageSize.height, this);
        }
    }

    /**
     * checks it the coordinates are inside the rendered imaged
     * @param x x coordinate
     * @param y y coordinate
     * @return true if the corodinates are inside else false
     */
    public boolean imageContains(int x, int y){
        return (x >= currentStartCorner.x && x < currentStartCorner.x + currentImageSize.width) &&
                (y >= currentStartCorner.y && y < currentStartCorner.y + currentImageSize.height);
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
}
