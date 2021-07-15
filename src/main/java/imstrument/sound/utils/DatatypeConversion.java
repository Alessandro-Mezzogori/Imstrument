package imstrument.sound.utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class DatatypeConversion {
    /**
     * Converts an array of shorts to a byte array
     * @param values short array to be converted
     * @return the byte array converted from the shorts array
     */
    public static byte[] ShortArray2ByteArray(short[] values) {
        byte[] buffer = new byte[values.length * Short.BYTES];
        for (int i = 0; i < values.length; i++) {
            buffer[i * 2] = (byte) values[i];
            buffer[i * 2 + 1] = (byte) (values[i] >> 8);
        }

        return buffer;
    }

    /**
     * converts an array of bytes to a float array
     * @param values byte array
     * @param sampleLen lenght in byte of a single sample
     * @return the float array converted from the byte array
     */
    public static float[] ByteArray2FloatArray(byte[] values, int sampleLen){
        // create the float return buffer
        float[] floatBuffer = new float[values.length/sampleLen];
        // create the current byte buffer being processed
        byte[] buffer = new byte[4];
        // at which index will we start write the values
        int startingIndex = 4 - sampleLen;
        for(int i = 0; i < floatBuffer.length; i++){
            try {
                // copy the bytes from the values array to the buffer array
                System.arraycopy(values, i * buffer.length, buffer, startingIndex, buffer.length - startingIndex);
            }catch (Exception e){ e.printStackTrace();}

            /* pad left with zero's if the lenght of the buffers is not 4 bytes */
            for(int j = 0; j < startingIndex; j++){
                buffer[j] = 0;
            }

            // wrap with a bytebuffer to set the endiannes and convert to float
            ByteBuffer byteBuffer = ByteBuffer.wrap(buffer);
            byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
            floatBuffer[i] = byteBuffer.getFloat();
        }
        return floatBuffer;
    }
}
