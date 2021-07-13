package imstrument.algorithm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * Used to draw the rectangles for the algorithm units
 */
public class AlgorithmCanvas extends AlgorithmDisplay implements MouseListener, MouseMotionListener
{
    /* amount to divide the components of currentGroup to have it inside a 100x100 square +- approximations*/

    /* drawing params */
    /**
     * current drawing group
     */
    int[] currentGroup;
    /**
     * tells if the canvas is drawing a rectangle
     */
    boolean drawingRectangle;
    /**
     * Synchronizes the Algorithm canvas and the control window where the operators are assigned
     */
    CustomAlgorithmCreator.AlgorithmCreationSynchronizer controller;

    /**
     * creates a canvas to draw/add algo units to the passed arraylist
     * @param groups array list containing the algorithmic unit of the current decoded algorithm
     * @param controller dummy object for synchronization with other diplays of groups
     */
    public AlgorithmCanvas(ArrayList<AlgorithmUnit> groups, CustomAlgorithmCreator.AlgorithmCreationSynchronizer controller){
        super(groups);
        this.controller = controller;
        /* set dimensions */

        /* initialize values */
        currentGroup = new int[AlgorithmUnit.RECT_SIZE];
        drawingRectangle = false;

        /* add listeners */
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Dimension currentSize = getSize();
        // get the current center point
        centerPoint.x = currentSize.width/2;
        centerPoint.y = currentSize.height/2;

        /* draw center square  */
        g.setColor(Color.magenta);
        g.fillRect(centerPoint.x - 2, centerPoint.y - 2, 5, 5);

        /* draw current drawing group */
        if(drawingRectangle) {
            g.setColor(STANDARD_UNIT_COLOR);
            /* have the starting drawing corner to be the top-left corner just for drawing */
            g.drawRect(
                (currentGroup[2] > 0) ? currentGroup[0] : currentGroup[0] + currentGroup[2],
                (currentGroup[3] > 0) ? currentGroup[1] : currentGroup[1] + currentGroup[3],
                Math.abs(currentGroup[2]),
                Math.abs(currentGroup[3])
            );
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // dummy function, not used
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // when the left mouse button is pressed start drawing the new rectangle
        if(SwingUtilities.isLeftMouseButton(e)){
            drawingRectangle = true;
            currentGroup[0] = e.getX();
            currentGroup[1] = e.getY();
            repaint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // when the left mouse button is release and the rectangle doesn't have a lenght 0 ( is an actual rectangle )
        // add it to the Algorithm units
        if(SwingUtilities.isLeftMouseButton(e) && drawingRectangle && currentGroup[2] != 0 && currentGroup[3] != 0) {
            /* have the starting drawing corner to be in reference to the middle point */
            if(centerPoint != null) {
                currentGroup[0] -= centerPoint.x;
                currentGroup[1] -= centerPoint.y;
            }

            // standardize the current rectangle to make it work in smaller images
            for(int i = 0, rectSize = currentGroup.length; i < rectSize; i++) { currentGroup[i] /= STANDARDIZING_RATIO; }
            /* add to the group and repaint */
            groups.add(new AlgorithmUnit(currentGroup));
            // notify the algorithm creation controller that there's been a change to the algorithmic units
            controller.update();

            /* reset */
            currentGroup[2] = 0;
            currentGroup[3] = 0;
            repaint();
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // dummy function not used
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // dummy function not used
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // if we are drawing then repaint to show the current painting rectangle
        if(drawingRectangle) {
            currentGroup[2] = e.getX() - currentGroup[0];
            currentGroup[3] = e.getY() - currentGroup[1];
            repaint();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // dummy function not used
    }

}
