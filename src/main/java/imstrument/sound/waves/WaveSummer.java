package imstrument.sound.waves;

import java.util.ArrayList;

/**
 * Managing class
 * synchronizes the sample generation of multiple soundsWaves
 */
public class WaveSummer {
    /**
     * array of the soundwaves generating samples
     */
    private final ArrayList<SoundWave> soundWaves;
    /**
     * flag used to shutdown | start the sound generation
     */
    private boolean shouldGenerate;
    /**
     *  sample rate enforced on all the SoundWave objects
     */
    private int sampleRate;
    /**
     *  attribute containing the sum of all the maximum aplitudes of the SoundWave objects,
     *  used to normalize the amplitude of the output sample
     */
    private double amplitudeSum;

    /**
     * index of the current generated sample
     */
    private int sampleIndex;
    /**
     * flag used to start the relaease of the SoundWaves
     */
    private boolean release;

    /* sound generation static params */
    /**
     * default sample rate
     */
    public static final int DEFAULT_SAMPLE_RATE = 44100;


    public WaveSummer(ArrayList<SoundWave> soundWaves){
        this.soundWaves = soundWaves;
        sampleRate = DEFAULT_SAMPLE_RATE;
        updateAmplitudeSum();
    }

    /* interface/public methods */

    /**
     * generates the next audio sample
     * @return the audio sample linked to the current sampleIndex
     */
    public short generateSample(){
        /* handle wave decay */
        boolean wavesReleased = true;
        if(release){
            for(SoundWave soundWave : this.soundWaves) {
                wavesReleased = wavesReleased && soundWave.isReleased();
            }

            if(wavesReleased){
                shouldGenerate = false;
                return 0;
            }
        }

        /* generate sample */
        double time = (double)(this.sampleIndex++)/this.sampleRate;
        double sample = 0.0;
        for(SoundWave soundWave : this.soundWaves){
            sample += ((double) soundWave.generateSample(time))/this.amplitudeSum;
        }
        return (short) (sample*this.amplitudeSum);
    }

    /* wave generation controls */

    /**
     * start generation of sound
     */
    public void start(){
        reset();
        this.shouldGenerate = true;
    }

    /**
     * start the release of the SoundWaves
     */
    public void stop() {
        startWavesRelease();
    }

    /**
     *
     * @return if the WaveSummer is generating samples
     */
    public boolean isShouldGenerate() {
        return shouldGenerate;
    }

    /* wave getters and setters */

    /**
     * add a new wave object to the summer
     * @param soundWave
     */
    public void addWave(SoundWave soundWave){
        this.soundWaves.add(soundWave);
        updateAmplitudeSum();
    }

    /**
     * retrieve a specific wave from the soundWave ArrayList
     * @param index of the selected wave
     * @return the SoundWave object selected if index is valid, else null
     */
    public SoundWave getWave(int index){
        try {
            return soundWaves.get(index);
        } catch (IndexOutOfBoundsException e){
            return null;
        }
    }

    /**
     * sets the sample rate of the wave summer
     * @param sampleRate sample rate to be setted to
     */
    public void setSampleRate(int sampleRate){
        this.sampleRate = sampleRate;
    }

    /**
     *
     * @return the number of SoundWaves generating the samples
     */
    public int getNumberOfWaves(){ return soundWaves.size();}

    /* inner logic methods */
    private void startWavesRelease(){
        this.release = true;
        for(SoundWave soundWave : soundWaves)
            soundWave.startRelease();
    }

    private void reset(){
        sampleIndex = 0;
        release = false;

        for(SoundWave soundWave : this.soundWaves)
            soundWave.reset();
    }

    private void updateAmplitudeSum(){
        amplitudeSum = 0.0;
        for(SoundWave soundWave : this.soundWaves)
            amplitudeSum += soundWave.maxAmplitude;
    }
}
