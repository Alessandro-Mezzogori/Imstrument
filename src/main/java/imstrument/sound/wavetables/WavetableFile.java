package imstrument.sound.wavetables;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class WavetableFile {
    public static byte[] FloatArray2ByteArray(float[] values){
        ByteBuffer buffer = ByteBuffer.allocate(4 * values.length);

        for (float value : values){
            buffer.putFloat(value);
        }

        return buffer.array();
    }

    public static void WriteToFile(Wavetable wavetable){
        try {
            //TODO se file non è trovato crearlo
            FileOutputStream fileOutputStream = new FileOutputStream(WavetableFile.class.getResource("/imstrument/wavetables/simple").getPath());

            System.out.println("Writing to " + WavetableFile.class.getResource("/imstrument/wavetables/simple").getPath());
            int offset = 0;
            for(int i = 0; i < wavetable.getWavetableNumber(); i++) {
                byte[] byteArray = FloatArray2ByteArray(wavetable.getSamples(i));
                fileOutputStream.write(byteArray, 0, byteArray.length);
                fileOutputStream.flush();
                offset += byteArray.length;
            }
            System.out.println("End of writing");
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void ReadFromFile(){
        try {
            FileInputStream fileInputStream = new FileInputStream(Wavetable.class.getResource("/imstrument/wavetables/simple").getPath());
            //TODO import from file

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
