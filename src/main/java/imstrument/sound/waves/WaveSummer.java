package imstrument.sound.waves;

import java.util.ArrayList;
import java.util.Arrays;

public class WaveSummer {
    private final ArrayList<Wave> waves;
    private boolean shouldGenerate;
    private int sampleRate;
    private double amplitudeSum;

    /* sound generation params */
    public static final int DEFAULT_SAMPLE_RATE = 44100;

    private int sampleIndex;
    private boolean release;

    public WaveSummer(ArrayList<Wave> waves){
        this.waves = waves;
        sampleRate = DEFAULT_SAMPLE_RATE;
        updateAmplitudeSum();
        reset();
    }


    public WaveSummer(Wave[] waves){
        this(new ArrayList<Wave>(Arrays.asList(waves)));
    }

    public WaveSummer(){
        this(new ArrayList<Wave>());
    }

    /* generate sample methods */
    public short generateSample(){

        /* handle wave decay */
        boolean wavesReleased = true;
        if(release){
            for(Wave wave : this.waves) {
                wavesReleased = wavesReleased && wave.isReleased();
            }

            if(wavesReleased){
                forceWaveReset();
                return 0;
            }
        }

        /* generate sample */
        double time = (double)(this.sampleIndex++)/this.sampleRate;
        double sample = 0.0;
        for(Wave wave: this.waves){
            sample += ((double)wave.generateSample(time))/this.amplitudeSum;
        }
        System.out.println(sample);
        return (short) (sample*this.amplitudeSum);
    }

    /* wave generation controls */
    public void start(){
        this.shouldGenerate = true;
    }

    public void stop() {
        startWavesRelease();
    }

    private void startWavesRelease(){
        this.release = true;
        for(Wave wave : waves)
            wave.startRelease();
    }

    private void reset(){
        sampleIndex = 0;
        shouldGenerate = false;
        release = false;
    }

    private void forceWaveReset(){
        reset();
        for(Wave wave : this.waves)
            wave.reset();
    }

    public boolean isShouldGenerate() {
        return shouldGenerate;
    }

    /* wave summer params control*/
    public void addWave(Wave wave){
        this.waves.add(wave);
        updateAmplitudeSum();
    }

    public void setSampleRate(int sampleRate){
        this.sampleRate = sampleRate;
    }

    private void updateAmplitudeSum(){
        amplitudeSum = 0.0;
        for(Wave wave: this.waves)
            amplitudeSum += wave.maxAmplitude;
    }
}
