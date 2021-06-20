package imstrument.algorithm.operators;

import java.awt.*;

public class NotRed implements Operator{
    public static String name = "NOTRED";
    public static Color LIGHT_BLUE = new Color(173, 216, 230);

    @Override
    public double compute(Color[] pixels) {
        // luminance = (r * 0.3) + (g * 0.59) + (b * 0.11)
        double notred = 0.0;
        for (Color pixel : pixels) {
            notred += pixel.getBlue() + pixel.getGreen();
        }
        return notred / pixels.length / 510.0;
    }

    @Override
    public String getName(){return name;}

    @Override
    public Color getColor(){return LIGHT_BLUE; }
}
