package imstrument.algorithm.operators;

import java.awt.*;

public class Red implements Operator{
    public static String name = "RED";

    @Override
    public double compute(Color[] pixels) {
        // luminance = (r * 0.3) + (g * 0.59) + (b * 0.11)
        double red = 0.0;
        for (Color pixel : pixels) {
            red += pixel.getRed();
        }
        return red / pixels.length/ 255.0;
    }

    @Override
    public String getName(){return name;}

    @Override
    public Color getColor(){return Color.red; }
}
