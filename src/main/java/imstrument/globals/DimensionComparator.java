package imstrument.globals;

import java.awt.*;
import java.util.Comparator;

public class DimensionComparator implements Comparator<Dimension> {
    /**
     * compares the area of the two dimensions
     * @param d1 first dimension
     * @param d2 second dimension
     * @return -1, 0, 1 if d1 area is lower than, equal to, or greater than d2
     */
    @Override
    public int compare(Dimension d1, Dimension d2) {
        return Integer.compare(d1.height * d1.width, d2.height * d2.width);
    }

    /**
     * tells if width or height of d1 is bigger than the width or height of d2
     * @param d1 first dimension
     * @param d2 second dimension
     * @return the result of the comparison
     */
    public boolean isBigger(Dimension d1, Dimension d2){
        return d1.height > d2.height || d1.width > d2.width;
    }
}
