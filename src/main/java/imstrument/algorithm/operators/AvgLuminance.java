package imstrument.algorithm.operators;

import java.awt.*;

public class AvgLuminance implements Operator{
    public static String name = "AVGLUMINANCE";
    @Override
    public double compute(Color[] pixels) {
        // luminance = (r * 0.3) + (g * 0.59) + (b * 0.11)
        double luminance = 0.0;
        for (Color pixel : pixels) {
            luminance += pixel.getRed()*0.3 + pixel.getGreen()*0.59 + pixel.getBlue()*0.11;
        }
        return luminance/ pixels.length;
    }

    @Override
    public String getName() { return name; }
}
