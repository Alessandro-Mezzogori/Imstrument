package imstrument.sound.wavetables;

import imstrument.sound.utils.DatatypeConversion;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.FileInputStream;
import java.util.Arrays;

public class Wavetable {
    private float[][] wavetables;
    private int wavetableIndex;

    public static int WAVETABLE_SIZE = 2048;
    public static int SAMPLE_RATE = 48000;
    public static double FUNDAMENTAL_FREQUENCY = ((double) WAVETABLE_SIZE) / SAMPLE_RATE;

    public Wavetable(int wavetableIndex) {
        this.wavetableIndex = wavetableIndex;

        readFromFile();
    }

    public Wavetable(){
        this(0);
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

    public void readFromFile() {
        //todo generalize
        File fileIn = new File(this.getClass().getResource("/imstrument/wavetables/14-SinFormant.wav").getPath());
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
                // Handle the error...
            }

            wavetables = new float[test.size()][];
            for(int i = 0; i < wavetables.length; i++)
                wavetables[i] = DatatypeConversion.ByteArray2FloatArray(test.get(i), audioInputStream.getFormat().getSampleSizeInBits()/8);
        } catch (Exception e) {
            // Handle the error...
        }
    }
}