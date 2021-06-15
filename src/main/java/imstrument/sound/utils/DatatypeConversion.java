package imstrument.sound.utils;

import java.nio.ByteBuffer;

public class DatatypeConversion {
    public static byte[] FloatArray2ByteArray(float[] values){
        ByteBuffer buffer = ByteBuffer.allocate(4 * values.length);

        for (float value : values){
            buffer.putFloat(value);
        }

        return buffer.array();
    }

    public static float[] ByteArray2FloatArray(byte[] values){

        float[] floatBuffer = new float[values.length/3*4 / Float.BYTES];
        byte[] buffer = new byte[4];

        for(int i = 0; i < floatBuffer.length; i++){
            System.arraycopy(values, i, buffer, 1, buffer.length - 1);
            buffer[0] = 0x000000;
            ByteBuffer byteBuffer = ByteBuffer.wrap(buffer);

            floatBuffer[i] = byteBuffer.getFloat();

            //System.out.println(floatBuffer[i]);
        }

        System.out.println(floatBuffer.length);
        return floatBuffer;
    }
}
