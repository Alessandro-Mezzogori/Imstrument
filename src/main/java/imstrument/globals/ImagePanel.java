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

public class ImagePanel extends JPanel {
    private BufferedImage image;
    private int x, y;

    public ImagePanel(URL url){
        /* set default coordinates of image in panel*/
        x = 0;
        y = 0;
        /* attempt to retrieve image from path */
        try{
            image = ImageIO.read(url);
        } catch (IOException e){
            // TODO imlement error catching
            e.printStackTrace();
            System.out.println("Image not found at path: " + url.toString());
        }
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Dimension size = this.getSize();
        g.drawImage(image.getScaledInstance(size.width, size.height, BufferedImage.TYPE_INT_RGB),0, 0, this);
    }
    
    @Override
    public Dimension getPreferredSize() {
        return image != null ? new Dimension(image.getWidth(), image.getHeight()) : super.getPreferredSize();
    }

    @Override
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }
}
