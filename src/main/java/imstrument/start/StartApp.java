package imstrument.start;

import imstrument.algorithm.Algorithm;
import imstrument.sound.openal.AudioThread;
import imstrument.sound.waves.*;
import imstrument.sound.wavetables.Wavetable;

import javax.swing.*;
import java.io.File;

public class StartApp {
    public static WaveManager waveManager;
    public static AudioThread audioThread;
    public static Algorithm algorithm;

    public static final File defaultFolder = new File(new JFileChooser().getFileSystemView().getDefaultDirectory().toString() + "/imstrument/");

    public static void main(String[] args){
        /* create default folder if it doesn't exists */
        if(!defaultFolder.exists()){
            boolean mkdir = defaultFolder.mkdir();//TODO notify user if default folder is not created  (mkdir = false)
        }


        Wavetable wavetable = new Wavetable(Wavetable.Type.SIMPLE, 0);
        wavetable.readFromFile();

        Soundwave carrier = new Soundwave(
                new Wavetable(Wavetable.Type.SIMPLE, 0),
                440.0,
                new Envelope(1.0, 1.0, 0.0, 1.0, 1.0, 0.0, 0.5, 1.0),
                new Soundwave(
                        new Wavetable(Wavetable.Type.SIMPLE, 0),
                        10.0,
                        new Envelope(1.0, 1.0, 0.0, 1.0, 1.0, 0.0, 0.5, 1.0),
                        null,
                        0.0
                ),
                1000.0
        );
        /* check if folder exists */

        /* initialize algorithm */
        algorithm = new Algorithm();

        /* initialize audio thread and WaveManager*/
        waveManager = new WaveManager();

       //waveManager.importWaveSettings(carrier, WaveManager.KeyboardRows.TOP_ROW, waveManager.currentOctaves[0]);
       //waveManager.importWaveSettings(carrier, WaveManager.KeyboardRows.BOTTOM_ROW, waveManager.currentOctaves[1]);
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
