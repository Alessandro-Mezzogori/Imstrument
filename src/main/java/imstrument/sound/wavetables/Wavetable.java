package imstrument.sound.wavetables;

import imstrument.sound.utils.DatatypeConversion;
import imstrument.start.StartApp;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
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
     * wavetable file extension
     */
    public static final String fileExtension = "wav";

    public static final String DEFAULT_WAVETABLE = "14-SinFormant";

    /* static block for folder creation */
    static{
        // create the folder where all the wavetables are store
        if(!WAVETABLE_FOLDER.exists()){
            boolean mkdir = WAVETABLE_FOLDER.mkdir();
        }
        // if not present copy the default wavetable
        File defaultWavetable = new File(WAVETABLE_FOLDER + "/" + DEFAULT_WAVETABLE + "." + fileExtension);
        if( !defaultWavetable.exists() ){
            File defaultWavetableResource = new File(Wavetable.class.getResource("/imstrument/wavetables/" + DEFAULT_WAVETABLE + "." + fileExtension).getPath());
            try {
                Files.copy(
                    defaultWavetableResource.toPath(),
                    defaultWavetable.toPath(),
                    StandardCopyOption.REPLACE_EXISTING
                );
            } catch (IOException ioException) {
                JOptionPane.showMessageDialog(null, "An error has occured in the setup of the wavetables, please restart Imstrument");
                System.exit(1);
            }
        }
    }

    public Wavetable(int wavetableIndex, String filename) {
        this.wavetableIndex = wavetableIndex;

        readFromFile(filename);
    }

    public Wavetable(String filename){
        this(0, filename);
    }

    /**
     * returns the array of samples of the wave with wavetable index
     * @param wavetableIndex index of the wave
     * @return the array of samples
     */
    public float[] getSamples(int wavetableIndex) {
        return wavetables[wavetableIndex];
    }

    /**
     * returns the number of waves in the wavetable
     * @return number of waves in the wavetable
     */
    public int getWavetableNumber() {
        return wavetables.length;
    }

    /**
     *
     * @return the current wavetable idnex
     */
    public int getWavetableIndex() {
        return wavetableIndex;
    }

    /**
     * sets the wavetable index to the given paramter
     * will throw out of bound exception if it's outside the lenght of the wavetable
     * @param index to be setted to
     */
    public void setWavetableIndex(int index) {
        if(index >= this.wavetables.length) throw new IndexOutOfBoundsException("Trying to select an non existing soundwave");

        this.wavetableIndex = index;
    }

    /* static helper functions */

    /**
     * returns the step size given a frequency
     * @param frequency to be converted to step size
     * @return the step size from the given frequency
     */
    public static double getStepSize(double frequency) {
        return frequency * FUNDAMENTAL_FREQUENCY;
    }

    /**
     * loads a wavetable from file
     * @param filename wavetable simple filename
     */
    public void readFromFile(String filename) {
        File fileIn = new File(WAVETABLE_FOLDER.toString() + "/" + filename + "." + fileExtension);
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(fileIn);
            int bytesPerFrame = audioInputStream.getFormat().getFrameSize();
            // Set an arbitrary buffer size of 1024 frames.

            int numBytes = 2048 * bytesPerFrame;
            ArrayList<byte[]> test = new ArrayList<>();
            byte[] audioBytes = new byte[numBytes];

            // Try to read numBytes bytes from the file.
            while (audioInputStream.read(audioBytes) != -1) {
                // Calculate the number of frames actually read.
                test.add(Arrays.copyOf(audioBytes, audioBytes.length));
            }

            wavetables = new float[test.size()][];
            for(int i = 0; i < wavetables.length; i++)
                wavetables[i] = DatatypeConversion.ByteArray2FloatArray(test.get(i), audioInputStream.getFormat().getSampleSizeInBits()/8);
        } catch (IOException | UnsupportedAudioFileException e) {
            // something went wrong in the reading process
        }
        // everything went ok
    }
}