package imstrument.sound.waves;

public class Soundwave {
    private double frequency;
    private int waveIndex;
    private int waveIndexStep;

    private Wavetable wavetable;

    public Soundwave(Wavetable wavetable, double frequency){
        this.wavetable = wavetable;

        setFrequency(frequency);

        reset();
    }

    public double getSample(){
        double sample = wavetable.getSamples(0)[waveIndex];
        waveIndex = (waveIndex + waveIndexStep) % Wavetable.WAVETABLE_SIZE;
        return sample;
    }

    public void reset(){
        this.waveIndex = 0;
    }

    /* getters and setters */
    private void setFrequency(double frequency){
        this.frequency = frequency;
        this.waveIndexStep = (int) (frequency*Wavetable.WAVETABLE_SIZE/Wavetable.SAMPLE_RATE);
    }
}
