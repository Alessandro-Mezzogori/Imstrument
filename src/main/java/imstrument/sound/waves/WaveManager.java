package imstrument.sound.waves;

import imstrument.sound.utils.Note;
import imstrument.sound.utils.NoteFrequencyMapping;
import imstrument.sound.utils.Octave;
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
    private final ArrayList<SoundwaveSummer> soundwaveSummers;

    /* TODO 3 onde a cui verrano passat gli indici a cui si vuole accedere */
    /* TODO array di index e indexStep per ogni waveCount, quando si cambia ottava si aggiorna indexWaveStep */
    /* TODO interpolazione quando il numero di onde che sta playing */
    /* TODO una sola envelope per ogni onda (macro envelope, visibile/modificabile in una tab a parte (tipo keyboard)*/

    private final int waveCount = 25; // 0 -> click wave, 1-24 -> keyboard triggered waves
    private final int octaveKeyCount = 12;

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
     * default sample rate
     */
    public static final int MOUSE_SOUNDWAVE_INDEX = 0;


    public WaveManager(){
        this.soundwaveSummers = new ArrayList<>();
        for(int i = 0 ; i < waveCount; i++)
            this.soundwaveSummers.add(new SoundwaveSummer());

        shouldGenerate = new ArrayList<>(Arrays.asList(new Boolean[soundwaveSummers.size()]));
        Collections.fill(shouldGenerate, Boolean.FALSE);

    }


    /* interface/public methods */

    /**
     * generates the next audio sample
     * @return the audio sample linked to the current sampleIndex
     */
    public synchronized short generateSample(){
        /* handle wave decay */
        updateGeneratingSamples();
        if(!generatingSamples)
            return 0;

        /* generate sample */
        double sample = 0.0;
        int sampleNumber = 0;
        for(int i = 0; i < soundwaveSummers.size(); i++){
            if(shouldGenerate.get(i)) {
                sample += soundwaveSummers.get(i).generateSample();
                sampleNumber++;
            }
        }
        return (short) (sample*Short.MAX_VALUE/sampleNumber); //TODO implement interpolation
    }

    private void updateGeneratingSamples(){
        boolean wavesReleased = true;
        for(int i = 0; i < soundwaveSummers.size(); i++) {
            boolean isReleased = soundwaveSummers.get(i).isReleased();

            if(!isReleased){
                wavesReleased = false;
            }
            else{
                shouldGenerate.set(i, false);
            }
        }

        if(wavesReleased){
            generatingSamples = false;
        }
    }

    /* wave generation controls */

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
        if(soundwaveSummers.get(waveIndex).isReleasingOrRelease() ) {
            soundwaveSummers.get(waveIndex).reset();
        }

        generatingSamples = true;
        shouldGenerate.set(waveIndex, true);
    }

    /* inner logic methods */
    public void startWaveRelease(int index){
        soundwaveSummers.get(index).startRelease();
    }

    public void importMouseWaveSettings(SoundwaveSummer soundWaveSummer){
        soundwaveSummers.get(0).importSoundwaveSummer(soundWaveSummer);
    }

    public void importWaveSettings(SoundwaveSummer soundWaveSummer, KeyboardRows keyboardRows, Octave octave){
        for(int i = 0; i < octaveKeyCount; i++){
            SoundwaveSummer sw = soundwaveSummers.get(i + keyboardRows.getRowNumber()*12 + 1);
            sw.importSoundwaveSummer(soundWaveSummer, NoteFrequencyMapping.getNoteFrequency(Note.values()[i], octave));
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
