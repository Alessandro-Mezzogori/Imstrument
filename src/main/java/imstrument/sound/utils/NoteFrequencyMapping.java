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
     * returns the frequency matching the passed note in the octave
     * @param note note to be converted
     * @param currentOctave octave to be converted
     * @return frequency of the passed note
     */
    public static double getNoteFrequency(Note note, int currentOctave){
        return Math.pow(2, (note.getNoteNumber() + 12*currentOctave)/12.0)*C0;
    }
}

