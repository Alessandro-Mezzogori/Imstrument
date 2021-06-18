package imstrument.algorithm;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class AlgorithmDisplay extends JComponent {
    protected static final Dimension STANDARD_USAGE_SIZE = new Dimension(100, 100);
    protected static final Dimension ALGORITHM_DISPLAY_SIZE = new Dimension(300, 300);
    protected static final int STANDARDIZING_RATIO = (int) Math.round(((double)(AlgorithmDisplay.ALGORITHM_DISPLAY_SIZE.width)/AlgorithmDisplay.STANDARD_USAGE_SIZE.width));

    protected ArrayList<AlgorithmUnit> groups;
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
        Dimension currentSize = getSize();
        centerPoint.x = currentSize.width/2;
        centerPoint.y = currentSize.height/2;

        /* draw center square  */
        g.setColor(Color.red);
        g.fillRect(centerPoint.x - 1, centerPoint.y - 1, 2, 2);

        /* draw saved groups */
        g.setColor(Color.cyan);
        for(AlgorithmUnit group : groups){
            g.drawRect(
                    centerPoint.x + group.rect[0]* STANDARDIZING_RATIO,
                    centerPoint.y + group.rect[1]* STANDARDIZING_RATIO,
                    (group.rect[2])* STANDARDIZING_RATIO,
                    (group.rect[3])* STANDARDIZING_RATIO
            );
        }
    }
}
