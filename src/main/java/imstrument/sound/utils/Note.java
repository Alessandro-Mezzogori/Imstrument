package imstrument.sound.utils;

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

    private int noteNumber;

    Note(int noteNumber){
        this.noteNumber = noteNumber;
    }

    public int getNoteNumber(){
        return noteNumber;
    }
}
