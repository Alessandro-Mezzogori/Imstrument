package imstrument.sound.waves;

import imstrument.sound.utils.Note;
import imstrument.sound.utils.NoteFrequencyMapping;
import imstrument.sound.utils.Octave;

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
    private final ArrayList<SoundWave> soundWaves;

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
        this.soundWaves = new ArrayList<>();
        for(int i = 0 ; i < waveCount; i++)
            this.soundWaves.add(new SoundWave());

        shouldGenerate = new ArrayList<>(Arrays.asList(new Boolean[soundWaves.size()]));
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
        double amplitudeSum = 0.0;
        for(int i = 0; i < soundWaves.size(); i++){
            if(shouldGenerate.get(i)) {
                sample += soundWaves.get(i).generateSample();

                /* TODO  add velocity to key pressed ? */
                amplitudeSum += soundWaves.get(i).maxAmplitude;
            }
        }

        return (short) ((sample/amplitudeSum)*Short.MAX_VALUE);
    }

    private void updateGeneratingSamples(){
        boolean wavesReleased = true;
        for(int i = 0; i < soundWaves.size(); i++) {
            boolean isReleased = soundWaves.get(i).isReleased();

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

    public void setShouldGenerate(int waveIndex, boolean value){
        if(value && !shouldGenerate.get(waveIndex)) {
            soundWaves.get(waveIndex).reset();
            generatingSamples = true;
        }

        shouldGenerate.set(waveIndex, value);
    }

    /* inner logic methods */
    public void startWaveRelease(int index){
        soundWaves.get(index).startRelease();
    }

    public void importWaveSettings(SoundWave soundWave){
        for(int i = 0; i < soundWaves.size(); i++){
            SoundWave sw = soundWaves.get(i);
            sw.importSoundWaveSettings(soundWave);
            sw.setFrequency(NoteFrequencyMapping.getNoteFrequency(Note.values()[i % octaveKeyCount], Octave.values()[3 + i/octaveKeyCount]));
        }
    }
}
