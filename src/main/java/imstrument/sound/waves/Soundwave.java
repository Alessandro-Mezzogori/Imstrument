package imstrument.sound.waves;

import imstrument.sound.wavetables.Wavetable;

public class Soundwave {
    /* general */
    private double frequency;
    private int sampleIndex;

    /* wave table sound gen attributes */
    private double waveIndex;
    private double waveIndexStep;

    /* wave table sweep attributes */
    public Wavetable wavetable;
    public Envelope sweepEnvelope;

    /* frequency modulation attributes */
    Soundwave modulatingWave;
    double modulatingIndex;

    public Soundwave(Wavetable wavetable, double frequency, Envelope sweepEnvelope, Soundwave modulatingWave, double modulatingIndex){
        this.frequency = frequency;
        this.wavetable = wavetable;
        this.sweepEnvelope = sweepEnvelope;

        this.modulatingWave = modulatingWave;

        this.modulatingIndex = modulatingIndex;
        /* compute step size */
        waveIndexStep = Wavetable.getStepSize(frequency);
        reset();
    }

    public Soundwave(Wavetable wavetable, double frequency) {
        this(
                wavetable,
                frequency,
                new Envelope(),
                null,
                0.0
        );
    }

    public Soundwave(){
        this(
                new Wavetable(Wavetable.Type.SIMPLE, 0),
                440,
                new Envelope(),
                null,
                0.0
        );
    }

    public double getSample(){
        /* variables for easier readability */
        double time = ((double)sampleIndex++)/Wavetable.SAMPLE_RATE;

        /* WAVETABLE SWEEP  */
        int wavetableNumber = wavetable.getWavetableNumber() - wavetable.getWavetableIndex() - 1;

        /* gets the current amplitude of the envelope that drives the sweep */
        double envelopeAmplitude = sweepEnvelope.getAmplitudeAmplifier(time)*wavetableNumber;

        /* extracting the wavetable indices to linearly interpolate between them with the envelope amplitude as the driver */
        int wavetableIndex1 = envelopeAmplitude <= 0 ? 0 : (int) Math.floor(envelopeAmplitude);
        int wavetableIndex2 = wavetableIndex1 + 1 <= wavetableNumber ? wavetableIndex1 + 1 : wavetableIndex1;
        double firstWeight = 1 - (envelopeAmplitude - wavetableIndex1);
        int intWaveIndex = (int)Math.floor(waveIndex); //dummy variable used to approx. the waveIndex

        /* linear interpolation  */
        double sample = firstWeight*wavetable.getSamples(wavetableIndex1)[intWaveIndex] + (1.0 - firstWeight)*wavetable.getSamples(wavetableIndex2)[intWaveIndex];

        /* frequency modulation trough the WaveTable modulatingWavetable and the associated envelope */
        double modulatingSample = modulatingWave != null ? modulatingWave.getSample() : 0.0;

        /* updating the indices
        *  waveIndex: is modified accordingly to the base wavetable step formula modified to accomodate the frequency modulatino
        *  modulatingWaveIndex: is modified only accordingly to the base wavetable step formula (implies that there's only one level of modulation )*/
        waveIndex = Math.abs(waveIndex + waveIndexStep + modulatingIndex*modulatingSample*Wavetable.WAVETABLE_SIZE/Wavetable.SAMPLE_RATE) % Wavetable.WAVETABLE_SIZE;

        return sample; //TODO add amplitudeEnvelope ?
    }

    public void reset(){
        /* reset the wave sample generation to the start*/
        this.waveIndex = 0;
        /* reset the generation of the sample array to the first element */
        this.sampleIndex = 0;
        /* resets the current time of the envelope driving the wavetable sweep */
        sweepEnvelope.reset();

        /* frequency modulation resets*/
        if( modulatingWave != null){
            modulatingWave.reset();
        }
    }

    /* getters and setters */

    public void importFrom(Soundwave soundwave, double frequency){
        wavetable = soundwave.wavetable;
        sweepEnvelope.importSettings(soundwave.sweepEnvelope);

        /* calculate step sizes */
        waveIndexStep = Wavetable.getStepSize(frequency);

        if(soundwave.modulatingWave != null){
            if(modulatingWave == null){
                modulatingWave = new Soundwave();
            }
            modulatingWave.importFrom(soundwave.modulatingWave, soundwave.modulatingWave.frequency);
            modulatingIndex = soundwave.modulatingIndex;
        }
        else {
            modulatingWave = null;
        }

        reset();
    }

    public void setFrequency(double frequency){
        waveIndexStep = Wavetable.getStepSize(frequency);
    }
}
