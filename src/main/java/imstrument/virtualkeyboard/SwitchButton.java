package imstrument.virtualkeyboard;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class SwitchButton extends JButton {
    private int id;
    private int refKey;
    private char pos;

    public SwitchButton (char c, int index, int refKey) {
        this.pos = c;
        this.id = index;
        this.refKey = refKey;
        if (c == 'r') {
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
    }

    public int getId(){return id;}

    public int getRefKey(){return refKey;}
}
