package imstrument.start;

import imstrument.homepage.Homepage;
import imstrument.sound.openal.AudioThread;
import imstrument.sound.utils.Note;
import imstrument.sound.utils.NoteFrequencyMapping;
import imstrument.sound.utils.Octave;
import imstrument.sound.waves.*;

import javax.swing.*;
import java.util.ArrayList;

public class StartApp {
    public static WaveManager waveManager;
    public static AudioThread audioThread;

    public static void main(String[] args){
        /* crea una nuova istanza della imstrument.Homepage tramite il dispatcher degli eventi */
        Envelope envelope = new Envelope(1, 0.001, 1.0, 0.1, 0.01, 1.0,0.5, 0.1);
        SoundWave carrier = new SoundWave(Short.MAX_VALUE, NoteFrequencyMapping.getNoteFrequency(Note.C, Octave._4), envelope);
        carrier.setWaveform(SoundWaveType.SINE);

        SoundWave modulating = new SoundWave((short)1, NoteFrequencyMapping.getNoteFrequency(Note.C, Octave._4), envelope);
        modulating.setWaveform(SoundWaveType.SINE);
        carrier.setModulatingWave(modulating, 6);


        /* initialize audio thread and WaveManager*/

        waveManager = new WaveManager();
        waveManager.importWaveSettings(carrier, WaveManager.KeyboardRows.TOP_ROW, Octave._3);
        waveManager.importWaveSettings(carrier, WaveManager.KeyboardRows.BOTTOM_ROW, Octave._2);

        audioThread = new AudioThread(() -> {
            boolean isGenerating = false;

            if (waveManager.isGeneratingSamples()) {
                short[] samples = new short[AudioThread.BUFFER_SIZE];
                for (int i = 0; i < AudioThread.BUFFER_SIZE; i++) {
                    samples[i] = waveManager.generateSample();
                }
                return samples;
            }
            return null;
        });

        SwingUtilities.invokeLater(TopContainer::new);
    }
}
