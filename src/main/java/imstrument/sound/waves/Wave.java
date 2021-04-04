package imstrument.sound.waves;

public abstract class Wave {
    protected short amplitude;
    protected int frequency;
    protected int time;

    public Wave(short amplitude, int frequency) {
        this.amplitude = amplitude;
        this.frequency = frequency;
        this.time = 0;
    }

    /**
     * reset the generation of the wave
     */
    public void reset(){
        time = 0;
    }
    /**
     * returns a short array of bufferSize size
     * containing the wave values
     * @param bufferSize the length the of the return array
     * @return returns the arrays containing the wave values
     */
    public abstract short[] generate(int bufferSize, int sampleRate);
}
