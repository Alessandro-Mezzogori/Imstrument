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

//        wavetables = new float[testLengt.h][];
//        for(int i = 0; i < testLength; i++)
//            wavetables[i] = new float[WAVETABLE_SIZE];
//
//        for (int i = 0; i < WAVETABLE_SIZE; i++) {
//            double time = ((double) i / SAMPLE_RATE);
//            wavetables[0][i] = (float) Math.sin(2 * Math.PI * fundamentalFrequency * time);
//            wavetables[1][i] = Math.signum(wavetables[0][i]);
//            wavetables[2][i] = (float)(2*(time*fundamentalFrequency - Math.floor(0.5 + time*fundamentalFrequency)));
//        }


        //TODO switch type
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

    /* import / export to file */


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
        ArrayList<byte[]> byteArray = new ArrayList<>();
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(fileIn);
            int bytesPerFrame = audioInputStream.getFormat().getFrameSize();
            // Set an arbitrary buffer size of 1024 frames.
            System.out.println("Bytes per frame: " + bytesPerFrame);
            System.out.println("Bits in sample:" + audioInputStream.getFormat().getSampleSizeInBits());
            System.out.println("Channels number: " + audioInputStream.getFormat().getChannels());
            System.out.println("Big Endian: " + audioInputStream.getFormat().isBigEndian());

            int numBytes = 2048 * bytesPerFrame;
            ArrayList<byte[]> test = new ArrayList<>();
            byte[] audioBytes = new byte[numBytes];
//            byte[] header = new byte[44];
            try {
//                audioInputStream.read(header);
                // Try to read numBytes bytes from the file.
                while (audioInputStream.read(audioBytes) != -1) {
                    // Calculate the number of frames actually read.
                    test.add(Arrays.copyOf(audioBytes, audioBytes.length));
                }
            } catch (Exception ex) {
                // Handle the error...
            }
            System.out.println(test.size());
            wavetables = new float[test.size()][];
            for(int i = 0; i < wavetables.length; i++)
                wavetables[i] = DatatypeConversion.ByteArray2FloatArray(test.get(i), audioInputStream.getFormat().getSampleSizeInBits()/8);
//
        } catch (Exception e) {
            // Handle the error...
        }
//            /* Generates the file that contains the audio*/
//            File fileAudio = new File(this.getClass().getResource("/imstrument/wavetables/14-SinFormant.wav").getPath());
//            try {
//                /* Creating the audioinputstream for working with the bytes of the file*/
//                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(fileAudio);
//                int bytesPerFrame = audioInputStream.getFormat().getFrameSize();
//                ArrayList<byte[]> byteArray = new ArrayList<>();
//
//                /* Creating a buffer header because the wav file has 4 bytes as descriptions */
//                byte[] bufferHeader = new byte[4];
//                audioInputStream.read(bufferHeader);
//                while (true) {
//                    byte[] inputBuffer = new byte[Wavetable.WAVETABLE_SIZE * 3];
//                    if (audioInputStream.read(inputBuffer, 0, inputBuffer.length) == -1)
//                        break;
//
//                    byteArray.add(inputBuffer);
//                }
//                wavetables = new float[byteArray.size()][];
//                for (int i = 0; i < wavetables.length; i++)
//                    wavetables[i] = DatatypeConversion.ByteArray2FloatArray(byteArray.get(i));
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//        }
//        catch (Exception ex)
//        {
//            ex.printStackTrace();
//        }
    }
}