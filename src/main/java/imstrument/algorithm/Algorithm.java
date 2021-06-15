package imstrument.algorithm;

import imstrument.algorithm.operators.AvgLuminance;
import imstrument.algorithm.operators.Operator;
import imstrument.algorithm.operators.Red;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Algorithm {
    private  ArrayList<Operator> operatorMatrix; // TODO add multiple operator support
    private  ArrayList<int[]> groups;

    private static final int groupElementSize;
    private static final Pattern pattern;
    private static final Hashtable<String, Operator> operatorTable;

    static
    {
        groupElementSize = 4;
        pattern = Pattern.compile("_<(-?[\\d]+),(-?[\\d]+),(-?[\\d]+),(-?[\\d]+)><(\\w+)>_");
        operatorTable = new Hashtable<>();
        operatorTable.put("AVGLUMINANCE", new AvgLuminance());
        operatorTable.put("RED", new Red());
    }

    public Algorithm(String algorithm){
        this();
        decode(algorithm);
    }

    public Algorithm(){
        operatorMatrix = new ArrayList<>();
        groups = new ArrayList<>();
    }

    public void compute(BufferedImage image, Point pressed){
        System.out.println(image.getWidth() + " " + image.getHeight());

        double[] values = new double[groups.size()];
        for(int i = 0, groupsLen = groups.size(); i < groupsLen; i++){
            int[] group = groups.get(i);

            // bound checking
            int[] pixels = image.getRGB(pressed.x + group[0], pressed.y + group[1], group[2], group[3], null, 0, group[2]);
            Color[] colors = new Color[pixels.length];

            for(int j = 0, pixelsLen = pixels.length; j < pixelsLen; j++)
                colors[j] = new Color(pixels[j]);
            values[i] = operatorMatrix.get(i).compute(colors);
        }

        // TODO REMOVE DEBUG PRINT
        System.out.println("VALUES: ");
        for(double value : values)
            System.out.println(value);
    }

    public void decode(String algorithm){
        groups.clear();
        operatorMatrix.clear();
        Matcher matcher = pattern.matcher(algorithm);

        /* as long as there are matches read them */
        while(matcher.find()) {
            int[] group = new int[groupElementSize];
            for(int i = 1, groupCount = matcher.groupCount(); i < groupCount; i++) {
                group[i - 1] = Integer.parseInt(matcher.group(i));
            }

            groups.add(group);
            operatorMatrix.add(operatorTable.get(matcher.group(matcher.groupCount())));
        }

        for (int i = 0, groupsSize = groups.size(); i < groupsSize; i++) {
            int[] test = groups.get(i);
            System.out.println("FINISHED: ");
            System.out.println(String.valueOf(test[0]) + " " + test[1] + " " + test[2] + " " + test[3] + " " + operatorMatrix.get(i));
        }
    }

    public ArrayList<int[]> getGroups() {
        return groups;
    }
}
