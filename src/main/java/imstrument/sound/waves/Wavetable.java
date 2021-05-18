package imstrument.sound.waves;

public class Wavetable {
    private float[][] wavetables;
    private Type type;
    private int wavetableIndex;

    public static int WAVETABLE_SIZE = 8192;
    public static int SAMPLE_RATE = 48000;
    private static final double fundamentalFrequency = ((double) SAMPLE_RATE/WAVETABLE_SIZE);

    public Wavetable(Type type, int wavetableIndex){
        this.type = type;
        this.wavetableIndex = wavetableIndex;

        final int testLength = 3;

        wavetables = new float[testLength][];
        for(int i = 0; i < testLength; i++)
            wavetables[i] = new float[WAVETABLE_SIZE];

        for (int i = 0; i < WAVETABLE_SIZE; i++) {
            double time = ((double) i / SAMPLE_RATE);
            wavetables[0][i] = (float) Math.sin(2 * Math.PI * fundamentalFrequency * time);
            wavetables[1][i] = Math.signum(wavetables[0][i]);
            wavetables[2][i] = (float)(2*(time*fundamentalFrequency - Math.floor(0.5 + time*fundamentalFrequency)));
        }

        //TODO switch type
    }

    public Wavetable(float[] wavetable){
        this.wavetables = new float[][]{wavetable};
        this.type = Type.CUSTOM;
        this.wavetableIndex = 0;
    }

    public float[] getSamples(int wavetableIndex){
        return wavetables[wavetableIndex];
    }

    public float[] getSamples(){
        return wavetables[this.wavetableIndex];
    }

    public int getWavetableNumber(){ return wavetables.length; }
    public int getWavetableIndex(){return wavetableIndex; }

    public enum Type{
        SIMPLE, //SINE, SAW, SQUARE, TRIANGLE
        CUSTOM, //WAVETABLE CREATED FROM OTHER WAVETABLES
    }
}
