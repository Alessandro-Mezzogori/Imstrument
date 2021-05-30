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

    double modulatingIndex = 0.0;
    double modulatingIndexStep = 60.0*Wavetable.WAVETABLE_SIZE/Wavetable.SAMPLE_RATE;
    Wavetable testWavetable = new Wavetable(Wavetable.Type.SIMPLE, 0);
    Envelope testEnvelope = new Envelope(0.5, 4.0, 1.0, 0.0001, 0.1, 0.5, 0.0, 0.0);

    public double getSample(){
        /* wavetable sweep */
        int wavetableNumber = wavetable.getWavetableNumber() - wavetable.getWavetableIndex() - 1;

        /* gets the current envelope that drives the sweep */
        double envelopeAmplitude = sweepEnvelope.getAmplitudeAmplifier((double)sampleIndex++/Wavetable.SAMPLE_RATE)*wavetableNumber;

        /* extracting the wavetable indices to linearly interpolate between them with the envelope amplitude as the driver */
        int wavetableIndex1 = envelopeAmplitude <= 0 ? 0 : (int) Math.floor(envelopeAmplitude);
        int wavetableIndex2 = wavetableIndex1 + 1 <= wavetableNumber ? wavetableIndex1 + 1 : wavetableIndex1;
        double firstWeight = 1 - (envelopeAmplitude - wavetableIndex1);
        int intWaveIndex = (int)Math.floor(waveIndex); //dummy variable used to approx. the waveIndex

        /* linear interpolation between */
        double sample = firstWeight*wavetable.getSamples(wavetableIndex1)[intWaveIndex] + (1.0 - firstWeight)*wavetable.getSamples(wavetableIndex2)[intWaveIndex];

        /* frequency modulation */
        double modulatingSample = testEnvelope.getAmplitudeAmplifier((double)sampleIndex++/Wavetable.SAMPLE_RATE)*testWavetable.getSamples()[(int)Math.floor(modulatingIndex)];
        //double modulatingSample = envelopeAmplitude*testWavetable.getSamples()[(int)Math.floor(modulatingIndex)];
        modulatingIndex = (modulatingIndex + modulatingIndexStep) % Wavetable.WAVETABLE_SIZE;

        /* updating the waveIndex for the next sample */
        waveIndex = Math.abs(waveIndex + (frequency + 30*modulatingSample)*Wavetable.WAVETABLE_SIZE/Wavetable.SAMPLE_RATE) % Wavetable.WAVETABLE_SIZE;
        //System.out.println("F: " + frequency + " Fm: " + modulatingSample*30 + " I: " + waveIndex);

        return sample;
    }

    public void reset(){
        /* reset the wave sample generation to the start*/
        this.waveIndex = 0;
        /* reset the generation of the sample array to the first element */
        this.sampleIndex = 0;
        /* resets the current time of the envelope driving the wavetable sweep */
        sweepEnvelope.reset();

        /* frequency modulation resets*/
        testEnvelope.reset();
        modulatingIndex = 0;
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
