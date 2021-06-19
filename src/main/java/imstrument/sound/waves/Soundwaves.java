package imstrument.sound.waves;

/**
 * utility class for the Soundwave class
 */
public class Soundwaves {
    /**
     * returns the number of modulating soundwaves
     * @param soundwave a soundwave
     * @return the number of modulating soundwaves
     */
    public static int getModulatingSoundwaveNumber(Soundwave soundwave){
        int total = 0;

        if( soundwave != null) {
            while(soundwave.modulatingWave != null) {
                soundwave = soundwave.modulatingWave;
                total++;
            }
        }
        return total;
    }

    /**
     * wrapper for getModulatingSoundwave that always gets the inner most modulating wave
     * @param soundwave a soundwave instance
     * @return the inner most modulating wave of the soundwave chain
     */
    public static Soundwave getLastModulatingSoundwave(Soundwave soundwave){
        return getModulatingSoundwave(soundwave, getModulatingSoundwaveNumber(soundwave));
    }

    /**
     * utility method to get the modulator at the requested level, throws indexOutOfBounds if the level is
     * negative or is outside the number of the current modulators
     * @param soundwave instance of Soundwave class
     * @param level level of the modulator
     * @return returns
     */
    public static Soundwave getModulatingSoundwave(Soundwave soundwave, int level){
        if(soundwave == null ) return null;

        if(level < 0 || level > getModulatingSoundwaveNumber(soundwave))
            throw  new IndexOutOfBoundsException("level is negative or larger than the number of modulators present");

        while(soundwave.modulatingWave != null && level-- != 0) soundwave = soundwave.modulatingWave;
        return soundwave;
    }
}
