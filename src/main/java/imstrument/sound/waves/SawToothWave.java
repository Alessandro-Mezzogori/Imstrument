package imstrument.sound.waves;

public class SawToothWave extends Wave{
    public SawToothWave(short amplitude, int frequency) {
        super(amplitude, frequency);
    }

    @Override
    public short[] generate(int bufferSize, int sampleRate) {
        short[] buffer = new short[bufferSize];
        double pi = Math.PI;
        for(int i = 0; i < bufferSize; i++){
            double tf = (double)(time++)/sampleRate * frequency;
            buffer[i] = (short)(2*amplitude*(tf - Math.floor(0.5 + tf)));
            System.out.println(buffer[i]);
        }
        return buffer;
    }
}
