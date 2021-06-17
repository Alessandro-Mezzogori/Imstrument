package imstrument.algorithm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class AlgorithmCanvas extends JComponent implements MouseListener, MouseMotionListener
{
    /* canvas params */
    Dimension preferredDimension;

    /* drawing params */
    ArrayList<AlgorithmUnit> groups;
    int[] currentGroup;
    boolean drawingRectangle;

    Point centerPoint;
    Point rightClick;
    JPopupMenu rightClickOptions;

    CustomAlgorithmCreator.AlgorithmCreationSynchronizer controller;

    int currentlySelected; // index of the latest created rectangle that contains the point where it was rightClicked

    /**
     * creates a canvas to draw/add algo units to the passed arraylist
     * @param groups
     */
    public AlgorithmCanvas(ArrayList<AlgorithmUnit> groups, CustomAlgorithmCreator.AlgorithmCreationSynchronizer controller){
        this.controller = controller;
        /* set dimensions */
        preferredDimension = new Dimension(300, 300);
        setMinimumSize(preferredDimension);
        setPreferredSize(preferredDimension);
        setMaximumSize(preferredDimension);

        /* initialize values */
        this.groups = groups;
        currentGroup = new int[AlgorithmUnit.RECT_SIZE];
        drawingRectangle = false;
        centerPoint = new Point();

        /* create popup menu on right click*/
        rightClickOptions = new JPopupMenu();
//        JMenuItem setGroup = new JMenuItem("Operators");
//        setGroup.addActionListener(e -> {
//            groups.remove(currentlySelected);
//            AlgorithmCanvas.this.repaint();
//        });
//        rightClickOptions.add(setGroup);

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
        setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.RED));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Dimension currentSize = getSize();
        centerPoint.x = currentSize.width/2;
        centerPoint.y = currentSize.height/2;

        /* draw square to show where the center is */
        g.setColor(Color.red);
        g.fillRect(centerPoint.x - 2, centerPoint.y - 2, 5, 5);

        /* draw saved groups */
        g.setColor(Color.cyan);
        for(AlgorithmUnit group : groups){
            g.drawRect(group.rect[0],group.rect[1],group.rect[2],group.rect[3]);
        }

        /* draw current drawing group */
        if(drawingRectangle) {
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
        if(SwingUtilities.isRightMouseButton(e)) {
            int x = e.getX(), y = e.getY();
            drawingRectangle = false;
            for(currentlySelected = groups.size() - 1; currentlySelected >= 0; currentlySelected--) {
                int[] rect = groups.get(currentlySelected).rect;
                if (x >= rect[0] && y >= rect[1]  && x <= rect[0] + rect[2]&& y <= rect[1] + rect[3]) {
                    break;
                }
            }

            if( currentlySelected >= 0 ) {
                rightClickOptions.show(e.getComponent(), x, y);
            }
        }
        else if(SwingUtilities.isLeftMouseButton(e)){
            drawingRectangle = true;
            currentGroup[0] = e.getX();
            currentGroup[1] = e.getY();
            repaint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(SwingUtilities.isLeftMouseButton(e) && drawingRectangle && currentGroup[2] != 0 && currentGroup[3] != 0) {
            /* have the starting drawing corner to the top-left */
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
