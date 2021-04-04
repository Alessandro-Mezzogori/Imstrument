package imstrument.sound.waves;

public class SineWave extends Wave{
    public SineWave(short amplitude, int frequency){
        super(amplitude, frequency);
    }

    @Override
    public short[] generate(int bufferSize, int sampleRate) {
        short[] buffer = new short[bufferSize];
        for(int i = 0; i < bufferSize; i++){
            buffer[i] = (short) (amplitude*Math.sin(2*Math.PI*frequency*(time++)/sampleRate));
        }
        return buffer;
    }
}
