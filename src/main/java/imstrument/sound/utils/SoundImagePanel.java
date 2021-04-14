package imstrument.sound.utils;

import imstrument.start.StartApp;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

public class SoundImagePanel extends ImagePanel{
    public SoundImagePanel(URL url, Dimension margins, Point startingPoint, boolean centerimage) {
        super(url, margins, startingPoint, centerimage);

        this.addMouseListener(new ImageMouseListener());
    }

    public SoundImagePanel(){
        super();

        this.addMouseListener(new ImageMouseListener());
    }

    private class ImageMouseListener extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            if (image != null) {
                //Point p = e.getPoint();
                //p.x -= currentStartCorner.x;
                //p.y -= currentStartCorner.y;
                //Color pixelColor = new Color(image.getRGB(p.x, p.y));
                if (!StartApp.audioThread.isRunning()) {
                    // reset wave before starting a new sample generation
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
