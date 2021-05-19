package imstrument.sound.wavetables;

import imstrument.sound.waves.Soundwave;
import imstrument.start.StartApp;

import java.awt.*;

//TODO temporaneo, usato principalmente per debugging
public class WaveVisualizer extends Canvas {
    @Override
    public void paint(Graphics g) {
        Graphics2D graphics2D = (Graphics2D) g;

        Soundwave soundwave = StartApp.waveManager.soundwaves.get(1);

        double totalTime = soundwave.sweepEnvelope.getTotalTime();
        int x = 20, y = 150, sample_number = 1800;
        int jstep = (int) totalTime*Wavetable.SAMPLE_RATE / sample_number;
        double prevSample = soundwave.getSample();
        for(int i = 0; i < sample_number; i += 1, x += 1) {
            double sample = soundwave.getSample();
            for(int j = 0; j < jstep; j++)
                sample = soundwave.getSample();
            g.drawLine(x, (int)(prevSample * 20) + y, x + 1, (int)(sample * 20) + y);
            prevSample = sample;
        }

        soundwave.reset();
    }
}
