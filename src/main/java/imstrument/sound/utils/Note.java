package imstrument.sound.utils;

/**
 * Enum containing all the notes in a musical scale
 */
public enum Note{
    C(1),
    C_SHARP(2),
    D(3),
    D_SHARP(4),
    E(5),
    F(6),
    F_SHARP(7),
    G(8),
    G_SHARP(9),
    A(10),
    A_SHARP(11),
    B(12);

    private final int noteNumber;

    Note(int noteNumber){
        this.noteNumber = noteNumber;
    }

    /**
     *
     * @return the matching number of the Note (from C to B starting from 1)
     */
    public int getNoteNumber(){
        return noteNumber;
    }

    }
