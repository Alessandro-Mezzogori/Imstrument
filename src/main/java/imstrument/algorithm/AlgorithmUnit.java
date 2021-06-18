package imstrument.algorithm;

import imstrument.algorithm.operators.Operator;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class AlgorithmUnit {
    int[] rect;
    Operator operator; //TODO add multiple operators ??

    public static int RECT_SIZE = 4;

    public AlgorithmUnit(int[] rect, Operator operator){
        this.rect = new int[RECT_SIZE];
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
    }

    public int[] getRect() {
        return rect;
    }

    public Operator getOperator() {
        return operator;
    }

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
