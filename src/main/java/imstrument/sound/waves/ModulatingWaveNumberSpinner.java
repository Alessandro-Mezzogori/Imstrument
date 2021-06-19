package imstrument.sound.waves;

import imstrument.start.StartApp;
import org.lwjgl.system.CallbackI;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.DefaultFormatter;

public class ModulatingWaveNumberSpinner extends JSpinner {
    SpinnerNumberModel spinnerNumberModel;

    public static int numberOfModulators = 0;
    public static int maximumNumberOfModulators = 5;
    public static int minimumNumberOfModulators = 0;

    public ModulatingWaveNumberSpinner(){
        super();

        spinnerNumberModel = new SpinnerNumberModel(
                minimumNumberOfModulators,
                minimumNumberOfModulators,
                maximumNumberOfModulators,
                1
        );
        setModel(spinnerNumberModel);

        ((DefaultFormatter)((JSpinner.DefaultEditor) getEditor()).getTextField().getFormatter()).setAllowsInvalid(false);

        addChangeListener(e -> {
            /* get the current number of modulators */
            Soundwave soundwave = StartApp.waveManager.soundwaves.get(0);
            int current = Soundwaves.getModulatingSoundwaveNumber(soundwave);

            System.out.println("C: " + current + " S: " + spinnerNumberModel.getNumber());
            /* compute the difference between the current and the selected  */
            int difference = spinnerNumberModel.getNumber().intValue() - current;
            /* while there's a difference in the number of modulators keep adding / removing as needed */
            while(difference != 0){
                Soundwave modulator = null;
                if( difference > 0 ){
                    /* add a modulator to the last modulator */
                    modulator = Soundwaves.getLastModulatingSoundwave(soundwave);
                    modulator.modulatingWave = new Soundwave();
                    difference--;
                }
                else{
                    /* remove the last modulator */
                    modulator = Soundwaves.getModulatingSoundwave(soundwave, current - 1);
                    modulator.modulatingWave = null;
                    difference++;
                }

                modulator.modulatingIndex = 0.0;
            }
        });
    }
}
