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
        ByteBuffer byteBuffer = ByteBuffer.wrap(values);
        float[] floatBuffer = new float[values.length / Float.BYTES];
        for(int i = 0; i < floatBuffer.length; i++){
            floatBuffer[i] = byteBuffer.getFloat(i*Float.BYTES);
        }
        return floatBuffer;
    }
}
