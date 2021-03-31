package imstrument.start;

import imstrument.homepage.Homepage;

import javax.swing.*;
import java.awt.*;

public class StartApp {
    public static void main(String[] args){
        /* crea una nuova istanza della imstrument.Homepage tramite il dispatcher degli eventi */
        SwingUtilities.invokeLater(StartingGUI::new);
    }
}
