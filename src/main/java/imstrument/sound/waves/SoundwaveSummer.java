package imstrument.sound.waves;

/**
 * Object abstracting the concept of wave
 */
public class SoundwaveSummer {
    /**
     * index of the current generated sample
     */
    private int sampleIndex;

    private Soundwave[] soundwaves;

    /**
     * the envelope describing the ADSR cycle of the amplitude
     */
    private final Envelope envelope;

    public SoundwaveSummer(Soundwave[] soundwaves, Envelope envelope){
        this.envelope = envelope;
        this.soundwaves = soundwaves;
        reset();
    }

    public SoundwaveSummer(){
        this(new Soundwave[]{new Soundwave(WaveTable.SINE, 440), new Soundwave(WaveTable.SINE, 220), new Soundwave(WaveTable.SINE, 110)}, new Envelope());
    }

    /**
     * returns the next sample of the wave
     * @return returns short with the value of the next sample
     */
    public double generateSample(){
        double sample = 0.0;
        for(Soundwave soundwave : soundwaves){
            sample += soundwave.getSample() / soundwaves.length;
        }
        return envelope.getAmplitudeAmplifier((double) (sampleIndex++) / WaveTable.SAMPLE_RATE)*sample;
    }

    /**
     * starts the decay of the sound
     */
    public void startRelease(){
        envelope.startRelease();
    }

    /**
     * reset the generation of the wave
     */
    public void reset() {
        envelope.reset();
        sampleIndex = 0;

        for(Soundwave soundwave : soundwaves)
            soundwave.reset();
    }

    /* methods for envelope profile */

    /**
     *
     * @return if the soundwave envelope is in the RELEASED state (amplitude = 0.0)
     */
    public boolean isReleased(){return envelope.state == EnvelopeState.RELEASED;}

    /**
     *
     * @return if the soundwave envelope is in the RELEASE state (amplitude = 0.0)
     */
    public boolean isReleasingOrRelease(){return envelope.state == EnvelopeState.RELEASE || envelope.state == EnvelopeState.RELEASED;}

    /* setters */
    public void importSoundwaveSummer(SoundwaveSummer soundwaveSummer, double baseFrequency){
        importSoundwaveSummer(soundwaveSummer);
        double multiplier = baseFrequency/soundwaves[0].getFrequency();

        for(Soundwave soundwave : soundwaves)
            soundwave.setFrequency(soundwave.getFrequency()*multiplier);
    }

    public void importSoundwaveSummer(SoundwaveSummer soundwaveSummer){
        /* "copies" the waves contained in soundwaveSummer*/
        envelope.importSettings(soundwaveSummer.envelope);
        soundwaves = new Soundwave[soundwaveSummer.getSoundwavesNumber()];
        for(int i = 0; i < soundwaves.length; i++)
            soundwaves[i] = new Soundwave(soundwaveSummer.soundwaves[i]);
    }

    public int getSoundwavesNumber() {return soundwaves.length; }
}
