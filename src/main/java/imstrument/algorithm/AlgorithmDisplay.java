package imstrument.algorithm;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class AlgorithmDisplay extends JComponent {
    protected static final Dimension STANDARD_ALGO_DISPLAY_SIZE = new Dimension(100, 100);
    protected ArrayList<AlgorithmUnit> groups;
    protected Point centerPoint;

    public AlgorithmDisplay(ArrayList<AlgorithmUnit> groups){
        /* set dimensions */
        setMinimumSize(STANDARD_ALGO_DISPLAY_SIZE);
        setPreferredSize(STANDARD_ALGO_DISPLAY_SIZE);
        setMaximumSize(STANDARD_ALGO_DISPLAY_SIZE);

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
                    centerPoint.x + group.rect[0],
                    centerPoint.y + group.rect[1],
                    (group.rect[2]),
                    (group.rect[3])
            );
        }
    }
}
