package imstrument.algorithm;

import imstrument.algorithm.operators.Transparency;
import imstrument.algorithm.operators.*;
import imstrument.sound.waves.Soundwave;
import imstrument.sound.waves.Soundwaves;
import imstrument.start.StartApp;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Algorithm {
    /* algorithm computing attributes */
    private final ArrayList<AlgorithmUnit> units; // contains the decoded algorithm
    private String currentName; // name of the algorithm
    private boolean allUnitsDeactivated; //  tells if all the algorithm units are outside the displayed image

    /* decoding attributes */
    private static final Pattern pattern = Pattern.compile("_<(-?[\\d]+),(-?[\\d]+),(-?[\\d]+),(-?[\\d]+)><(.*?)>_"); // REGEX pattern for one algounit
    protected static final TreeMap<String, Operator> operatorTable; // TreeMap used for quick conversion between the operator name and the equivalent Operator Object

    /* algorithm storing attributes */
    public static final String fileExtension = "imalg"; // file extension of an Imstrument algorithm
    public static final File ALGORITHM_FOLDER = new File(StartApp.defaultFolder.toString() + "/algorithm/"); // folder of the saved algorithms
    public static final String EMPTY_ALGORITHM_NAME = "";

    /* assign to soundwave params */
    private static final double MAX_FREQUENCY = 2000.0; // max possible frequency permitted
    private static final double[] FREQUENCY_MULTIPLIERS = new double[]{0.25, 0.5, 1.0, 2.0, 3.0, 4.0, 5.0, 10.0, 15.0, 18.0}; // frequency multipliers of the modulating soundwaves
    private static final double MAX_MODULATING_INDEX = 3000.0; // max modulating index of the modulating soundwaves

    /*
        static block to initialize the Operators table and
        generating the algorithm storing folder if it wasn't done before
     */
    static
    {
        operatorTable = new TreeMap<>(); // initialize the operator conversion map

        Operator[] operators = {
                new Luminance(), new Blackness(), new Opacity(), new Transparency(),
                new Red(), new Green(), new Blue(),
                new NotRed(), new NotGreen(), new NotBlue()
        }; // list of all possible operators

        // populate the operator conversion map
        for(Operator operator : operators)
            operatorTable.put(operator.getName(), operator);

        // create algorithm storing folder if it doesn't exists
        if(!ALGORITHM_FOLDER.exists()){
            boolean mkdir = ALGORITHM_FOLDER.mkdir();
        }
    }

    public Algorithm(){
        units = new ArrayList<>();
        currentName = EMPTY_ALGORITHM_NAME;
    }

    /**
     * computes a double value between [0.0, 1.0] from a given algorithm unit
     * @param image image on which the values are computed
     * @param pressed the point that was clicked on the image
     * @return array of the computed values
     */
    public double[] compute(BufferedImage image, Point pressed){
        // Create a double for each unit in the algorithm
        double[] values = new double[units.size()];
        allUnitsDeactivated = true;

        // for each unit extract the values inside the associated rectangle and compute a value
        for(int i = 0, groupSize = units.size(); i < groupSize; i++){
            // retrieve the current unit so there's no function overhead
            AlgorithmUnit unit = units.get(i);
            // alias the rectangle unit for better readability
            int[] rect = unit.rect;

            // if the unit is active ( is inside the currently shown image ) compute the corresponding value
            if(unit.isActive()) {
                allUnitsDeactivated = false;
                // get the pixels of the specified rectangle
                int[] pixels = image.getRGB(pressed.x + rect[0], pressed.y + rect[1], rect[2], rect[3], null, 0, rect[2]);
                // conversion from byte hex to a easier class to use
                Color[] colors = new Color[pixels.length];

                for (int j = 0, pixelsLen = pixels.length; j < pixelsLen; j++)
                    colors[j] = new Color(pixels[j]);
                // compute the value following the operator specification
                values[i] = unit.operator.compute(colors);
            }
        }

        // if there's no unit inside the image return null, else return the compute values
        // this is needed because it would return an array without any significance
        return allUnitsDeactivated ? null : values;
    }

    /**
     * Tweaks and assigns the values computed from the current active algorithm to the given soundwave
     * @param soundwave to be modified by the computed values
     * @param unitValues computed values
     */
    public void assignValues(Soundwave soundwave, double[] unitValues){
        /* compute the sizes of each algorithm unit */
        int[] sizes = new int[units.size()]; // array storing all the area of the unit's rectangles
        int total = 0; // total area / amount of pixels inside the unit's rectangles
        for(int i = 0, unitsSize = units.size(); i < unitsSize; i++){
            // if the units is inactive its area is 0
            sizes[i] = units.get(i).isActive() ? units.get(i).getPixelNumber() : 0;
            // add the area of the current unit to the total area
            total += sizes[i];
        }

        /* get the number of Soundwaves that have to be assigned */
        int soundwaveNumber = Soundwaves.getModulatingSoundwaveNumber(soundwave);
        /* compute how many pixel are used per value aka (10 + 11*modulating soundwave */
        int numberOfValues = Soundwave.PARAM_NUMBER_WITHOUT_MODULATOR + Soundwave.PARAM_NUMBER_WITH_MODULATOR * soundwaveNumber;
        int pixelsPerValue = total >= numberOfValues ? total/numberOfValues : 1;

        /* assign the unitValues via linear interpolation if between two units */
        int currentUnit = 0;
        /* get the first active unit */
        while(sizes[currentUnit] == 0) currentUnit = (currentUnit + 1) % sizes.length;

        int currentPixel = 0; // current pixel in the computation
        // Create the soundwaves that will be used to generate the new one from the values that were computed
        Soundwave carrier = null, prevSoundwave = null, currentSoundwave;
        // create a carrier soundwave modulated by soundwaveNumber waves
        for(int currentSoundwaveNumber = 0; currentSoundwaveNumber < soundwaveNumber + 1; currentSoundwaveNumber++){
            // build the soundwave from the outside in  -> carrier to inner most modulator
            // the soundwaves that have a modulator need an additional parameter ( modulation index )
            double[] currentValues = new double[ currentSoundwaveNumber < soundwaveNumber ? Soundwave.PARAM_NUMBER_WITH_MODULATOR : Soundwave.PARAM_NUMBER_WITHOUT_MODULATOR];

            /* compute the require unitValues */
            for(int i = 0; i < currentValues.length; i++) {
                // compute the remaining pixels in the current unit
                int remainingPixelInUnit = sizes[currentUnit] - currentPixel;
                /* compute the weight associated with the value of the currentUnit */
                double weight = Math.min(1.0, Math.max( 0.0, ((double) remainingPixelInUnit) / pixelsPerValue));

                /* computer the value with the current unit */
                currentValues[i] = unitValues[currentUnit] * weight;

                /* if it's not fully contained in the currentUnit add the second part of the weighted sum */
                if (pixelsPerValue > remainingPixelInUnit) {
                    /* get the next active unit */
                    while(sizes[currentUnit] == 0) currentUnit = (currentUnit + 1) % sizes.length;

                    /* no need to check if currentUnit + 1 is in range because in the last group it's fully contained */
                    currentValues[i] += (1.0 - weight) * unitValues[currentUnit];

                    /* get next unit if there are no more unit wrap around */
                    currentPixel = pixelsPerValue - remainingPixelInUnit;
                } else {
                    // continue to go trought the current unit's pixels
                    currentPixel += pixelsPerValue;
                }
            }

            /* ####### TWEAKING ####### */
            // to have nicer soundwaves
            if( prevSoundwave != null ) {
                /* frequency tweaking if it's a modulator */
                currentValues[1] = carrier.getFrequency()*FREQUENCY_MULTIPLIERS[(int) (FREQUENCY_MULTIPLIERS.length*currentValues[1])];
            }
            else{
                currentValues[1] = currentValues[1]*MAX_FREQUENCY;
            }

            /* if it's a modulator tweak the index */
            if(currentValues.length == Soundwave.PARAM_NUMBER_WITH_MODULATOR){
                currentValues[18] = MAX_MODULATING_INDEX*currentValues[18];
            }

            /* soundwave creation */
            currentSoundwave = new Soundwave(currentValues);
            if( prevSoundwave != null ) {
                /* if it's not the inner most wave */
                prevSoundwave.setModulatingWave(currentSoundwave);
            }else{
                /* the first created soundwave is also the carrier (used for tweaking unitValues to have a better sound ) */
                carrier = currentSoundwave;
            }
            prevSoundwave = currentSoundwave;
        }

        // substitute the soundwave
        StartApp.waveManager.soundwaves.set(StartApp.waveManager.soundwaves.indexOf(soundwave), carrier);
    }

    /**
     * Wrapper for the functions Compute and assignValues
     * @param image image on which to process the request
     * @param pressed the point clicked by the mouse
     * @param soundwave the soundwave to assign the new values
     */
    public void computeAndAssign(BufferedImage image, Point pressed, Soundwave soundwave){
        // compute the values
        double[] values = compute(image, pressed);
        if(values != null){
            // assign the values
            assignValues(soundwave, values);
        }
    }

    /**
     * Decodes the given alogirthm in string form and associates with a name
     * @param name of the algorithm
     * @param algorithm descriptor string
     */
    public void decode(String name, String algorithm){
        // set the algorithm current name to the chosen algorithm name
        this.currentName = name;
        // clear the units in preparation of the decoding
        units.clear();
        // match all the instances of a alogrithm unit
        Matcher matcher = pattern.matcher(algorithm);

        /* as long as there are matches read them */
        while(matcher.find()) {
            // create an array of RECT_SIZE (4) that stores the description of the associated rectangle to the current unit
            int[] group = new int[AlgorithmUnit.RECT_SIZE];
            // parse the first matches to get the rectangle specfication
            for(int i = 1, groupCount = matcher.groupCount(); i < groupCount; i++) {
                group[i - 1] = Integer.parseInt(matcher.group(i));
            }

            // add the algorithm unit with the associated rectangle and operator given by the last match in the string
            units.add(new AlgorithmUnit(group, operatorTable.get(matcher.group(matcher.groupCount()))));
        }
    }

    /**
     * Getter for algorithm units
     * @return algorithm units array list
     */
    public ArrayList<AlgorithmUnit> getUnits() {
        return units;
    }

    /**
     * retrives the current algorithm's name
     * @return a string that contains the current algorithm name
     */
    public String getCurrentName(){ return currentName;}

    public boolean isAllUnitsDeactivated() {return allUnitsDeactivated; }
}
