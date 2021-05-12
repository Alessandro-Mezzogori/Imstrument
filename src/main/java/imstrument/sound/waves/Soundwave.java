package imstrument.sound.waves;

public class Soundwave {
    private double frequency;
    private int waveTableIndex;
    private int waveTableIndexStep;
    private WaveTable waveTable;

    public Soundwave(WaveTable waveTable, double frequency){
        this.waveTable = waveTable;

        setFrequency(frequency);
        reset();
    }

    public Soundwave(Soundwave soundwave){
        setFrequency(soundwave.getFrequency());
        setWaveTable(soundwave.getWaveTable());
        reset();
    }

    public double getSample(){
        double sample = waveTable.getSamples()[waveTableIndex];
        waveTableIndex = (waveTableIndex + waveTableIndexStep) % WaveTable.WAVETABLE_SIZE;
        return sample;
    }

    public void reset(){
        this.waveTableIndex = 0;
    }

    public void setFrequency(double frequency){
        this.frequency = frequency;
        this.waveTableIndexStep = (int)frequency*WaveTable.WAVETABLE_SIZE/WaveTable.SAMPLE_RATE;
    }

    public double getFrequency(){return this.frequency;}

    public void setWaveTable(WaveTable waveTable){
        this.waveTable = waveTable;
    }

    public WaveTable getWaveTable(){return this.waveTable;}

}
