package imstrument.algorithm;

import imstrument.algorithm.operators.*;
import imstrument.start.StartApp;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Algorithm {
    private final ArrayList<AlgorithmUnit> groups;
    private String currentName;

    private static final int groupElementSize = 4;
    private static final Pattern pattern = Pattern.compile("_<(-?[\\d]+),(-?[\\d]+),(-?[\\d]+),(-?[\\d]+)><(.*?)>_");
    protected static final TreeMap<String, Operator> operatorTable;

    public static final String fileExtension = "imalg";
    public static final File ALGORITHM_FOLDER = new File(StartApp.defaultFolder.toString() + "/algorithm/");

    static
    {
        operatorTable = new TreeMap<>();

        Operator[] operators = {new AvgLuminance(), new Red(), new DummyOperator()};
        for(Operator operator : operators)
            operatorTable.put(operator.getName(), operator);

        if(!ALGORITHM_FOLDER.exists()){
            boolean mkdir = ALGORITHM_FOLDER.mkdir(); // TODO notify user if default folder is not created
        }
    }

    public Algorithm(){
        groups = new ArrayList<>();
        currentName = "";
    }

    public void compute(BufferedImage image, Point pressed){
        System.out.println(image.getWidth() + " " + image.getHeight());

        double[] values = new double[groups.size()];
        for(int i = 0, groupSize = groups.size(); i < groupSize; i++){
            AlgorithmUnit unit = groups.get(i);
            int[] rect = unit.rect;

            // bound checking
            int[] pixels = image.getRGB(pressed.x + rect[0], pressed.y + rect[1], rect[2], rect[3], null, 0, rect[2]);
            Color[] colors = new Color[pixels.length];

            for(int j = 0, pixelsLen = pixels.length; j < pixelsLen; j++)
                colors[j] = new Color(pixels[j]);
            values[i] = unit.operator.compute(colors);
        }

        // TODO REMOVE DEBUG PRINT
        System.out.println("VALUES: ");
        for(double value : values)
            System.out.println(value);
    }

    public void decode(String name, String algorithm){
        //TODO error messages
        this.currentName = name;
        groups.clear();
        //operatorMatrix.clear();
        Matcher matcher = pattern.matcher(algorithm);

        /* as long as there are matches read them */
        while(matcher.find()) {
            int[] group = new int[groupElementSize];
            for(int i = 1, groupCount = matcher.groupCount(); i < groupCount; i++) {
                group[i - 1] = Integer.parseInt(matcher.group(i));
            }

            groups.add(new AlgorithmUnit(group, operatorTable.get(matcher.group(matcher.groupCount()))));
        }
    }

    public ArrayList<AlgorithmUnit> getGroups() {
        return groups;
    }
    public String getCurrentName(){ return currentName;}

    public static class Standard{
        /* add standard algorithms*/
    }
}
