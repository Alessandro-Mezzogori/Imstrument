package imstrument.algorithm;

import imstrument.algorithm.operators.Operator;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class AlgorithmUnit {
    int[] rect; // associated rectangle of values
    Operator operator; // associated operator
    boolean active; // tells if it's inside or outside the current image
    public static int RECT_SIZE = 4; // values need to define a rectangle

    public AlgorithmUnit(int[] rect, Operator operator){
        this();
        /* put the starting corner to the top-left*/
        this.rect[0] = (rect[2] > 0) ? rect[0] : rect[0] + rect[2];
        this.rect[1] = (rect[3] > 0) ? rect[1] : rect[1] + rect[3];
        this.rect[2] = Math.abs(rect[2]);
        this.rect[3] = Math.abs(rect[3]);

        this.operator = operator;
    }

    public AlgorithmUnit(int[] rect){
        this(rect, null);
    }

    public AlgorithmUnit(){
        rect = new int[RECT_SIZE];
        operator = null;
        active = true;
    }

    public int[] getRect() {
        return rect;
    }

    /**
     * gets the unit's operator
     * @return the reference to the units operator
     */
    public Operator getOperator() {
        return operator;
    }

    /**
     * tells if the units should be counted or not in the compute and assign functions of the algorithm class
     * @return if the unit is active
     */
    public boolean isActive() { return active; }

    /**
     * set the active status of the unit ( used to disable the unit from the computation if outside the image )
     * @param active status of unit
     */
    public void setActive(boolean active) { this.active = active; }

    /**
     * computes the area of the rectangle described by the attribute "rect"
     * @return the area of the rectangle
     */
    public int getPixelNumber(){return this.rect[2]*this.rect[3];}

    /**
     * debug function
     * @return the string representation of the unit
     */
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("_<");
        for(int i = 0; i < RECT_SIZE; i++){
            stringBuilder.append(rect[i]);
            if( i != RECT_SIZE - 1) stringBuilder.append(",");
        }
        stringBuilder.append("><").append(operator.getName()).append(">_");
        return stringBuilder.toString();
    }

}
