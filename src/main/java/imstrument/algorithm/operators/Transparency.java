package imstrument.algorithm.operators;

import java.awt.*;

public class Transparency implements Operator{
    public static String name = "TRANSPARENCY";

    @Override
    public double compute(Color[] pixels) {
        // luminance = (r * 0.3) + (g * 0.59) + (b * 0.11)
        double alpha = 0.0;
        for (Color pixel : pixels) {
            alpha += pixel.getAlpha();
        }
        return 255 - (alpha / pixels.length);
    }

    @Override
    public String getName(){return name;}
}
