package imstrument.sound.io;

import imstrument.sound.waves.Envelope;
import imstrument.sound.waves.SoundWave;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class SoundKeyboard {
    ArrayList<SoundWave> waves;
    Envelope commonEnvelope;
    SoundWave commonModulatingSoundWave;

    private static int numberOfKeys = 24;

    public SoundKeyboard(){
        commonEnvelope = new Envelope();

        this.waves = new ArrayList<>();
    }
}
