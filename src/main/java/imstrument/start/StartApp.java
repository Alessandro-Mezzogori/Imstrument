package imstrument.start;

import imstrument.algorithm.Algorithm;
import imstrument.sound.openal.AudioThread;
import imstrument.sound.openal.Recorder;
import imstrument.sound.waves.*;
import imstrument.sound.wavetables.Wavetable;

import javax.swing.*;
import java.io.File;

public class StartApp {
    public static WaveManager waveManager;
    public static AudioThread audioThread;
    public static Algorithm algorithm;
    public static Recorder recorder;

    public static final File defaultFolder = new File(new JFileChooser().getFileSystemView().getDefaultDirectory().toString() + "/imstrument/");

    public static void main(String[] args){
        /* create default folder if it doesn't exists */
        if(!defaultFolder.exists()){
            boolean mkdir = defaultFolder.mkdir();
        }

        /* initialize algorithm */
        algorithm = new Algorithm();

        /* initialize WaveManager*/
        waveManager = new WaveManager();

        /* initialize the audio recorder */
        recorder = new Recorder();

        /* initialize the audio thread */
        audioThread = new AudioThread(() -> {
            // sample supplier to the audio thread
            // if it's recording or generating samples create a sample array
            if (waveManager.isGeneratingSamples() || recorder.isRecording()) {
                short[] samples = new short[AudioThread.BUFFER_SIZE];

                // if it's generating then populate the sample array with sound data
                if(waveManager.isGeneratingSamples()) {
                    for (int i = 0; i < AudioThread.BUFFER_SIZE; i++) {
                        samples[i] = waveManager.generateSample();
                    }
                    return samples;
                }
            }
            return null;
        });

        SwingUtilities.invokeLater(TopContainer::new);
    }
}
