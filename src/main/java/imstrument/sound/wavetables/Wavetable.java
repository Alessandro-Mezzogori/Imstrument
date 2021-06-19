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
    private Type type;
    private int wavetableIndex;

    public static int WAVETABLE_SIZE = 2048;
    public static int SAMPLE_RATE = 48000;
    public static double FUNDAMENTAL_FREQUENCY = ((double) WAVETABLE_SIZE) / SAMPLE_RATE;
    private static final double fundamentalFrequency = ((double) SAMPLE_RATE / WAVETABLE_SIZE);

    public Wavetable(Type type, int wavetableIndex) {
        this.type = type;
        this.wavetableIndex = wavetableIndex;

        final int testLength = 3;

        readFromFile();

    }

    public Wavetable(float[] wavetable) {
        this.wavetables = new float[][]{wavetable};
        this.type = Type.CUSTOM;
        this.wavetableIndex = 0;
    }

    public float[] getSamples(int wavetableIndex) {
        return wavetables[wavetableIndex];
    }

    public float[] getSamples() {
        return wavetables[this.wavetableIndex];
    }

    public int getWavetableNumber() {
        return wavetables.length;
    }

    public int getWavetableIndex() {
        return wavetableIndex;
    }

    /* static helper functions */
    public static double getStepSize(double frequency) {
        return frequency * FUNDAMENTAL_FREQUENCY;
    }

    public enum Type {
        SIMPLE, //SINE, SAW, SQUARE, TRIANGLE
        CUSTOM, //WAVETABLE CREATED FROM OTHER WAVETABLES
    }

    /* import export to file */
    public void writeToFile() {
        //todo generalize
        try {
            //TODO se file non è trovato crearlo
            FileOutputStream fileOutputStream = new FileOutputStream(this.getClass().getResource("/imstrument/wavetables/14-SinFormant.wav").getPath());

            int offset = 0;
            for (int i = 0; i < wavetables.length; i++) {
                byte[] byteArray = DatatypeConversion.FloatArray2ByteArray(wavetables[i]);
                fileOutputStream.write(byteArray, 0, byteArray.length);
                fileOutputStream.flush();
                offset += byteArray.length;
            }
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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