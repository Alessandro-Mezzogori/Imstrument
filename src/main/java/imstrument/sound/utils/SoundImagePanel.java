package imstrument.sound.utils;

import imstrument.algorithm.Algorithm;
import imstrument.algorithm.AlgorithmUnit;
import imstrument.sound.waves.WaveManager;
import imstrument.start.StartApp;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.ArrayList;

/**
 * extension of ImagePanel class providing audio generation on triggers
 */
public class SoundImagePanel extends ImagePanel{

    private boolean drawMouse;
    Algorithm algorithm = new Algorithm(
            "_<-9,-9,3,3><RED>_" +
            "_<-6,-6,3,3><RED>_" +
            "_<-3,-3,3,3><RED>_" +
            "_<0,0,3,3><AVGLUMINANCE>_" +
            "_<3,3,3,3><AVGLUMINANCE>_" +
            "_<6,6,3,3><AVGLUMINANCE>_" +
            "_<-9,6,3,3><AVGLUMINANCE>_" +
            "_<-6,3,3,3><AVGLUMINANCE>_" +
            "_<0,-3,3,3><AVGLUMINANCE>_" +
            "_<3,-6,3,3><AVGLUMINANCE>_"
    );
    Point mousePoint;


    public SoundImagePanel(URL url, Dimension margins, Point startingPoint, boolean centerimage) {
        super(url, margins, startingPoint, centerimage);

        drawMouse = false;
        this.addMouseListener(new ImageMouseListener());
    }

    public SoundImagePanel(){
        super();

        drawMouse = false;

        ImageMouseListener listener = new ImageMouseListener();
        this.addMouseListener(listener);
        this.addMouseMotionListener(listener);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(drawMouse) {
            g.setColor(Color.red);
            for (AlgorithmUnit unit : algorithm.getGroups()) {
                int[] rect = unit.getRect();
                g.fillRect(
                        mousePoint.x + currentStartCorner.x + rect[0],
                        mousePoint.y + currentStartCorner.y + rect[1],
                        rect[2],
                        rect[3]
                );
            }
        }
    }

    /**
     * Mouse adapter to link mouse click on the image to the generation of sound
     */
    private class ImageMouseListener extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            if (image != null) {
                Point p = e.getPoint();
                p.x -= currentStartCorner.x;
                p.y -= currentStartCorner.y;

                algorithm.compute(image, p);

                /* stops the audio thread from starting over and over again for performance and quality */
                StartApp.waveManager.triggerWaveGeneration(WaveManager.MOUSE_SOUNDWAVE_INDEX);
                if (!StartApp.audioThread.isRunning()) {
                    StartApp.audioThread.triggerPlayback();
                }

            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            if(drawMouse) {
                mousePoint = e.getPoint();
                mousePoint.x -= currentStartCorner.x;
                mousePoint.y -= currentStartCorner.y;

                //soundAlgorithm.computePoints(image, p);
                ((SoundImagePanel) e.getSource()).repaint();
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            StartApp.waveManager.setShouldGenerate(false, WaveManager.MOUSE_SOUNDWAVE_INDEX);
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            if(image != null) {
                drawMouse = true;
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            drawMouse = false;
        }
    }
}
