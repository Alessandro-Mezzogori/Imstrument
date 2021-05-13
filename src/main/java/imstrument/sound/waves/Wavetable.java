package imstrument.sound.waves;

public class Wavetable {
    float[][] wavetables;

    public static int WAVETABLE_SIZE = 8192;
    public static int SAMPLE_RATE = 48000;

    public Wavetable(Type type, int wavetableIndex){
        double frequency = ((double)SAMPLE_RATE)/WAVETABLE_SIZE;

        wavetables = new float[1][];
        wavetables[0] = new float[WAVETABLE_SIZE];
        for(int i = 0; i < WAVETABLE_SIZE; i++){
            double time = ((double)i/SAMPLE_RATE);
            wavetables[0][i] = (float)Math.sin( 2 * Math.PI * frequency * time);
        }

        //TODO switch type
    }

    public Wavetable(Type type, int wavetableIndex, Envelope sweepEnvelope){
        
    }

    public float[] getSamples(int wavetableIndex){
        return wavetables[wavetableIndex];
    }

    public enum Type{
        SIMPLE, //SINE, SAW, SQUARE, TRIANGLE
        CUSTOM, //WAVETABLE CREATED FROM OTHER WAVETABLES
    }
}
