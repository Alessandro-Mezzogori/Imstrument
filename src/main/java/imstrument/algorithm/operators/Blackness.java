package imstrument.algorithm.operators;

import java.awt.*;

public class Blackness implements Operator{
    public static String name = "BLACKNESS";
    @Override
    public double compute(Color[] pixels) {
        // luminance = (r * 0.3) + (g * 0.59) + (b * 0.11)
        double luminance = 0.0;
        for (Color pixel : pixels) {
            luminance += pixel.getRed()*0.2126 + pixel.getGreen()*0.7152 + pixel.getBlue()*0.0722;
        }
        return 255 - (luminance/ pixels.length);
    }

    @Override
    public String getName() { return name; }

    @Override
    public Color getColor(){return Color.black; }
}
