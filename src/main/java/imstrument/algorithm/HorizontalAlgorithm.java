package imstrument.algorithm;

import imstrument.sound.waves.Envelope;
import imstrument.sound.waves.SoundwaveSummer;
import imstrument.sound.waves.WaveTable;
import imstrument.start.StartApp;

import java.awt.*;
import java.awt.image.BufferedImage;

public class HorizontalAlgorithm extends Algorithm{
    private final int colorsLength = 28;
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
                (255 - getLuminance(colors[1]))/255,
                getLuminance(colors[2])/255,
                getLuminance(colors[3])/255,
                (255 - getLuminance(colors[4]))/255,
                0.5,
                (255 - getLuminance(colors[6]))/255,
                getLuminance(colors[7])/255
        );
        SoundwaveSummer soundWaveSummer = new SoundwaveSummer();

        System.out.println(soundWaveSummer);
        StartApp.waveManager.importMouseWaveSettings(soundWaveSummer);
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
