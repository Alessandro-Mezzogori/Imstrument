package imstrument.sound.waves;

import imstrument.sound.utils.Note;
import imstrument.sound.utils.NoteFrequencyMapping;
import imstrument.sound.utils.Octave;
import imstrument.sound.wavetables.Wavetable;
import imstrument.start.StartApp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Managing class
 * synchronizes the sample generation of multiple soundsWaves
 */
public class WaveManager{
    /**
     * array of the soundwaves generating samples
     */
    public final ArrayList<Soundwave> soundwaves;

    /**
     * TODO
     */
    public Octave[] currentOctaves;
    /**
     * array of boolean values, if true the corrisponding wave at the same index will generate
     * a sample when asked from the Wavesummer
     */
    private final ArrayList<Boolean> shouldGenerate;

    /**
     * flag used to shutdown | start the sound generation
     */
    private boolean generatingSamples;

    /* sound generation static params */
    /**
     * index of the wavesummer associated with mouse click
     */
    public static final int MOUSE_SOUNDWAVE_INDEX = 0;
    /**
     * numbers of keys in a single octave
     */
    public static final int OCTAVE_KEY_COUNT = 12;

    public WaveManager(){
        this.soundwaves = new ArrayList<>();
        this.currentOctaves = new Octave[KeyboardRows.values().length];
        // 0 -> click wave, 1-24 -> keyboard triggered waves
        int waveCount = 25;
        for(int i = 0; i < waveCount; i++)
            this.soundwaves.add(new Soundwave(new Wavetable(Wavetable.Type.SIMPLE, 0), 0));

        shouldGenerate = new ArrayList<>(Arrays.asList(new Boolean[soundwaves.size()]));
        Collections.fill(shouldGenerate, Boolean.FALSE);

    }


    /* interface/public methods */

    public void setShouldGenerate(boolean shouldGenerate, int index) {
        //TODO add controls
        this.shouldGenerate.set(index, shouldGenerate);
        if(!shouldGenerate)
            this.soundwaves.get(index).reset();
    }

    /**
     * generates the next audio sample
     * @return the audio sample linked to the current sampleIndex
     */
    public synchronized short generateSample(){
        /* generate sample */
        double sample = 0.0;
        boolean isGenerating = false;
        for(int i = 0; i < soundwaves.size(); i++){
            if(shouldGenerate.get(i)) {
                isGenerating = true;
                sample += soundwaves.get(i).getSample();
            }
        }

        this.generatingSamples = isGenerating;
        return (short) (sample*Short.MAX_VALUE/5.0); //TODO implement interpolation and normalization
    }

    /**
     *
     * @return if the WaveManager is generating samples
     */
    public boolean isGeneratingSamples() {
        return generatingSamples;
    }

    /* wave getters and setters */

    public void triggerWaveGeneration(int waveIndex){
        if (!StartApp.audioThread.isRunning()) {
            StartApp.audioThread.triggerPlayback();
        }
        generatingSamples = true;
        shouldGenerate.set(waveIndex, true);
    }

    public void importMouseWaveSettings(Soundwave soundwave){
        soundwaves.set(0 ,soundwave);
    }

    public void importWaveSettings(Soundwave soundwave, KeyboardRows keyboardRows, Octave octave){
        currentOctaves[keyboardRows.getRowNumber()] = octave;

        for(int i = 0; i < OCTAVE_KEY_COUNT; i++){
            soundwaves.get(i + keyboardRows.getRowNumber()*12 + 1).importFrom(soundwave, NoteFrequencyMapping.getNoteFrequency(Note.values()[i], octave));
        }
    }

    public void setOctave(KeyboardRows keyboardRows, Octave octave){
        currentOctaves[keyboardRows.getRowNumber()] = octave;

        for(int i = 0; i < OCTAVE_KEY_COUNT + 12* keyboardRows.getRowNumber(); i++){
            soundwaves.get(i).setFrequency(
                    NoteFrequencyMapping.getNoteFrequency(
                            Note.values()[i], octave
                    )
            );
        }
    }

    public enum KeyboardRows{
        TOP_ROW(0),
        BOTTOM_ROW(1),
        ;

        int rowNumber;
        KeyboardRows(int rowNumber){
            this.rowNumber = rowNumber;
        }

        public int getRowNumber() {
            return rowNumber;
        }
    }
}
