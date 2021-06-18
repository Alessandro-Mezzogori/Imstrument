package imstrument.virtualkeyboard;

import imstrument.sound.waves.WaveManager;
import imstrument.start.StartApp;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class SwitchButton extends JButton {
    private boolean goUp;
    private final int refKey;
    private char pos;

    public SwitchButton ( boolean value, int refKey) {
        this.goUp = value;
        this.refKey = refKey;
        if (goUp) {
            try {
                Image img = ImageIO.read(getClass().getResource("/imstrument/globals/imstrument_rightarrow.jpg"));
                img.getScaledInstance(35,10, java.awt.Image.SCALE_SMOOTH);
                this.setIcon(new ImageIcon(img));
            } catch (IOException e) {
                e.printStackTrace(); //TODO aggiungi alternative
            }
        }
        else {
            try {
                Image img = ImageIO.read(getClass().getResource("/imstrument/globals/imstrument_leftarrow.jpg"));
                img.getScaledInstance(35,10, java.awt.Image.SCALE_SMOOTH);
                this.setIcon(new ImageIcon(img));
            } catch (IOException e) {
                e.printStackTrace(); //TODO aggiungi alternative
            }
        }

        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StartApp.waveManager.setOctave(
                        WaveManager.KeyboardRows.values()[refKey],
                        StartApp.waveManager.currentOctaves[refKey] + ((SwitchButton.this.goUp) ? 1 : -1)
                );
            }
        });
    }

    public boolean getId(){return goUp;}

    public int getRefKey(){return refKey;}
}
