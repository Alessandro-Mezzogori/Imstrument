package imstrument.algorithm;

import imstrument.algorithm.operators.*;
import imstrument.algorithm.operators.Transparency;
import imstrument.sound.waves.Soundwave;
import imstrument.sound.waves.Soundwaves;
import imstrument.sound.waves.WaveManager;
import imstrument.start.StartApp;
import org.lwjgl.system.CallbackI;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Algorithm {
    private final ArrayList<AlgorithmUnit> units;
    private String currentName;

    private static final int groupElementSize = 4;
    private static final Pattern pattern = Pattern.compile("_<(-?[\\d]+),(-?[\\d]+),(-?[\\d]+),(-?[\\d]+)><(.*?)>_");
    protected static final TreeMap<String, Operator> operatorTable;

    public static final String fileExtension = "imalg";
    public static final File ALGORITHM_FOLDER = new File(StartApp.defaultFolder.toString() + "/algorithm/");

    static
    {
        operatorTable = new TreeMap<>();

        Operator[] operators = {
                new Luminance(), new Blackness(), new Opacity(), new Transparency(),
                new Red(), new Green(), new Blue(),
                new NotRed(), new NotGreen(), new NotBlue()
        };

        for(Operator operator : operators)
            operatorTable.put(operator.getName(), operator);

        if(!ALGORITHM_FOLDER.exists()){
            boolean mkdir = ALGORITHM_FOLDER.mkdir(); // TODO notify user if default folder is not created
        }
    }

    public Algorithm(){
        units = new ArrayList<>();
        currentName = "";
    }

    public double[] compute(BufferedImage image, Point pressed){

        double[] values = new double[units.size()];
        for(int i = 0, groupSize = units.size(); i < groupSize; i++){
            AlgorithmUnit unit = units.get(i);
            int[] rect = unit.rect;

            // bound checking
            int[] pixels = image.getRGB(pressed.x + rect[0], pressed.y + rect[1], rect[2], rect[3], null, 0, rect[2]);
            Color[] colors = new Color[pixels.length];

            for(int j = 0, pixelsLen = pixels.length; j < pixelsLen; j++)
                colors[j] = new Color(pixels[j]);
            values[i] = unit.operator.compute(colors);
        }

        return values;
    }

    public void assignValues(Soundwave soundwave, double[] values){
        /* compute the sizes of each algorithm unit */
        int[] sizes = new int[units.size()];
        int total = 0;
        for(int i = 0, unitsSize = units.size(); i < unitsSize; i++){
            sizes[i] = units.get(i).getPixelNumber();
            total += sizes[i];
        }

        /* get the number of Soundwaves that have to be assigned */
        int soundwaveNumber = Soundwaves.getModulatingSoundwaveNumber(soundwave);
        /* compute how many pixel are used per value aka (10 + 11*modulating soundwave */
        int numberOfValues = Soundwave.PARAM_NUMBER_WITHOUT_MODULATOR + Soundwave.PARAM_NUMBER_WITH_MODULATOR * soundwaveNumber;
        int pixelsPerValue = total >= numberOfValues ? total/numberOfValues : 1;

        /* TODO if pixelsPerValue is less than 0 -> cycle back trough */

        /* assign the values via linear interpolation if between two units */
        int currentUnit = 0;
        int currentPixel = 0;
        Soundwave prevSoundwave = null, currentSoundwave;
        for(int currentSoundwaveNumber = 0; currentSoundwaveNumber < soundwaveNumber + 1; currentSoundwaveNumber++){
            double[] currentValues = new double[ currentSoundwaveNumber != 0 ? Soundwave.PARAM_NUMBER_WITH_MODULATOR : Soundwave.PARAM_NUMBER_WITHOUT_MODULATOR];

            for(int i = 0; i < currentValues.length; i++) {
                int remainingPixelInUnit = sizes[currentUnit] - currentPixel;

                /* compute the weight associated with the value of the currentUnit */
                double weight = Math.min(1.0, ((double) remainingPixelInUnit) / pixelsPerValue);

                /* computer the value with the current unit */
                currentValues[i] = values[currentUnit] * weight;

                /* if it's not fully contained in the currentUnit add the second part of the weighted sum */
                if (pixelsPerValue > remainingPixelInUnit) {
                    /* no need to check if currentUnit + 1 is in range because in the last group it's fully contained */
                    currentValues[i] += (1.0 - weight) * values[(currentUnit + 1) % sizes.length];
                    /* assign value to the correct parameter */

                    /* get next unit if there are no more unit wrap around */
                    currentUnit = (currentUnit + 1) % sizes.length;
                    currentPixel = pixelsPerValue - remainingPixelInUnit;
                } else {
                    currentPixel += pixelsPerValue;
                }
            }

            currentSoundwave = new Soundwave(currentValues);
            if( currentSoundwaveNumber > 0) {
                /* if it's not the inner most wave */
                currentSoundwave.setModulatingWave(prevSoundwave);
            }
            prevSoundwave = currentSoundwave;
        }
        StartApp.waveManager.soundwaves.set(
                StartApp.waveManager.soundwaves.indexOf(soundwave), prevSoundwave
        );
    }

    public void decode(String name, String algorithm){
        //TODO error messages
        this.currentName = name;
        units.clear();
        //operatorMatrix.clear();
        Matcher matcher = pattern.matcher(algorithm);

        /* as long as there are matches read them */
        while(matcher.find()) {
            int[] group = new int[groupElementSize];
            for(int i = 1, groupCount = matcher.groupCount(); i < groupCount; i++) {
                group[i - 1] = Integer.parseInt(matcher.group(i));
            }

            units.add(new AlgorithmUnit(group, operatorTable.get(matcher.group(matcher.groupCount()))));
        }
    }

    public ArrayList<AlgorithmUnit> getUnits() {
        return units;
    }
    public String getCurrentName(){ return currentName;}

    public static class Standard{
        /* add standard algorithms*/
    }
}
