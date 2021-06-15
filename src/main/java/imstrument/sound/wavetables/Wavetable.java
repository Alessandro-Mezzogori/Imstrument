package imstrument.sound.wavetables;

import imstrument.sound.utils.DatatypeConversion;

import java.io.*;
import java.util.ArrayList;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.FileInputStream;

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

//        wavetables = new float[testLength][];
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
            FileOutputStream fileOutputStream = new FileOutputStream(this.getClass().getResource("/imstrument/wavetables/simple").getPath());

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
        try {
            /* Generates the file that contains the audio*/
            File fileAudio = new File(this.getClass().getResource("/imstrument/wavetables/Classic.wav").getPath());

            try {

                /* Creating the audioinputstream for working with the bytes of the file*/
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(fileAudio);
                int bytesPerFrame = audioInputStream.getFormat().getFrameSize();
                ArrayList<byte[]> byteArray = new ArrayList<>();

                /* Creating a buffer header because the wav file has 4 bytes as descriptions */
                byte[] bufferHeader = new byte[4];
                audioInputStream.read(bufferHeader);
                while (true) {
                    byte[] inputBuffer = new byte[Wavetable.WAVETABLE_SIZE * 4];
                    if (audioInputStream.read(inputBuffer, 0, inputBuffer.length) == -1)
                        break;

                    byteArray.add(inputBuffer);
                }
                wavetables = new float[byteArray.size()][];
                for (int i = 0; i < wavetables.length; i++)
                    wavetables[i] = DatatypeConversion.ByteArray2FloatArray(byteArray.get(i));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}