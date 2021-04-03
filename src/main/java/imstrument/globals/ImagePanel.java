package imstrument.globals;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.Buffer;

/**
 * JPanel specialized to show images
 */
public class ImagePanel extends JPanel {
    private BufferedImage image;
    private int x, y;

    public ImagePanel(URL url) {
        /* set default coordinates of image in panel*/
        x = 0;
        y = 0;
        /* attempt to retrieve image from path */
        try {
            image = ImageIO.read(url);
        } catch (IOException e) {
            // TODO imlement error catching
            e.printStackTrace();
            System.out.println("Image not found at path: " + url.toString());
        }
    }

    public ImagePanel(){
        image = null;
    }

    /**
     * returns the image of the ImagePanel
     * @return
     */
    public BufferedImage getImage() {
        return image;
    }

    /**
     * sets new image of ImagePanel and updates the current shown image
     * @param image image to be shown in the ImagePanel
     */
    public void setImage(BufferedImage image) {
        this.image = image;
        this.repaint();
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        if(image != null) {
            Dimension parentSize = this.getSize();
            Dimension imageSize = this.getPreferredSize();
            Dimension marginSize = new Dimension(20, 20);
            this.x = (parentSize.width - imageSize.width - marginSize.width)/2;
            this.y = (parentSize.height - imageSize.height - marginSize.height)/2;
            g.drawImage(image, this.x, this.y, this);
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

    //TODO add on click listeners and boolean field to activate the generation of sound
}
