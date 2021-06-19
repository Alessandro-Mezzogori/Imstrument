package imstrument.algorithm;

import imstrument.algorithm.operators.Operator;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class OperatorColorLegend extends JComponent {
    /* number of items in a legend line */
    final int  itemsInLine = 4;
    /* distances */
    final int spacingFromColor = 20;
    final int spacingFromWord = 20;
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
        for(Operator operator : Algorithm.operatorTable.values()){
            g.setColor(operator.getColor());
            g.fillRect(x, y - colorSquareSize, colorSquareSize, colorSquareSize);

            g.setColor(Color.black);
            g.drawString(operator.getName(), x + spacingFromColor, y);

            y += verticalSpacing;
        }
    }
}
