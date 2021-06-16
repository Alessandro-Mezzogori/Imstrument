package imstrument.algorithm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class AlgorithmCanvas extends JComponent implements MouseListener, MouseMotionListener
{
    /* canvas params */
    Dimension preferredDimension;

    /* drawing params */
    ArrayList<int[]> groups;  // rectangles
    int[] currentGroup;
    boolean drawingRectangle;

    Point centerPoint;
    Point rightClick;
    JPopupMenu rightClickOptions;
    JMenuItem delete;

    public AlgorithmCanvas(){
        /* set dimensions */
        preferredDimension = new Dimension(300, 300);
        setMinimumSize(preferredDimension);
        setPreferredSize(preferredDimension);
        setMaximumSize(preferredDimension);


        /* initialize values */
        groups = new ArrayList<>();
        currentGroup = new int[4];
        drawingRectangle = false;
        centerPoint = new Point();

        /* create popup menu on right click*/
        rightClickOptions = new JPopupMenu();
        delete = new JMenuItem("Delete");
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int i; // index of the latest created rectangle that contains the point where it was rightClicked
                /* find if it exists a rectangle that contains the right click point */
                for(i = groups.size() - 1; i >= 0; i--){
                    int[] group = groups.get(i);
                    if( rightClick.x >= group[0] && rightClick.y >= group[1]  && rightClick.x <= group[0] + group[3] && rightClick.y <= group[1] + group[3]){
                        break;
                    }
                }
                /* remove the rectangle if found */
                if(i >= 0) {
                    groups.remove(i);
                    AlgorithmCanvas.this.repaint();
                }
            }
        });
        rightClickOptions.add(delete);

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
        for(int[] group : groups){
            g.fillRect(group[0],group[1],group[2],group[3]);
        }

        /* draw current drawing group */
        if(drawingRectangle) {
            /* have the starting drawing corner to be the top-left corner just for drawing */
            g.fillRect(
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
            drawingRectangle = false;
            rightClick = e.getPoint();

            rightClickOptions.show(e.getComponent(), rightClick.x, rightClick.y);
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
            currentGroup[0] = (currentGroup[2] > 0) ? currentGroup[0] : currentGroup[0] + currentGroup[2];
            currentGroup[1] = (currentGroup[3] > 0) ? currentGroup[1] : currentGroup[1] + currentGroup[3];
            currentGroup[2] = Math.abs(currentGroup[2]);
            currentGroup[3] = Math.abs(currentGroup[3]);

            /* add to the group and repaint */
            groups.add(Arrays.copyOf(currentGroup, currentGroup.length));
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

    /**
     *
     * @return
     */
    public ArrayList<int[]> saveGroups(){
        /* save the references to the int arrays then clear the groups */
        ArrayList<int[]> clone = new ArrayList<>(groups.size());
        for(int[])

        return clone;
    }

}
