package imstrument.algorithm.operators;

import java.awt.*;

public class Luminance implements Operator{
    public static String name = "AVGLUMINANCE";
    @Override
    public double compute(Color[] pixels) {
        // luminance = (r * 0.3) + (g * 0.59) + (b * 0.11)
        double luminance = 0.0;
        for (Color pixel : pixels) {
            luminance += pixel.getRed()*0.2126 + pixel.getGreen()*0.7152 + pixel.getBlue()*0.0722;
        }
        return luminance/ pixels.length / 255.0;
    }

    @Override
    public String getName() { return name; }

    @Override
    public Color getColor(){return Color.white; }
}
