package imstrument.algorithm;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class AlgorithmDisplay extends JComponent {
    /**
     * Dimension used to "normalize the retctangle of all the created units so they work for smaller images (100x100)
     */
    protected static final Dimension STANDARD_USAGE_SIZE = new Dimension(100, 100);
    /**
     * Display size of the canvas used when viewing and/or creating an algorithm
     */
    protected static final Dimension ALGORITHM_DISPLAY_SIZE = new Dimension(400, 400);
    /**
     * Ratio used to standardize the rectangles of all the created units to be inside a 100x100 square
     */
    protected static final int STANDARDIZING_RATIO = (int) Math.round(((double)(AlgorithmDisplay.ALGORITHM_DISPLAY_SIZE.width)/AlgorithmDisplay.STANDARD_USAGE_SIZE.width));

    /**
     * Standard unit color if there's not operator assigned
     */
    protected static final Color STANDARD_UNIT_COLOR = Color.CYAN;

    /**
     *  Reference to the alogirthm units that are shown in the component
     */
    protected ArrayList<AlgorithmUnit> groups;
    /**
     * center point of the canvas
     */
    protected Point centerPoint;

    public AlgorithmDisplay(ArrayList<AlgorithmUnit> groups){
        /* set dimensions */
        setMinimumSize(ALGORITHM_DISPLAY_SIZE);
        setPreferredSize(ALGORITHM_DISPLAY_SIZE);
        setMaximumSize(ALGORITHM_DISPLAY_SIZE);

        /* initialize values */
        this.groups = groups;

        /* compute center point */
        centerPoint = new Point();

        setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.RED));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // compute the center point
        Dimension currentSize = getSize();
        centerPoint.x = currentSize.width/2;
        centerPoint.y = currentSize.height/2;

        /* draw center square  */
        g.setColor(Color.magenta);
        g.fillRect(centerPoint.x - 1, centerPoint.y - 1, 2, 2);

        /* draw saved groups */
        for(AlgorithmUnit unit : groups){
            // if there's an operator used its color else go with cyan
            g.setColor(unit.operator == null ? STANDARD_UNIT_COLOR : unit.operator.getColor());

            g.drawRect(
                    centerPoint.x + unit.rect[0] * STANDARDIZING_RATIO,
                    centerPoint.y + unit.rect[1] * STANDARDIZING_RATIO,
                    unit.rect[2] * STANDARDIZING_RATIO,
                    unit.rect[3] * STANDARDIZING_RATIO
            );
        }
    }
}
