package imstrument.start;

import imstrument.sound.openal.AudioThread;
import imstrument.sound.utils.Note;
import imstrument.sound.utils.NoteFrequencyMapping;
import imstrument.sound.utils.Octave;
import imstrument.sound.waves.*;

import javax.swing.*;

public class StartApp {
    public static WaveManager waveManager;
    public static AudioThread audioThread;

    public static void main(String[] args){
        Envelope envelope = new Envelope(1, 0.001, 1.0, 0.1, 0.01, 1.0,0.5, 0.1);
        SoundwaveSummer carrier = new SoundwaveSummer(
                new Soundwave[]{
                        new Soundwave(WaveTable.SINE, 440),
                        new Soundwave(WaveTable.SINE, 220),
                        new Soundwave(WaveTable.SAW, 300)
                },
                envelope
        );

        /* initialize audio thread and WaveManager*/

        waveManager = new WaveManager();
        waveManager.importWaveSettings(carrier, WaveManager.KeyboardRows.TOP_ROW, Octave._4);
        waveManager.importWaveSettings(carrier, WaveManager.KeyboardRows.BOTTOM_ROW, Octave._5);

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
