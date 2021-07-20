package imstrument.sound.utils;

import imstrument.algorithm.AlgorithmUnit;
import imstrument.sound.waves.WaveManager;
import imstrument.start.StartApp;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * extension of ImagePanel class providing audio generation on triggers
 */
public class SoundImagePanel extends ImagePanel{
    /**
     * tells if it will draw the mouse or not
     */
    private boolean drawMouse;

    /**
     * current mouse location
     */
    Point mousePoint;

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
            // draw each unit's rectangle
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
     *  sets the active state of a algorithm unit based on the mouse location
     *  if a unit has some part of its rectangle outside the boundary of the scaled image
     *  then it will be set to inactive
     */
    private void updateUnitActiveState(){
        for(AlgorithmUnit unit : StartApp.algorithm.getUnits()){
            int[] rect = unit.getRect();
            int x = mousePoint.x + currentStartCorner.x + rect[0];
            int y = mousePoint.y + currentStartCorner.y + rect[1];
            unit.setActive(scaledImageContains(x, y) && scaledImageContains(x + rect[2], y + rect[3]));
        }
    }

    /**
     * compute the soundwave trough the currently selected algorithm and play it
     */
    private void computeSoundwave(){
        if (scaledImage != null) {
            updateUnitActiveState();

            StartApp.algorithm.computeAndAssign(
                    scaledImage,
                    mousePoint,
                    StartApp.waveManager.soundwaves.get(WaveManager.MOUSE_SOUNDWAVE_INDEX)
            );

            /* stops the audio thread from starting over and over again for performance and quality */
            if(!StartApp.algorithm.isAllUnitsDeactivated()) {
                StartApp.waveManager.triggerWaveGeneration(WaveManager.MOUSE_SOUNDWAVE_INDEX);
                if (StartApp.audioThread.isNotRunning()) {
                    StartApp.audioThread.triggerPlayback();
                }
            }
        }
    }

    /**
     * Mouse adapter to link mouse click on the image to the generation of sound
     */
    private class ImageMouseListener extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            // when clicked compute the soundwave at that click
            extractMousePoint(e);
            computeSoundwave();
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            // get the mouse location
            extractMousePoint(e);
            // check if the mouse should be rendered in the image
            drawMouse = SoundImagePanel.this.contains(mousePoint);
            // update the state of the panel
            updateUnitActiveState();
            // repaint
            SoundImagePanel.this.repaint();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            // stop playing the Mouse soundwave when the mouse is released
            StartApp.waveManager.setShouldGenerate(false, WaveManager.MOUSE_SOUNDWAVE_INDEX);
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            // if there's a loaded image then draw the mouse when it enters the component
            if(scaledImage != null) {
                drawMouse = true;
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            // stop the mouse drawing when it exits
            drawMouse = false;
        }

        /**
         * exctracts the mouse location from a mouse event
         * @param e mouse event from which to extract the mouse location
         */
        private void extractMousePoint(MouseEvent e){
            mousePoint = e.getPoint();
            mousePoint.x -= currentStartCorner.x;
            mousePoint.y -= currentStartCorner.y;
        }
    }
}
