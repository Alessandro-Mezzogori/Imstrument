package imstrument.algorithm;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Algorithm {
    Point[] points;

    public Algorithm(){
        points = new Point[0];
    }

    /**
     * method to compute changes in the mouse SoundwaveSummer on based
     * on the point clicked on the image
     *
     * should affect: Envelope and/or frequency of the Mouse SoundwaveSummer
     */
    public abstract void computeSoundWave(BufferedImage image);
    public abstract void computePoints(BufferedImage image, Point clickedPoint);
    public Point[] getPoints() {return  points;}
}
