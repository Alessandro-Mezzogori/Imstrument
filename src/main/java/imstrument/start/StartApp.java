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
            boolean mkdir = defaultFolder.mkdir();
        }

        Wavetable wavetable = new Wavetable(0);
        wavetable.readFromFile();

        /* initialize algorithm */
        algorithm = new Algorithm();

        /* initialize audio thread and WaveManager*/
        waveManager = new WaveManager();

         audioThread = new AudioThread(() -> {
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
