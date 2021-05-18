package imstrument.sound.waves;

public class Soundwave {
    private double frequency;
    private int sampleIndex;

    private int waveIndex;
    private int waveIndexStep;

    public Wavetable wavetable;
    public Envelope sweepEnvelope;


    public Soundwave(Wavetable wavetable, double frequency){
        this.wavetable = wavetable;
        this.sweepEnvelope = new Envelope();

        setFrequency(frequency);
        reset();
    }

    public double getSample(){
        int wavetableNumber = wavetable.getWavetableNumber() - wavetable.getWavetableIndex() - 1;
        double envelopeAmplitude = sweepEnvelope.getAmplitudeAmplifier((double)sampleIndex++/Wavetable.SAMPLE_RATE)*wavetableNumber;
        int wavetableIndex1 = envelopeAmplitude <= 0 ? 0 : (int) Math.floor(envelopeAmplitude);
        int wavetableIndex2 = wavetableIndex1 + 1 <= wavetableNumber ? wavetableIndex1 + 1 : wavetableIndex1;
        double firstWeight = 1 - (envelopeAmplitude - wavetableIndex1);

        double sample = firstWeight*wavetable.getSamples(wavetableIndex1)[waveIndex] + (1.0 - firstWeight)*wavetable.getSamples(wavetableIndex2)[waveIndex];

        if(sampleIndex % 1000 == 0)
            System.out.println("S: " + sample + " I: " +wavetableIndex1 + ", " + wavetableIndex2 + " W: " + firstWeight + " SI: " + sampleIndex + " T: " + (double)sampleIndex/Wavetable.SAMPLE_RATE);

        waveIndex = (waveIndex + waveIndexStep) % Wavetable.WAVETABLE_SIZE;
        return sample;
    }

    public void reset(){
        this.waveIndex = 0;
        this.sampleIndex = 0;
        sweepEnvelope.reset();
    }

    /* getters and setters */
    private void setFrequency(double frequency){
        this.frequency = frequency;
        this.waveIndexStep = (int) (frequency*Wavetable.WAVETABLE_SIZE/Wavetable.SAMPLE_RATE);
    }

    public void importFrom(Soundwave soundwave, double frequency){
        wavetable = soundwave.wavetable;
        sweepEnvelope.importSettings(soundwave.sweepEnvelope);
        setFrequency(frequency);
        reset();;
    }
}
