package imstrument.sound.wavetables;

import imstrument.sound.openal.AudioThread;
import imstrument.sound.utils.DatatypeConversion;
import imstrument.start.StartApp;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.FileInputStream;
import java.util.Arrays;

public class Wavetable {
    /**
     * content of the wavetable
     */
    private float[][] wavetables;
    /**
     * Starting wavetable index
     */
    private int wavetableIndex;

    /**
     * size of a single wave in the wavetabel
     */
    public static int WAVETABLE_SIZE = 2048;

    /**
     * sample rate of the wavetable
     */
    public static int SAMPLE_RATE = 44100;

    /**
     * frequency of the wave if it would be played with an step increment of 1
     */
    public static double FUNDAMENTAL_FREQUENCY = ((double) WAVETABLE_SIZE) / Wavetable.SAMPLE_RATE;

    /* wavetable storing */
    /**
     * wavetable storing folder
     */
    public static final File WAVETABLE_FOLDER = new File(StartApp.defaultFolder.toString() + "/wavetables/"); // folder of the saved algorithms
    /**
     * wavetable extension
     */
    public static final String fileExtension = "wav";


    /* static block for folder creation */
    static{
        if(!WAVETABLE_FOLDER.exists()){
            boolean mkdir = WAVETABLE_FOLDER.mkdir();
        }
    }

    public Wavetable(int wavetableIndex, String filename) {
        this.wavetableIndex = wavetableIndex;

        readFromFile(filename); //TODO default get currently selected
    }

    public Wavetable(String filename){
        this(0, filename);
    }


    public float[] getSamples(int wavetableIndex) {
        return wavetables[wavetableIndex];
    }

    public int getWavetableNumber() {
        return wavetables.length;
    }

    public int getWavetableIndex() {
        return wavetableIndex;
    }

    public void setWavetableIndex(int index) {
        if(index >= this.wavetables.length) throw new IndexOutOfBoundsException("Trying to select an non existing soundwave");

        this.wavetableIndex = index;
    }

    /* static helper functions */
    public static double getStepSize(double frequency) {
        return frequency * FUNDAMENTAL_FREQUENCY;
    }

    public void readFromFile(String filename) {
        //todo generalize
        File fileIn = new File(WAVETABLE_FOLDER.toString() + "/" + filename + "." + fileExtension);
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(fileIn);
            int bytesPerFrame = audioInputStream.getFormat().getFrameSize();
            // Set an arbitrary buffer size of 1024 frames.

            int numBytes = 2048 * bytesPerFrame;
            ArrayList<byte[]> test = new ArrayList<>();
            byte[] audioBytes = new byte[numBytes];
            try {
                // Try to read numBytes bytes from the file.
                while (audioInputStream.read(audioBytes) != -1) {
                    // Calculate the number of frames actually read.
                    test.add(Arrays.copyOf(audioBytes, audioBytes.length));
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            wavetables = new float[test.size()][];
            for(int i = 0; i < wavetables.length; i++)
                wavetables[i] = DatatypeConversion.ByteArray2FloatArray(test.get(i), audioInputStream.getFormat().getSampleSizeInBits()/8);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}