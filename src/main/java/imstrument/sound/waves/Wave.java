package imstrument.sound.waves;

public class Wave {
    /**
     * default sample rate of all Wave instances, standard of WAV
     */
    public static final int DEFAULT_SAMPLE_RATE = 44100;

    /**
     * max amplitude of the Wave istance
     */
    protected short maxAmplitude;
    /**
     * frequency of the current instance of Wave
     */
    private double frequency;



    /* managing and generation attributes*/
    /**
     * classifier of which type of waveform is the current istance
     */
    private WaveType waveform;



    /* fm modulation */
    Wave modulatingWave;
    double indexOfModulation;
    Envelope envelope;

    public Wave(short maxAmplitude, double frequency, Envelope envelope){
        this.maxAmplitude = maxAmplitude;
        this.frequency = frequency;

        this.envelope = envelope;
        this.waveform = WaveType.SINE;

        reset();
    }

    /**
     * returns the next sample of the wave
     * @return returns short with the value of the next sample
     */
    public double generateSample(double time){
        double amplitude = envelope.getAmplitudeAmplifier(time)*maxAmplitude;
        return amplitude * waveFunction(time);
    }


    /**
     * retrieves the function of the wave object corrisponding to the current waveForm attribute value
     * @return a value between -1 and 1
     */
    protected double waveFunction(double time){
        double modulatingFrequency = 0.0;
        if(modulatingWave != null) {
            System.out.println(modulatingWave.generateSample(time));
            modulatingFrequency = indexOfModulation * modulatingWave.generateSample(time);
        }
        //System.out.println(modulatingFrequency);
        return switch (waveform) {
            case SINE -> Math.sin(2 * Math.PI * frequency * time + modulatingFrequency);
            case SAW -> 2 * (time * frequency - Math.floor(0.5 + time * frequency));
            case SQUARE -> Math.signum(Math.sin(2 * Math.PI * frequency * time));
            case TRIANGLE -> Math.abs(2 * (time * frequency - Math.floor(0.5 + time * frequency)));
            case NOISE -> 2 * Math.random() - 1.0;
        };
    }

    /**
     * starts the decay of the sound
     */
    public void startRelease(){
        envelope.startRelease();

        if(modulatingWave != null)
            modulatingWave.startRelease();
    }

    /**
     * reset the generation of the wave
     */
    public void reset() {
        envelope.reset();

        if(modulatingWave != null)
            modulatingWave.reset();
    }

    /* methods for envelope profile */

    public boolean isReleased(){return envelope.state == EnvelopeState.RELEASED;}

    /* setters */
    public void setAttack(double attack, double attackVelocity) {
        envelope.setAttack(attack, attackVelocity);
    }

    public void setWaveform(WaveType waveform) {
        this.waveform = waveform;
    }

    /* modulating wave methods */

    public void setModulatingWave(Wave modulatingWave, double indexOfModulation) {
        this.modulatingWave = modulatingWave;
        this.modulatingWave.maxAmplitude = 1;
        this.indexOfModulation = indexOfModulation;
    }
}
