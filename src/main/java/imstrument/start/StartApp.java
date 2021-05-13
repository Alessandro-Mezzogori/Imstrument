package imstrument.start;

import imstrument.sound.openal.AudioThread;
import imstrument.sound.utils.Octave;
import imstrument.sound.waves.*;

import javax.swing.*;

public class StartApp {
    public static WaveManager waveManager;
    public static AudioThread audioThread;

    public static void main(String[] args){
        Soundwave carrier = new Soundwave(new Wavetable(Wavetable.Type.SIMPLE, 0), 440.0);


        /* initialize audio thread and WaveManager*/

        waveManager = new WaveManager();
        waveManager.importWaveSettings(carrier, WaveManager.KeyboardRows.TOP_ROW, Octave._3);
        waveManager.importWaveSettings(carrier, WaveManager.KeyboardRows.BOTTOM_ROW, Octave._4);

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
