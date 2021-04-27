package imstrument.algorithm;

import java.awt.*;
import java.awt.image.BufferedImage;

public interface Algorithm {
    /**
     * method to compute changes in the mouse SoundWave on based
     * on the point clicked on the image
     *
     * should affect: Envelope and/or frequency of the Mouse SoundWave
     */
    void compute(BufferedImage image, Point clickedPoint, boolean computeSoundWave); // TODO da splittare in due funzioni
    Point[] getPoints();
}
