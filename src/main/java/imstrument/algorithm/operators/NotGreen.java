package imstrument.algorithm.operators;

import java.awt.*;

public class NotGreen implements Operator{
    public static String name = "NOTGREEN";

    @Override
    public double compute(Color[] pixels) {
        // luminance = (r * 0.3) + (g * 0.59) + (b * 0.11)
        double notgreen = 0.0;
        for (Color pixel : pixels) {
            notgreen += pixel.getBlue() + pixel.getRed();
        }
        return notgreen / pixels.length / 510.0;
    }

    @Override
    public String getName(){return name;}

    @Override
    public Color getColor(){return Color.orange; }
}
