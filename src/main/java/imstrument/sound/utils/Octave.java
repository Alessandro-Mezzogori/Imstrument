package imstrument.sound.utils;

/**
 * Enum containing the usable octaves
 */
public enum Octave{
    _0(0),
    _1(1),
    _2(2),
    _3(3),
    _4(4),
    _5(5),
    _6(6),
    _7(7),
    _8(8);

    private final int octaveNumber;

    Octave(int octaveNumber){
        this.octaveNumber = octaveNumber;
    }

    /**
     *
     * @return the associated number of the Octave
     */
    public int getOctaveNumber(){
        return  octaveNumber;
    }
}