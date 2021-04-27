package imstrument.virtualkeyboard;

import javax.swing.*;
import java.awt.*;

public class BlackButton extends ButtonKey {

    public BlackButton(){
        super();
        JButton blackButton = new JButton();
        this.setBackground(Color.BLACK);
        blackButton.setOpaque(true);
    }

}
