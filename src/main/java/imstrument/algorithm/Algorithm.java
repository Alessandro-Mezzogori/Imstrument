package imstrument.algorithm;

import java.awt.*;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class Algorithm {
    private static ArrayList<Operator> operatorMatrix;
    private static Color[][] pixels;
    private static ArrayList<int[]> groups;

    private static final int groupElementSize = 4;
    private static final Pattern pattern = Pattern.compile("_<([\\d+,]+)><(\\w+)>_");

    public static void compute(){

    }

    public static void decode(String algorithm){
        groups.clear();
        operatorMatrix.clear();


    }
}
