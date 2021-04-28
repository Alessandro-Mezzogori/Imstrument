package imstrument.algorithm;

import imstrument.sound.utils.Note;
import imstrument.sound.utils.NoteFrequencyMapping;
import imstrument.sound.utils.Octave;
import imstrument.sound.waves.Envelope;
import imstrument.sound.waves.SoundWave;
import imstrument.start.StartApp;

import java.awt.*;
import java.awt.image.BufferedImage;

public class HorizontalAlgorithm extends Algorithm{
    private final int colorsLength = 18;
    private final Point[] points;

    public HorizontalAlgorithm(){
        points = new Point[colorsLength];
        for(int i = 0; i < colorsLength; i++){
            points[i] = new Point(0,0);
        }

    }

    @Override
    public void computeSoundWave(BufferedImage image) {
        Color[] colors = new Color[colorsLength];
        for (int i = 0; i < colorsLength; i++) {
            colors[i] = new Color(image.getRGB(points[i].x, points[i].y));
        }

        Envelope envelope = new Envelope(
                getLuminance(colors[0])/255,
                getLuminance(colors[1])/255,
                getLuminance(colors[2])/255,
                getLuminance(colors[3])/255,
                getLuminance(colors[4])/255,
                getLuminance(colors[5])/255,
                getLuminance(colors[6])/255,
                getLuminance(colors[7])/255
        );
        SoundWave soundWave = new SoundWave(Short.MAX_VALUE, NoteFrequencyMapping.getNoteFrequency(Note.C, Octave._4), envelope);

        Envelope modulatingEnvelope = new Envelope(
                getLuminance(colors[8])/255,
                getLuminance(colors[9])/255,
                getLuminance(colors[10])/255,
                getLuminance(colors[11])/255,
                getLuminance(colors[12])/255,
                getLuminance(colors[13])/255,
                getLuminance(colors[14])/255,
                getLuminance(colors[15])/255
        );
        SoundWave modulating = new SoundWave(
                (short) 1, getLuminance(colors[16])*10 , modulatingEnvelope
        );

        soundWave.setModulatingWave(modulating, 3);

        System.out.println(soundWave);
        System.out.println("I: " + getLuminance(colors[17])/25.5);
        System.out.println(modulating);

        StartApp.waveManager.importMouseWaveSettings(soundWave);
    }

    @Override
    public void computePoints(BufferedImage image, Point clickedPoint) {
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
    }

    private double getLuminance(Color color){
        return color.getGreen()*0.7152 + color.getRed()*0.2126 + color.getBlue()*0.0722;
    }

    public Point[] getPoints(){ return points; }
}
