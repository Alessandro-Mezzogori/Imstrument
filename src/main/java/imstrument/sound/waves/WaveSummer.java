package imstrument.sound.waves;

import java.util.ArrayList;

public class WaveSummer {
    private final ArrayList<SoundWave> soundWaves;
    private boolean shouldGenerate;
    private int sampleRate;
    private double amplitudeSum;

    private int sampleIndex;
    private boolean release;

    /* sound generation static params */
    public static final int DEFAULT_SAMPLE_RATE = 44100;


    public WaveSummer(ArrayList<SoundWave> soundWaves){
        this.soundWaves = soundWaves;
        sampleRate = DEFAULT_SAMPLE_RATE;
        updateAmplitudeSum();
    }

    /* interface/public methods */
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
    public void start(){
        reset();
        this.shouldGenerate = true;
    }

    public void stop() {
        startWavesRelease();
    }

    public boolean isShouldGenerate() {
        return shouldGenerate;
    }

    /* wave getters and setters */
    public void addWave(SoundWave soundWave){
        this.soundWaves.add(soundWave);
        updateAmplitudeSum();
    }

    public SoundWave getWave(int index){ return soundWaves.get(index); }

    public void setSampleRate(int sampleRate){
        this.sampleRate = sampleRate;
    }

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
