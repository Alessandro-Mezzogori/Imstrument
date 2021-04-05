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
    private boolean decaying;

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
        double sample = 0;
        /* handle wave decay */
        boolean wavesDecayed = true;
        if(decaying){
            for(Wave wave : this.waves) {
                wavesDecayed = wavesDecayed && wave.decayed;
            }

            if(wavesDecayed){
                forceWaveReset();
                return 0;
            }
        }

        /* generate sample */
        for(Wave wave: this.waves){
            sample += (wave.generateSample(this.sampleIndex++, this.sampleRate) / amplitudeSum);
        }
        return (short) (Short.MAX_VALUE*sample);
    }

    /* wave generation controls */
    public void start(){
        this.shouldGenerate = true;
    }

    public void stop() {
        startWavesDecay();
    }

    private void startWavesDecay(){
        this.decaying = true;
        for(Wave wave : waves)
            wave.startDecaying();
    }

    private void reset(){
        sampleIndex = 0;
        shouldGenerate = false;
        decaying = false;
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
