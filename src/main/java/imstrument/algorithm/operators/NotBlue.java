package imstrument.algorithm.operators;

import java.awt.*;

public class NotBlue implements Operator{
    public static String name = "NOTBLUE";

    @Override
    public double compute(Color[] pixels) {
        // luminance = (r * 0.3) + (g * 0.59) + (b * 0.11)
        double notblue = 0.0;
        for (Color pixel : pixels) {
            notblue += pixel.getGreen() + pixel.getRed();
        }
        return notblue / pixels.length;
    }

    @Override
    public String getName(){return name;}

    @Override
    public Color getColor(){return Color.yellow; }
}
