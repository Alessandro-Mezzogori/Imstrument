package imstrument.sound.wavetables;

import imstrument.sound.wavetables.WaveVisualizer;

import javax.swing.*;
import java.awt.*;

public class Test extends JFrame {
    public Test(){
        WaveVisualizer waveVisualizer = new WaveVisualizer();

        this.setLayout(new BorderLayout());
        this.add(waveVisualizer, BorderLayout.CENTER);
        this.setVisible(true);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize(dim.width, 400);
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
    }
}
