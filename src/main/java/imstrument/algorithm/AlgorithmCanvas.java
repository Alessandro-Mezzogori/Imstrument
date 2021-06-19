package imstrument.algorithm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class AlgorithmCanvas extends AlgorithmDisplay implements MouseListener, MouseMotionListener
{
    /* amount to divide the components of currentGroup to have it inside a 100x100 square +- approximations*/

    /* drawing params */
    int[] currentGroup;
    boolean drawingRectangle;
    JPopupMenu rightClickOptions;
    CustomAlgorithmCreator.AlgorithmCreationSynchronizer controller;
    int currentlySelected; // index of the latest created rectangle that contains the point where it was rightClicked

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

        /* create popup menu on right click*/
        rightClickOptions = new JPopupMenu();

        JMenuItem deleteGroup = new JMenuItem("Delete");
        deleteGroup.addActionListener(e -> {
            groups.remove(currentlySelected);
            controller.update();
            AlgorithmCanvas.this.repaint();
        });
        rightClickOptions.add(deleteGroup);

        /* add listeners */
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Dimension currentSize = getSize();
        centerPoint.x = currentSize.width/2;
        centerPoint.y = currentSize.height/2;

        /* draw center square  */
        g.setColor(Color.magenta);
        g.fillRect(centerPoint.x - 2, centerPoint.y - 2, 5, 5);

        /* draw current drawing group */
        if(drawingRectangle) {
            g.setColor(Color.cyan);
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
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(SwingUtilities.isLeftMouseButton(e)){
            drawingRectangle = true;
            currentGroup[0] = e.getX();
            currentGroup[1] = e.getY();
            repaint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(SwingUtilities.isLeftMouseButton(e) && drawingRectangle && currentGroup[2] != 0 && currentGroup[3] != 0) {
            /* have the starting drawing corner to be in reference to the middle point */
            if(centerPoint != null) {
                currentGroup[0] -= centerPoint.x;
                currentGroup[1] -= centerPoint.y;
            }

            for(int i = 0, rectSize = currentGroup.length; i < rectSize; i++) { currentGroup[i] /= STANDARDIZING_RATIO; }
            /* add to the group and repaint */
            groups.add(new AlgorithmUnit(currentGroup));
            controller.update();

            /* reset */
            currentGroup[2] = 0;
            currentGroup[3] = 0;
            repaint();
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if(drawingRectangle) {
            currentGroup[2] = e.getX() - currentGroup[0];
            currentGroup[3] = e.getY() - currentGroup[1];
            repaint();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    private void printGroup(int[] group){
        System.out.println("X: " + group[0] + " Y: " + group[1] + " W: " + group[2] + " H: " + group[3]);
    }
}
