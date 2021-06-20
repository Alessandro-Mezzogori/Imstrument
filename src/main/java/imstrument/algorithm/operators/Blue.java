package imstrument.algorithm.operators;

import java.awt.*;

public class Blue implements Operator{
    public static String name = "BLUE";

    @Override
    public double compute(Color[] pixels) {
        // luminance = (r * 0.3) + (g * 0.59) + (b * 0.11)
        double blue = 0.0;
        for (Color pixel : pixels) {
            blue += pixel.getBlue();
        }
        return blue / pixels.length / 255.0;
    }

    @Override
    public String getName(){return name;}

    @Override
    public Color getColor(){return Color.blue; }
}
