package imstrument.globals;

import javax.sound.midi.*;

public class ImageSynthetizer {
    Synthesizer midiSynth;
    Instrument[] instr;
    MidiChannel[] mChannels;
    public ImageSynthetizer(){
        try{
            /* Create a new Sythesizer and open it. Most of
             * the methods you will want to use to expand on this
             * example can be found in the Java documentation here:
             * https://docs.oracle.com/javase/7/docs/api/javax/sound/midi/Synthesizer.html
             */
            //TODO create algorithm implmeenting instrument
            midiSynth = MidiSystem.getSynthesizer();
            midiSynth.open();


            //get and load default instrument and channel lists
            instr = midiSynth.getDefaultSoundbank().getInstruments();
            mChannels = midiSynth.getChannels();
            midiSynth.loadInstrument(instr[0]);//load an instrument
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void playNote(int noteNumber){
        mChannels[0].noteOn(noteNumber, 100);//On channel 0, play note number 60 with velocity 100
        note = noteNumber;
    }
    int note;
    public void stopNote(){
        mChannels[0].noteOff(note);//turn of the note
    }
}
