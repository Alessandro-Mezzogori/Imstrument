package imstrument.virtualkeyboard;

import javax.swing.*;
import javax.swing.event.AncestorListener;
import java.awt.*;
import java.awt.event.ComponentListener;
import java.awt.event.MouseListener;

public class PianoKey extends JComponent {
    protected boolean pressed;
    private int id;
    private KeyColor color;
    public PianoKey(int index, KeyColor color) {
        super();
        this.color = color;
        this.id = index;
        pressed = false;
    }

    public void setPressed(boolean pressed) {
        this.pressed = pressed;
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        if(pressed){
            Dimension size = getSize();
            g.setColor(new Color(110, 140, 240));
            g.fillRect(0, 0, size.width, size.height);
            return;
        }
        if (color==KeyColor.WHITE){
            g.setColor(java.awt.Color.WHITE);
            g.fillRect(0,0,35,160);
        }
        else if(color==KeyColor.BLACK){
            g.setColor(java.awt.Color.BLACK);
            g.fillRect(0,0,25,95);
        }
    }

    public int getId() {
        return id;
    }

    public enum KeyColor{
        WHITE,BLACK
    }

}
