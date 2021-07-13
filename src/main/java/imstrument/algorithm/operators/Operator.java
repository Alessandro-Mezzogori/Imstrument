package imstrument.algorithm.operators;

import java.awt.*;

public interface Operator {
    /**
     * computes a value between 0 and 1 following some set of rules
     * @param pixels array of ARGB data
     * @return a value beetween [0.0,1.0]
     */
    double compute(Color[] pixels);

    /**
     * Retrieves the name of operator
     * @return the name of the operator in a string object
     */
    String getName();

    /**
     * Returns the associated color of the operator
     * @return the color of the operator
     */
    Color getColor();
}
