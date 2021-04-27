package imstrument.algorithm;

import imstrument.sound.utils.Note;
import imstrument.sound.utils.NoteFrequencyMapping;
import imstrument.sound.utils.Octave;
import imstrument.sound.waves.Envelope;
import imstrument.sound.waves.SoundWave;
import imstrument.start.StartApp;

import java.awt.*;
import java.awt.image.BufferedImage;

public class HorizontalAlgorithm implements Algorithm{
    private final int colorsLength = 16;
    private final Point[] points;

    public HorizontalAlgorithm(){
        points = new Point[colorsLength];
        for(int i = 0; i < colorsLength; i++){
            points[i] = new Point(0,0);
        }

    }

    @Override
    public void compute(BufferedImage image, Point clickedPoint, boolean computeSoundWave) {
        int x = clickedPoint.x - (colorsLength - 1)/2;
        int y = clickedPoint.y;

        if(x < 0){
            x -= x;
        }

        if(x + colorsLength > image.getWidth()){
            x -= x + colorsLength - image.getWidth();
        }

        if(y < 0){
            y -=  y;
        }

        if(y > image.getHeight() ){
            y -= y - image.getHeight();
        }


        for(int i = 0; i < colorsLength; i++){
            points[i] = new Point(x, y);
            x += 1;
        }

        if(computeSoundWave) {
            Color[] colors = new Color[colorsLength];
            for (int i = 0; i < colorsLength; i++) {
                colors[i] = new Color(image.getRGB(points[i].x, points[i].y));
            }

            Envelope envelope = new Envelope(
                    colors[0].getRed() / 255.0,
                    colors[0].getGreen() / 255.0,
                    colors[0].getBlue() / 255.0,
                    colors[1].getRed() / 255.0,
                    colors[1].getGreen() / 255.0,
                    colors[1].getBlue() / 255.0,
                    colors[2].getRed() / 255.0,
                    colors[2].getGreen() / 255.0
            );
            SoundWave soundWave = new SoundWave(Short.MAX_VALUE, NoteFrequencyMapping.getNoteFrequency(Note.C, Octave._4), envelope);

            Envelope modulatingEnvelope = new Envelope(
                    colors[3].getRed() / 255.0,
                    colors[3].getGreen() / 255.0,
                    colors[3].getBlue() / 255.0,
                    colors[4].getRed() / 255.0,
                    colors[4].getGreen() / 255.0,
                    colors[4].getBlue() / 255.0,
                    colors[5].getRed() / 255.0,
                    colors[5].getGreen() / 255.0
            );
            SoundWave modulating = new SoundWave(
                    (short) 1,
                    colors[6].getRed() * colors[6].getGreen() * 0.10906574394, // magic number
                    modulatingEnvelope
            );

            soundWave.setModulatingWave(modulating, (int) (colors[6].getBlue() / 30) + 2);

            System.out.println(soundWave);
            System.out.println(modulating);

            StartApp.waveManager.importMouseWaveSettings(soundWave);
        }
    }

    private double getLuminance(Color color){
        return color.getGreen()*0.7152 + color.getRed()*0.2126 + color.getBlue()*0.0722;
    }

    public Point[] getPoints(){ return points; }
}
