package imstrument.imagepage;

/* imstrument packages */
import imstrument.algorithm.ControlWindow;
import imstrument.colormap.ColorMap;
import imstrument.sound.utils.SoundImagePanel;
import imstrument.sound.waves.ModulatingWaveNumberSpinner;
import imstrument.sound.waves.WaveManager;
import imstrument.sound.wavetables.WavetableSelector;
import imstrument.start.StartApp;
import imstrument.start.TopContainer;
import imstrument.virtualkeyboard.VirtualKeyboardVisualizer;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * specialized JPanel to show the imagepage of the application Imstrument
 */
public class Imagepage extends JPanel {
    /* GUI components */
    /**
     * application menu
     */
    JMenuBar menuBar;
    /**
     * panel showing the image and processing the click events
     */
    SoundImagePanel soundImagePanel;
    /**
     * keyboard visualizer and octave changes
     */
    VirtualKeyboardVisualizer virtualKeyboardVisualizer;

    public Imagepage(){
        /* initialize components */
        /* create menubar */
        menuBar = new JMenuBar();

        // logo formatting
        JButton logoIcon = new JButton();
        logoIcon.setBorderPainted(false);
        logoIcon.setBorder(null);
        logoIcon.setMargin(new Insets(0, 0, 0, 0));
        logoIcon.setContentAreaFilled(false);
        try {
            // read the imstrument logo
            BufferedImage logoImage = ImageIO.read(this.getClass().getResource("/imstrument/globals/imstrument_logo.png"));
            logoIcon.setIcon(new ImageIcon(logoImage));
        } catch (IOException e) {
            logoIcon.setText("Homepage");
        }

        // when the logo is clicked return to the main page
        logoIcon.addActionListener(
                e -> {
                    // get the JFrame of the current card
                    JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor((JComponent) e.getSource());
                    if(topFrame instanceof TopContainer){
                        // if it's an instance of TopContainer ( container for the card layout )
                        // change to the HOMEPAGE card
                        TopContainer topContainer = (TopContainer) topFrame;
                        topContainer.changeCard(TopContainer.HOMEPAGE);
                    }
                }
        );

        menuBar.add(logoIcon);

        /* import menu */
        // menu item to import an image to be procecssed
        JMenu importMenu = new JMenu("Import");
        // menu item to import an image .jpg or .png
        JMenuItem importImage = new JMenuItem("Image");
        importImage.addActionListener(e -> importImage());
        importMenu.add(importImage);

        menuBar.add(importMenu);

        /* algorithms menu */
        // menu used to create and select the possible computing algorithms
        JMenu algorithmsMenu = new JMenu("Algorithms");
//        JMenuItem createAlgorithm = new JMenuItem("Create Algorithm");
//        createAlgorithm.addActionListener(e -> SwingUtilities.invokeLater(() -> new CustomAlgorithmCreator(false)));
//        algorithmsMenu.add(createAlgorithm);

        // menu item that when selected popups the control panel of the algorithm
        // used to create and / or select an algorithm
        JMenuItem algorithmControls = new JMenuItem("Controls");
        algorithmControls.addActionListener(e -> SwingUtilities.invokeLater(ControlWindow::new));
        algorithmsMenu.add(algorithmControls);


        menuBar.add(algorithmsMenu);

        /* visualize menu */
        // used to show hidden information or possible visualizers ( Keyboard and colormap )
        JMenu visualizeMenu = new JMenu("View");

        // menu item that when clicked will show the keyboard visualizer
        JMenuItem visualizeVirtualKeyboard = new JMenuItem("Virtual Keyboard");
        visualizeVirtualKeyboard.addActionListener(
                // create a  runnable that instantiate the virtual keyboard visualizer for future access
                // and linking between this jframe and the visualizer
                e -> SwingUtilities.invokeLater(() -> virtualKeyboardVisualizer = new VirtualKeyboardVisualizer())
        );
        visualizeMenu.add(visualizeVirtualKeyboard);

        // menu item that will show the color map manager
        JMenuItem showColorMap = new JMenuItem("Show ColorMap");
        showColorMap.addActionListener(e -> SwingUtilities.invokeLater(() -> { ColorMap colorMap = new ColorMap(soundImagePanel.getImage()); }));
        visualizeMenu.add(showColorMap);

        menuBar.add(visualizeMenu);
        /* mp3 management menu */
        // export menu
        JMenu wavMenu = new JMenu("WAV");

        // start recording what is being played
        JMenuItem startRecording = new JMenuItem("Start recording");
        startRecording.addActionListener(e -> StartApp.recorder.start());
        startRecording.setAccelerator(KeyStroke.getKeyStroke('r', Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        wavMenu.add(startRecording);

        // stop recoding what is played
        JMenuItem stopRecording = new JMenuItem("Stop recording");
        stopRecording.addActionListener(e -> StartApp.recorder.stop());
        stopRecording.setAccelerator(KeyStroke.getKeyStroke('s', Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        wavMenu.add(stopRecording);

        menuBar.add(wavMenu);

        /* sound menu */
        // used to manipulate the keyboard
        JMenu soundMenu = new JMenu("Sound");
        // wavetable selector
        JMenuItem wavetableSelector = new JMenuItem("Select wavetable");
        wavetableSelector.addActionListener(e -> SwingUtilities.invokeLater(WavetableSelector::new));
        soundMenu.add(wavetableSelector);

        // imports the current mouse soundwave in the top keyboard
        JMenuItem toKeyboard0 = new JMenuItem("Save to top keyboard");
        toKeyboard0.addActionListener(
                // import inside the top keyboard the current mouse soundwave with the current selected octave
                e -> StartApp.waveManager.importWaveSettings(
                        StartApp.waveManager.soundwaves.get(WaveManager.MOUSE_SOUNDWAVE_INDEX),
                        WaveManager.KeyboardRows.TOP_ROW,
                        StartApp.waveManager.currentOctaves[WaveManager.KeyboardRows.TOP_ROW.getRowNumber()]
                )
        );
        // setup keyboard shortcut CTRL + 1
        toKeyboard0.setAccelerator(KeyStroke.getKeyStroke('1', Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        soundMenu.add(toKeyboard0);

        // imports the current mouse soundwave in the bottom keyboard
        JMenuItem toKeyboard1 = new JMenuItem("Save to bottom keyboard");
        toKeyboard1.addActionListener(
                // import inside the bottom keyboard the current mouse soundwave with the current selected octave
                e -> StartApp.waveManager.importWaveSettings(
                        StartApp.waveManager.soundwaves.get(WaveManager.MOUSE_SOUNDWAVE_INDEX),
                        WaveManager.KeyboardRows.BOTTOM_ROW,
                        StartApp.waveManager.currentOctaves[WaveManager.KeyboardRows.BOTTOM_ROW.getRowNumber()]
                )
        );
        // setup keyboard shortcut CTRL + 2
        toKeyboard1.setAccelerator(KeyStroke.getKeyStroke('2', Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        soundMenu.add(toKeyboard1);

        menuBar.add(soundMenu);

        /* all the components added after horizontal glue will be to the right side of the menu bar*/
        menuBar.add(Box.createHorizontalGlue());

        // controller for the number of modulators the carrier soundwave will have ( starts at 0 )
        ModulatingWaveNumberSpinner modulatingWaveNumberSpinner = new ModulatingWaveNumberSpinner();
        JLabel modulatingWaveNumberSpinnerLabel = new JLabel("Modulators number ");
        modulatingWaveNumberSpinnerLabel.setLabelFor(modulatingWaveNumberSpinner);

        menuBar.add(modulatingWaveNumberSpinnerLabel);
        menuBar.add(modulatingWaveNumberSpinner);

        /* image panel */
        this.soundImagePanel = new SoundImagePanel();
        //this.soundImagePanel.setSoundAlgorithm(new HorizontalAlgorithm());
        /* jframe settings and params */
        this.setLayout(new BorderLayout());
        this.add(menuBar, BorderLayout.NORTH);
        this.add(soundImagePanel, BorderLayout.CENTER);

        setKeyboardBindings();
    }

    /**
     * Makes the user select a jpg or png image and loads it as the current
     * rendered image of the sound image panel component
     */
    private void importImage(){
        // create a file chooser object that only accepts the extensions JPG and PNG
        JFileChooser imageChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "JPG & PNG", "jpg", "png"
        );
        imageChooser.setFileFilter(filter);

        // retrieve if something was confirmed inside the file chooser dialog
        int choosingResult = imageChooser.showOpenDialog(this);
        if(choosingResult == JFileChooser.APPROVE_OPTION){
            try {
                // retrieve the selected image
                this.soundImagePanel.setImage(ImageIO.read(imageChooser.getSelectedFile()));
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "An error has occurred during the loading of the image, retry");
            }
        }
    }

    /**
     * function that setups all the keyboard bindings to play the top and bottom virtual keyboards
     */
    private void setKeyboardBindings(){
        InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        // TOP ROW PRESSED
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, false), "CT_T");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_3, 0, false), "C#T_T");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_E, 0, false), "DT_T");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_4, 0, false), "D#T_T");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_R, 0, false), "ET_T");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_T, 0, false), "FT_T");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_6, 0, false), "F#T_T");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_Y, 0, false), "GT_T");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_7, 0, false), "G#T_T");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_U, 0, false), "AT_T");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_8, 0, false), "A#T_T");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_I, 0, false), "BT_T");

        // BOTTOM ROW PRESSED
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, 0, false), "CB_T");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, false), "C#B_T");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_X, 0, false), "DB_T");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, false), "D#B_T");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_C, 0, false), "EB_T");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_V, 0, false), "FB_T");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_G, 0, false), "F#B_T");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_B, 0, false), "GB_T");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_H, 0, false), "G#B_T");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_N, 0, false), "AB_T");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_J, 0, false), "A#B_T");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_M, 0, false), "BB_T");

        // TOP ROW RELEASE
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, true), "CT_R");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_3, 0, true), "C#T_R");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_E, 0, true), "DT_R");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_4, 0, true), "D#T_R");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_R, 0, true), "ET_R");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_T, 0, true), "FT_R");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_6, 0, true), "F#T_R");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_Y, 0, true), "GT_R");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_7, 0, true), "G#T_R");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_U, 0, true), "AT_R");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_8, 0, true), "A#T_R");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_I, 0, true), "BT_R");

        // BOTTOM ROW RELEASE
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, 0, true), "CB_R");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, true), "C#B_R");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_X, 0, true), "DB_R");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, true), "D#B_R");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_C, 0, true), "EB_R");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_V, 0, true), "FB_R");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_G, 0, true), "F#B_R");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_B, 0, true), "GB_R");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_H, 0, true), "G#B_R");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_N, 0, true), "AB_R");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_J, 0, true), "A#B_R");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_M, 0, true), "BB_R");

        // BUTTON PRESSED ACTION
        Action onPressed = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // get the index of the associated soundwave
                int index = convertBindingToIndex(e.getActionCommand());

                if(index != -1){
                    // start playing the associated soundwave
                    StartApp.waveManager.triggerWaveGeneration(index);
                    if (virtualKeyboardVisualizer != null){
                        // if the visualizer is showing show that we are pressing a button
                        virtualKeyboardVisualizer.setPressed(index , true);
                    }
                }
            }
        };

        // BUTTON RELEASE ACTION
        Action onRelease = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // get the index of the associated soundwave
                int index = convertBindingToIndex(e.getActionCommand());

                if(index != -1){
                    // stop the soundwave generation
                    StartApp.waveManager.setShouldGenerate(false, index);
                    if (virtualKeyboardVisualizer != null){
                        // if the visualizer is showing stop pressing the button
                        virtualKeyboardVisualizer.setPressed(index , false);
                    }
                }
            }
        };

        ActionMap actionMap = getActionMap();
        /* link all the possibile bindings the the action */
        actionMap.put("CT_T", onPressed);
        actionMap.put("C#T_T", onPressed);
        actionMap.put("DT_T", onPressed);
        actionMap.put("D#T_T", onPressed);
        actionMap.put("ET_T", onPressed);
        actionMap.put("FT_T", onPressed);
        actionMap.put("F#T_T", onPressed);
        actionMap.put("GT_T", onPressed);
        actionMap.put("G#T_T", onPressed);
        actionMap.put("AT_T", onPressed);
        actionMap.put("A#T_T", onPressed);
        actionMap.put("BT_T", onPressed);

        actionMap.put("CB_T", onPressed);
        actionMap.put("C#B_T", onPressed);
        actionMap.put("DB_T", onPressed);
        actionMap.put("D#B_T", onPressed);
        actionMap.put("EB_T", onPressed);
        actionMap.put("FB_T", onPressed);
        actionMap.put("F#B_T", onPressed);
        actionMap.put("GB_T", onPressed);
        actionMap.put("G#B_T", onPressed);
        actionMap.put("AB_T", onPressed);
        actionMap.put("A#B_T", onPressed);
        actionMap.put("BB_T", onPressed);

        actionMap.put("CT_R", onRelease);
        actionMap.put("C#T_R", onRelease);
        actionMap.put("DT_R", onRelease);
        actionMap.put("D#T_R", onRelease);
        actionMap.put("ET_R", onRelease);
        actionMap.put("FT_R", onRelease);
        actionMap.put("F#T_R", onRelease);
        actionMap.put("GT_R", onRelease);
        actionMap.put("G#T_R", onRelease);
        actionMap.put("AT_R", onRelease);
        actionMap.put("A#T_R", onRelease);
        actionMap.put("BT_R", onRelease);

        actionMap.put("CB_R", onRelease);
        actionMap.put("C#B_R", onRelease);
        actionMap.put("DB_R", onRelease);
        actionMap.put("D#B_R", onRelease);
        actionMap.put("EB_R", onRelease);
        actionMap.put("FB_R", onRelease);
        actionMap.put("F#B_R", onRelease);
        actionMap.put("GB_R", onRelease);
        actionMap.put("G#B_R", onRelease);
        actionMap.put("AB_R", onRelease);
        actionMap.put("A#B_R", onRelease);
        actionMap.put("BB_R", onRelease);
    }

    /**
     * Retrieves the associated soundwave index from a keyboard action command
     * @param actionCommand keyboard action command es. 'w' '3' 'u'...
     * @return the associated soundwave index
     */
    private int convertBindingToIndex(String actionCommand) {
        return switch (actionCommand) {
            case "w" -> 1;
            case "3" -> 2;
            case "e" -> 3;
            case "4" -> 4;
            case "r" -> 5;
            case "t" -> 6;
            case "6" -> 7;
            case "y" -> 8;
            case "7" -> 9;
            case "u" -> 10;
            case "8" -> 11;
            case "i" -> 12;
            case "z" -> 13;
            case "s" -> 14;
            case "x" -> 15;
            case "d" -> 16;
            case "c" -> 17;
            case "v" -> 18;
            case "g" -> 19;
            case "b" -> 20;
            case "h" -> 21;
            case "n" -> 22;
            case "j" -> 23;
            case "m" -> 24;
            default -> -1;
        };

    }
}
