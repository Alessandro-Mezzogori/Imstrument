package imstrument.sound.utils;

public class NoteFrequencyMapping {
    private static double C0  = 16.35;
    private static double nextNoteFrequencyMultiplier = 1.0594;

    public static double getNoteFrequency(Note note, Octave octave){
        return Math.pow(nextNoteFrequencyMultiplier, note.getNoteNumber() + 12*octave.getOctaveNumber())*16.35;
    }
}

