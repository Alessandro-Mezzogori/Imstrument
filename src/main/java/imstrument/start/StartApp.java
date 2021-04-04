package imstrument.start;

import imstrument.homepage.Homepage;
import imstrument.sound.algorithm.AudioThread;

import javax.swing.*;
import java.awt.*;

public class StartApp {
    public static void main(String[] args){
        /* crea una nuova istanza della imstrument.Homepage tramite il dispatcher degli eventi */
        SwingUtilities.invokeLater(Homepage::new);
    }
}
