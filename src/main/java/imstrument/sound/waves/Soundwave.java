package imstrument.sound.waves;

import imstrument.sound.wavetables.Wavetable;

public class Soundwave {
    private double frequency;
    private int sampleIndex;

    private double waveIndex;
    private double waveIndexStep;

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

        int intWaveIndex = (int)Math.floor(waveIndex);
        double sample = firstWeight*wavetable.getSamples(wavetableIndex1)[intWaveIndex] + (1.0 - firstWeight)*wavetable.getSamples(wavetableIndex2)[intWaveIndex];

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
        this.waveIndexStep = frequency*Wavetable.WAVETABLE_SIZE/Wavetable.SAMPLE_RATE;
    }

    public void importFrom(Soundwave soundwave, double frequency){
        wavetable = soundwave.wavetable;
        sweepEnvelope.importSettings(soundwave.sweepEnvelope);
        setFrequency(frequency);
        reset();;
    }
}
