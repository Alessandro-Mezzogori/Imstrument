package imstrument.sound.waves;

public class WavetableTransform {
    /**
     * class containing static methods to manipulate the wavetable class
     */

    /**
     * Creates a new wavetable TODO
     * @param wavetable
     * @param envelope
     * @return
     */
    public static Wavetable wavetableSweep(Wavetable wavetable, Envelope envelope){
        float[] wavetableMatrix = new float[Wavetable.WAVETABLE_SIZE];

        int wavetableNumber = wavetable.getWavetableNumber() - wavetable.getWavetableIndex() - 1;


        for(int index = 0; index < Wavetable.WAVETABLE_SIZE; index++){
            double envelopeAmplitude = envelope.getAmplitudeAmplifier((double)index/Wavetable.SAMPLE_RATE)*wavetableNumber;
            int wavetableIndex = envelopeAmplitude == 0 ? 0 : (int) Math.floor(envelopeAmplitude);
            double firstWeight = envelopeAmplitude - wavetableIndex;
            wavetableMatrix[index] = (float) (firstWeight*wavetable.getSamples(wavetableIndex)[index] + (1.0 - firstWeight)*wavetable.getSamples(wavetableIndex + 1)[index]);
        }



        return new Wavetable(wavetableMatrix);
    }
}
