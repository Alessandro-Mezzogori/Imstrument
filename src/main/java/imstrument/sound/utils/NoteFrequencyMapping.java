package imstrument.sound.utils;

/**
 * util class to convert Note and Octave enums to the matching frequency
 */
public class NoteFrequencyMapping {
    /**
     * base note
     */
    private static final double C0  = 16.35;
    /**
     * frequency multiplier between two adjacent notes
     */
    private static final double nextNoteFrequencyMultiplier = 1.0594;

    /**
     * returns the frequency matching the passed note in the octave
     * @param note note to be converted
     * @param octave octave to be converted
     * @return frequency of the passed note
     */
    public static double getNoteFrequency(Note note, Octave octave){
        return Math.pow(nextNoteFrequencyMultiplier, note.getNoteNumber() + 12*octave.getOctaveNumber())*16.35;
    }
}

