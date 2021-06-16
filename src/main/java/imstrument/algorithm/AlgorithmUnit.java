package imstrument.algorithm;

import imstrument.algorithm.operators.Operator;

import java.util.ArrayList;
import java.util.Arrays;

public class AlgorithmUnit {
    int[] rect;
    ArrayList<Operator> operators;

    public static int RECT_SIZE = 4;

    public AlgorithmUnit(int[] rect, ArrayList<Operator> operators){
        this.rect = new int[RECT_SIZE];
        /* put the starting corner to the top-left*/
        this.rect[0] = (rect[2] > 0) ? rect[0] : rect[0] + rect[2];
        this.rect[1] = (rect[3] > 0) ? rect[1] : rect[1] + rect[3];
        this.rect[2] = Math.abs(rect[2]);
        this.rect[3] = Math.abs(rect[3]);

        this.operators = operators;
    }

    public AlgorithmUnit(int[] rect){
        this(rect, new ArrayList<>());
    }

    public AlgorithmUnit(){
        rect = new int[RECT_SIZE];
        operators = new ArrayList<>();
    }

//    void standardizeRectangle(){
//
//    }
}
