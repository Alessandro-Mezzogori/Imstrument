package imstrument.sound.utils;

import imstrument.start.StartApp;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

/**
 * extension of ImagePanel class providing audio generation on triggers
 */
public class SoundImagePanel extends ImagePanel{
    public SoundImagePanel(URL url, Dimension margins, Point startingPoint, boolean centerimage) {
        super(url, margins, startingPoint, centerimage);

        this.addMouseListener(new ImageMouseListener());
    }

    public SoundImagePanel(){
        super();

        this.addMouseListener(new ImageMouseListener());
    }

    /**
     * Mouse adapter to link mouse click on the image to the generation of sound
     */
    private class ImageMouseListener extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            if (image != null) {
                //Point p = e.getPoint();
                //p.x -= currentStartCorner.x;
                //p.y -= currentStartCorner.y;
                //Color pixelColor = new Color(image.getRGB(p.x, p.y));

                /* stops the audio thread from starting over and over again for performance and quality */
                if (!StartApp.audioThread.isRunning()) {
                    StartApp.waveSummer.start();
                    StartApp.audioThread.triggerPlayback();
                }

            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            StartApp.waveSummer.stop();
        }
    }
}
