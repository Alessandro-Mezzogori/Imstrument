package imstrument.sound.waves;

/**
 * Object abstracting the concept of wave
 */
public class SoundWave {
    /**
     * default sample rate of all Wave instances, standard of WAV
     */
    public static final int DEFAULT_SAMPLE_RATE = 44100;

    /**
     * index of the current generated sample
     */
    private int sampleIndex;

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
    private SoundWaveType waveform;

    /**
     * the envelope describing the ADSR cycle of the amplitude
     */
    private Envelope envelope;

    /* fm modulation */
    /**
     * sound wave modulating the frequency of the modulated
     */
    private SoundWave modulatingSoundWave;

    /**
     * degree of influence of the modulating wave on the frequency of the modulated
     */
    private double indexOfModulation;

    public SoundWave(short maxAmplitude, double frequency, Envelope envelope){
        this.maxAmplitude = maxAmplitude;
        this.frequency = frequency;

        this.envelope = envelope;
        this.waveform = SoundWaveType.SINE;

        reset();
    }

    public SoundWave(){
        this.maxAmplitude = 1;
        this.frequency = 261.63;

        this.envelope = new Envelope();
        this.waveform = SoundWaveType.SINE;
        reset();
    }

    /**
     * returns the next sample of the wave
     * @return returns short with the value of the next sample
     */
    public double generateSample(){
        double time = (double)(sampleIndex++)/DEFAULT_SAMPLE_RATE;
        double amplitude = envelope.getAmplitudeAmplifier(time)*maxAmplitude;
        return amplitude * waveFunction(time);
    }

    /**
     * retrieves the function of the wave object corrisponding to the current waveForm attribute value
     * @return a value between -1 and 1
     */
    private double waveFunction(double time){
        double modulatingFrequency = 0.0;
        if(modulatingSoundWave != null) {
            modulatingFrequency = indexOfModulation * modulatingSoundWave.generateModulatingSample(time);
        }
        return switch (waveform) {
            case SINE -> Math.sin(2 * Math.PI * frequency * time + modulatingFrequency);
            case SAW -> -(2/Math.PI) * Math.atan(1/Math.tan(time*Math.PI*frequency + modulatingFrequency)) ;
            case SQUARE -> Math.signum(Math.sin(2 * Math.PI * frequency * time + modulatingFrequency));
            case TRIANGLE -> Math.abs((2/Math.PI) * Math.atan(1/Math.tan(time*Math.PI*frequency + modulatingFrequency)));
            //case NOISE -> 2 * Math.random() - 1.0;
        };
    }

    //TODO crea una classe ModulatingSoundWave
    private double generateModulatingSample(double time){
        double amplitude = envelope.getAmplitudeAmplifier(time)*maxAmplitude;
        return amplitude * waveFunction(time);
    }

    /**
     * starts the decay of the sound
     */
    public void startRelease(){
        envelope.startRelease();

        if(modulatingSoundWave != null)
            modulatingSoundWave.startRelease();
    }

    /**
     * reset the generation of the wave
     */
    public void reset() {
        envelope.reset();
        sampleIndex = 0;

        if(modulatingSoundWave != null)
            modulatingSoundWave.reset();
    }

    /* methods for envelope profile */

    /**
     *
     * @return if the soundwave envelope is in the RELEASED state (amplitude = 0.0)
     */
    public boolean isReleased(){return envelope.state == EnvelopeState.RELEASED;}

    /* setters */

    /**
     * sets the SoundWave waveform
     * @param waveform waveform to be setted to
     */
    public void setWaveform(SoundWaveType waveform) {
        this.waveform = waveform;
    }

    /**
     * sets the Soundwave frequency
     * @param frequency frequency to be setted to
     */
    public void setFrequency(double frequency) {
        if(modulatingSoundWave != null) {
            /* keeps the ratio of carrier and modulating frequency the same*/
            modulatingSoundWave.setFrequency(frequency*this.frequency/modulatingSoundWave.frequency);
        }
        this.frequency = frequency;
    }

    /* modulating wave methods */
    /**
     * sets the modulating sound wave of the sound wave (it implicitly sets the max amplitude to 1)
     * @param modulatingSoundWave instance of the SoundWave class
     * @param indexOfModulation amount of influence on the frequency modulation
     */
    public void setModulatingWave(SoundWave modulatingSoundWave, double indexOfModulation) {
        this.modulatingSoundWave = modulatingSoundWave;
        this.modulatingSoundWave.maxAmplitude = 1;
        this.indexOfModulation = indexOfModulation;
    }

    /**
     * returns the modulating sound wave of the sound wave
     * @return a wave object
     */
    public SoundWave getModulatingWave(){return modulatingSoundWave;}

    /**
     * method to import settings from another SoundWave object, better than copying the object in performance
     * @param soundWave SoundWave object to be copied from
     */
    public void importSoundWaveSettings(SoundWave soundWave){
        this.maxAmplitude = soundWave.maxAmplitude;
        this.waveform = soundWave.waveform;
        this.envelope.importSettings(soundWave.envelope);


        /* if a modulating wave is present copy the modulating wave settings too */
        if (soundWave.modulatingSoundWave != null){
            /* if the current instance doesn't have modulation instantiate it */
            if(this.modulatingSoundWave == null)
                this.modulatingSoundWave = new SoundWave();

            this.modulatingSoundWave.importSoundWaveSettings(soundWave.modulatingSoundWave);
            this.indexOfModulation = soundWave.indexOfModulation;
        }
    }

    @Override
    public String toString() {
        return "Max: " + maxAmplitude + " Type: " + waveform + " F: " + frequency + "\nEnvelope:\n" + envelope.toString();
    }
}
