package imstrument.algorithm;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class CustomAlgorithmCreator extends JPanel {
    AlgorithmCanvas drawingAlgorithmCanvas;
    UnitWindow unitWindow;
    ArrayList<AlgorithmUnit> groups;


    public CustomAlgorithmCreator(){
        /* create custom algorithm canvas */
        groups = new ArrayList<>();
        drawingAlgorithmCanvas = new AlgorithmCanvas(
                groups,
                () -> {
                    unitWindow.updateList();
                }
        );
        unitWindow = new UnitWindow(
                groups,
                () -> {
                    drawingAlgorithmCanvas.repaint();
                }
        );

        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.LINE_AXIS));
        container.add(drawingAlgorithmCanvas);
        container.add(unitWindow);

        /* create custom algorithm buttons */
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
        JPanel buttonContainer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonContainer.add(saveButton);
        buttonContainer.add(cancelButton);


        setLayout(new BorderLayout());
        add(buttonContainer, BorderLayout.SOUTH);
//        add(drawingAlgorithmCanvas, BorderLayout.CENTER);
        add(container, BorderLayout.CENTER);
    }

    public interface AlgorithmCreationSynchronizer {
        void update();
    }
}
