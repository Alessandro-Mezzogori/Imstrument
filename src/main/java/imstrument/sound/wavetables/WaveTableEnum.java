package imstrument.sound.wavetables;

public enum WaveTableEnum {
    SINE, SAW, TRIANGLE, SQUARE;

    public static final int WAVETABLE_SIZE = 2048;
    public static final int SAMPLE_RATE = 44100;

    private final double[] wavetable = new double[WAVETABLE_SIZE];

    static{
        double frequency = ((double)SAMPLE_RATE)/WAVETABLE_SIZE;
        for(int i = 0; i < WAVETABLE_SIZE; i++) {
            double time = i / ((double)SAMPLE_RATE);
            SINE.wavetable[i] = Math.sin( 2 * Math.PI * frequency * time);
            SQUARE.wavetable[i] = Math.signum(SINE.wavetable[i]);
            SAW.wavetable[i] = (2*(time*frequency - Math.floor(0.5 + time*frequency)));
            TRIANGLE.wavetable[i] = Math.abs(SAW.wavetable[i]);
        }
    }

    public double[] getSamples(){
        return wavetable;
    }
}
