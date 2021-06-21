package imstrument.sound.utils;

import imstrument.algorithm.AlgorithmUnit;
import imstrument.sound.waves.Soundwaves;
import imstrument.sound.waves.WaveManager;
import imstrument.start.StartApp;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

/**
 * extension of ImagePanel class providing audio generation on triggers
 */
public class SoundImagePanel extends ImagePanel{

    private boolean drawMouse;

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
            for (AlgorithmUnit unit : StartApp.algorithm.getUnits()) {
                int[] rect = unit.getRect();

                /* check if it's inside the image */
                g.setColor(unit.isActive() ? Color.red : Color.red.darker().darker());
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
            computeSoundwave(e);
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            extractMousePoint(e);
            drawMouse = SoundImagePanel.this.contains(mousePoint);
            updateUnitActiveState();
            SoundImagePanel.this.repaint();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            StartApp.waveManager.setShouldGenerate(false, WaveManager.MOUSE_SOUNDWAVE_INDEX);
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            if(scaledImage != null) {
                drawMouse = true;
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            drawMouse = false;
        }

        private void updateUnitActiveState(){
            for(AlgorithmUnit unit : StartApp.algorithm.getUnits()){
                int[] rect = unit.getRect();
                int x = mousePoint.x + currentStartCorner.x + rect[0];
                int y = mousePoint.y + currentStartCorner.y + rect[1];
                unit.setActive(imageContains(x, y) && imageContains(x + rect[2], y + rect[3]));
            }
        }

        private void extractMousePoint(MouseEvent e){
            mousePoint = e.getPoint();
            mousePoint.x -= currentStartCorner.x;
            mousePoint.y -= currentStartCorner.y;
        }

        private void computeSoundwave(MouseEvent e){
            if (scaledImage != null) {
                extractMousePoint(e);
                updateUnitActiveState();

                StartApp.algorithm.computeAndAssign(
                        scaledImage,
                        mousePoint,
                        StartApp.waveManager.soundwaves.get(WaveManager.MOUSE_SOUNDWAVE_INDEX)
                );

                /* stops the audio thread from starting over and over again for performance and quality */
                if(!StartApp.algorithm.isAllUnitsDeactivated()) {
                    StartApp.waveManager.triggerWaveGeneration(WaveManager.MOUSE_SOUNDWAVE_INDEX);
                    if (!StartApp.audioThread.isRunning()) {
                        StartApp.audioThread.triggerPlayback();
                    }
                }
            }
        }
    }
}
