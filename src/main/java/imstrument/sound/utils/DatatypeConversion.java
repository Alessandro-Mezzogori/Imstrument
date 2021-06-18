package imstrument.sound.utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class DatatypeConversion {
    public static byte[] FloatArray2ByteArray(float[] values){
        ByteBuffer buffer = ByteBuffer.allocate(4 * values.length);

        for (float value : values){
            buffer.putFloat(value);
        }

        return buffer.array();
    }

    /**
     * converts an array of bytes to a float array
     * @param values byte array
     * @param sampleLen lenght in byte of a single sample
     * @return
     */
    public static float[] ByteArray2FloatArray(byte[] values, int sampleLen){
        float[] floatBuffer = new float[values.length/sampleLen];
        byte[] buffer = new byte[4];
        int startingIndex = 4 - sampleLen;
        for(int i = 0; i < floatBuffer.length; i++){
            try {
                System.arraycopy(values, i * buffer.length, buffer, startingIndex, buffer.length - startingIndex);
            }catch (Exception e){ e.printStackTrace();}

            /* pad left with zero's */
            for(int j = 0; j < startingIndex; j++){
                buffer[j] = 0;
            }

            ByteBuffer byteBuffer = ByteBuffer.wrap(buffer);
            byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
            floatBuffer[i] = byteBuffer.getFloat();
        }
        return floatBuffer;
    }
}
