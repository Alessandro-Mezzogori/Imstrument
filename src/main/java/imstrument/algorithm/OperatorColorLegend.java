package imstrument.algorithm;

import imstrument.algorithm.operators.Operator;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * Legend to show which operator corrisponds to which color
 */
public class OperatorColorLegend extends JComponent {
    /**
     * numbers of items in a legend's line
     */
    final int  itemsInLine = 4;
    /* distances */
    final int spacingFromColor = 20;
    final int colorSquareSize = 10;
    final int verticalSpacing = 20;
    final int leftMargin = 40;
    final int topMargin = 20;

    public OperatorColorLegend(){
        Dimension size = new Dimension(100 + 2*leftMargin, topMargin + Algorithm.operatorTable.size()*(verticalSpacing + colorSquareSize));
        setPreferredSize(size);
        setMaximumSize(size);
        setMinimumSize(size);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int x = leftMargin, y = topMargin;
        // for each operator show the color and write the name
        for(Operator operator : Algorithm.operatorTable.values()){
            // get the operator color
            g.setColor(operator.getColor());
            // draw a small rectangle beside the name with the operator associated color
            g.fillRect(x, y - colorSquareSize, colorSquareSize, colorSquareSize);

            // write the operator name beside the rectangle
            g.setColor(Color.black);
            g.drawString(operator.getName(), x + spacingFromColor, y);

            // go to the next
            y += verticalSpacing;
        }
    }
}
