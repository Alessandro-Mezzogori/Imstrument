package imstrument.algorithm;

import java.awt.*;

public class AvgLuminance implements Operator{
    @Override
    public double compute(Color[][] pixels) {
        // luminance = (r * 0.3) + (g * 0.59) + (b * 0.11)
        double luminance = 0.0;
        int pixelNumber = 0;
        for (Color[] pixelLine : pixels) {
            for (Color pixel : pixelLine) {
                luminance += pixel.getRed()*0.3 + pixel.getGreen()*0.59 + pixel.getBlue()*0.11;
            }
            pixelNumber += pixelLine.length;
        }

        return luminance/pixelNumber;
    }
}
