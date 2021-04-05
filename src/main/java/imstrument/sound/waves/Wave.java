package imstrument.sound.waves;

import java.util.Random;

public class Wave {
    public static final int DEFAULT_SAMPLE_RATE = 44100;

    protected short maxAmplitude;
    private double attack;
    private double decay;

    private boolean decaying;
    protected boolean decayed;
    private double startDecayTime;

    private double time;
    private double frequency;

    private double attackAmplitudeSlice;
    private double decayAmplitudeSlice;

    private WaveType waveform;

    public Wave(short maxAmplitude, double frequency, double attack, double decay){
        this.maxAmplitude = maxAmplitude;
        this.frequency = frequency;

        this.attack = attack;
        this.attackAmplitudeSlice = maxAmplitude / attack;

        this.decay = decay;
        this.decayAmplitudeSlice = maxAmplitude / decay;

        this.waveform = WaveType.SINE;
        reset();
    }

    /**
     * returns the next sample of the wave
     * containing the wave values
     * @return returns short with the value of the next sample
     */
    public short generateSample(int sampleIndex, int sampleRate){
        time = ((double) (sampleIndex)) / sampleRate;
        short amplitude = (short) Math.min(attackAmplitudeSlice * time, maxAmplitude);

        if(this.decaying){
            amplitude = (short) Math.max(0.0, amplitude - decayAmplitudeSlice*(time - startDecayTime));
            if(amplitude <= 0) {
                this.decayed = true;
            }
        }

        return (short) (amplitude * waveFunction());
    }

    public double waveFunction(){
        return switch (waveform) {
            case SINE -> Math.sin(2 * Math.PI * frequency * time);
            case SAW -> 2 * (time * frequency - Math.floor(0.5 + time * frequency));
            case SQUARE -> Math.signum(Math.sin(2 * Math.PI * frequency * time));
            case TRIANGLE -> Math.abs(2 * (time * frequency - Math.floor(0.5 + time * frequency)));
            case NOISE -> 2 * Math.random() - 1.0;
        };
    }

    /**
     * starts the decay of the sound
     */
    public void startDecaying(){
        decaying = true;
        startDecayTime = time;
    }

    /**
     * reset the generation of the wave
     */
    public void reset(){
        this.decaying = false;
        this.decayed = false;
    }

    /* setters */
    public void setAttack(double attack) {
        this.attack = attack;
        this.attackAmplitudeSlice = maxAmplitude / attack;
    }

    public void setDecay(double decay) {
        this.decay = decay;
        this.decayAmplitudeSlice = decayAmplitudeSlice / decay;
    }

    public void setWaveform(WaveType waveform) {
        this.waveform = waveform;
    }
}
