package imstrument.algorithm.operators;

import java.awt.*;

public class Red implements Operator{
    @Override
    public double compute(Color[] pixels) {
        // luminance = (r * 0.3) + (g * 0.59) + (b * 0.11)
        double red = 0.0;
        for (Color pixel : pixels) {
            red += pixel.getRed();
        }
        System.out.println("L: " + pixels.length);
        return red / pixels.length;
    }
}
