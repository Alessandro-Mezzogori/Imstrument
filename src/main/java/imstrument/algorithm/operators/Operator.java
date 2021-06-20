package imstrument.algorithm.operators;

import java.awt.*;

public interface Operator {
    /**
     * computes a value between 0 and 1 following some set of rules
     * @param pixels array of ARGB data
     * @return a value beetween [0.0,1.0]
     */
    double compute(Color[] pixels);
    String getName();
    Color getColor();
}
