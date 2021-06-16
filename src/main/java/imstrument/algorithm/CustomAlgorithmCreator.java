package imstrument.algorithm;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class CustomAlgorithmCreator extends JPanel {
    AlgorithmCanvas drawingAlgorithmCanvas;

    public CustomAlgorithmCreator(){
        /* create custom algorithm canvas */
        drawingAlgorithmCanvas = new AlgorithmCanvas();
        /* create custom algorithm buttons */
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
        JPanel buttonContainer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonContainer.add(saveButton);
        buttonContainer.add(cancelButton);

        setLayout(new BorderLayout());
        add(buttonContainer, BorderLayout.SOUTH);
        add(drawingAlgorithmCanvas, BorderLayout.CENTER);
    }
}
