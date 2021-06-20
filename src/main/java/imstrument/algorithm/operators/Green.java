package imstrument.algorithm.operators;

import java.awt.*;

public class Green implements Operator{
    public static String name = "GREEN";

    @Override
    public double compute(Color[] pixels) {
        // luminance = (r * 0.3) + (g * 0.59) + (b * 0.11)
        double green = 0.0;
        for (Color pixel : pixels) {
            green += pixel.getGreen();
        }
        return green / pixels.length / 255.0;
    }

    @Override
    public String getName(){return name;}

    @Override
    public Color getColor(){return Color.green; }
}
