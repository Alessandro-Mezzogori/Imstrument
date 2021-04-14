package imstrument.start;

import imstrument.homepage.Homepage;
import imstrument.sound.openal.AudioThread;
import imstrument.sound.utils.Note;
import imstrument.sound.utils.NoteFrequencyMapping;
import imstrument.sound.utils.Octave;
import imstrument.sound.waves.Envelope;
import imstrument.sound.waves.SoundWave;
import imstrument.sound.waves.WaveSummer;
import imstrument.sound.waves.SoundWaveType;

import javax.swing.*;
import java.util.ArrayList;

public class StartApp {
    /* temporaneo ? per separare la logica dal thread di swing*/
    public static WaveSummer waveSummer;
    public static AudioThread audioThread;

    public static void main(String[] args){
        /* crea una nuova istanza della imstrument.Homepage tramite il dispatcher degli eventi */
        ArrayList<SoundWave> soundWaves = new ArrayList<>();


        Envelope envelope = new Envelope(0.001, 0.001, 0.4, 0.2, 0.01, 0.0,0.0, 0.1);
        SoundWave carrier = new SoundWave(Short.MAX_VALUE, NoteFrequencyMapping.getNoteFrequency(Note.C, Octave._4), envelope);
        carrier.setWaveform(SoundWaveType.SINE);
        SoundWave modulating = new SoundWave((short)1, NoteFrequencyMapping.getNoteFrequency(Note.C, Octave._4), envelope);
        modulating.setWaveform(SoundWaveType.SINE);
        carrier.setModulatingWave(modulating, 6);
        soundWaves.add(carrier);

        Envelope envelope1 = new Envelope(0.001, 0.1, 0.3, 0.25, 0.2, 0.0,0.0, 0.1);
        SoundWave carrier1 = new SoundWave(Short.MAX_VALUE, NoteFrequencyMapping.getNoteFrequency(Note.C, Octave._3), envelope1);
        carrier1.setWaveform(SoundWaveType.SINE);
        SoundWave modulating1 = new SoundWave((short)1, NoteFrequencyMapping.getNoteFrequency(Note.C, Octave._3), envelope1);
        modulating1.setWaveform(SoundWaveType.SINE);
        carrier1.setModulatingWave(modulating1, 6);
        soundWaves.add(carrier1);

        /*
        Envelope envelope2 = new Envelope(0.001, 0.001, 0.3, 0.2, 0.01, 0.0,0.0, 0.1);
        SoundWave carrier2 = new SoundWave(Short.MAX_VALUE, NoteFrequencyMapping.getNoteFrequency(Note.G, Octave._4), envelope2);
        carrier2.setWaveform(SoundWaveType.SINE);
        SoundWave modulating2 = new SoundWave((short)1, NoteFrequencyMapping.getNoteFrequency(Note.G, Octave._4), envelope2);
        modulating2.setWaveform(SoundWaveType.SINE);
        carrier2.setModulatingWave(modulating2, 6);
        soundWaves.add(carrier2);
        */

        Envelope envelope3 = new Envelope(0.001, 0.001, 0.6, 0.2, 0.01, 0.0,0.0, 0.1);
        SoundWave carrier3 = new SoundWave(Short.MAX_VALUE, NoteFrequencyMapping.getNoteFrequency(Note.C, Octave._6), envelope3);
        carrier3.setWaveform(SoundWaveType.SAW);
        SoundWave modulating3 = new SoundWave((short)1, NoteFrequencyMapping.getNoteFrequency(Note.C, Octave._6), envelope3);
        modulating3.setWaveform(SoundWaveType.SINE);
        carrier3.setModulatingWave(modulating3, 6);
        soundWaves.add(carrier3);

        /* initialize audio thread and WaveSummer*/
        waveSummer = new WaveSummer(soundWaves);

        audioThread = new AudioThread(() -> {
            if (waveSummer.isShouldGenerate()) {
                short[] samples = new short[AudioThread.BUFFER_SIZE];
                for (int i = 0; i < AudioThread.BUFFER_SIZE; i++) {
                    samples[i] = waveSummer.generateSample();
                }
                return samples;
            }
            return null;
        });

        SwingUtilities.invokeLater(Homepage::new);
    }
}
