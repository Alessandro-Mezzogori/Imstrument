package imstrument.algorithm.operators;

import java.awt.*;

public class Opacity implements Operator{
    public static String name = "OPACITY";

    @Override
    public double compute(Color[] pixels) {
        // luminance = (r * 0.3) + (g * 0.59) + (b * 0.11)
        double alpha = 0.0;
        for (Color pixel : pixels) {
            alpha += pixel.getAlpha();
        }
        return alpha / pixels.length / 255.0;
    }

    @Override
    public String getName(){return name;}

    @Override
    public Color getColor(){return Color.DARK_GRAY; }
}
