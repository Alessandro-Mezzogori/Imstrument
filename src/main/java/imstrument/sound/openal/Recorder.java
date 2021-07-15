package imstrument.sound.openal;

import imstrument.sound.utils.DatatypeConversion;
import imstrument.sound.wavetables.Wavetable;
import imstrument.start.StartApp;
import org.lwjgl.system.CallbackI;

import javax.sound.sampled.*;
import javax.swing.*;
import java.io.*;
import java.time.LocalDateTime;

public class Recorder {
    /**
     * tells if something is recording
     */
    private boolean isRecording = false;
    /**
     * default storing folder for the recordings
     */
    public static final File DEFAULT_FOLDER = new File(StartApp.defaultFolder.toString() + "/recordings/");
    /**
     * recording file
     */
    private File recordingFile;
    /**
     * temporary storage file
     */
    private File tempFile;
    /**
     * audio format of the recording
     */
    private final AudioFormat format;

    private FileOutputStream fileWriter;

    static {
        // if the folder doesn't exists create it
        if (!DEFAULT_FOLDER.exists()) {
            boolean mkdir = DEFAULT_FOLDER.mkdir(); // TODO notify user if default folder is not created
        }
    }

    public Recorder() {
        // set up the audio format for the recording files
        format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, Wavetable.SAMPLE_RATE, Short.SIZE, 1, Short.BYTES, Wavetable.SAMPLE_RATE, false);
    }

    public void record(short[] input) {
        if(isRecording) {
            // convert the input samples into the byte array
            byte[] buffer = DatatypeConversion.ShortArray2ByteArray(input);

            // write the content of the buffer to the temporary file
            try {
                fileWriter.write(buffer);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    public void start() {
        // start recording only if it wasn't before
        if(!isRecording) {
            // get the current datetime
            LocalDateTime date = java.time.LocalDateTime.now();
            // create a unique filename
            String filename = String.format(
                    "%s/recording_%02d%02d%04d_%02d%02d.wav",
                    DEFAULT_FOLDER.getAbsolutePath(),
                    date.getDayOfMonth(),
                    date.getMonthValue(),
                    date.getYear(),
                    date.getHour(),
                    date.getMinute()
            );

            // creation of the output file
            recordingFile = new File(filename);
            // creation of temporary storage file to store the data while recording
            tempFile = new File(DEFAULT_FOLDER + "/temp");

            try {
                // create a file writer in append mode for the temporary file
                fileWriter = new FileOutputStream(tempFile, true);
            } catch (FileNotFoundException ignored) { }

            // set the recording flag to true so it notifies the watchers
            isRecording = true;
        }
    }

    public void stop() {
         //create an input stream with the created buffer as the source
        if(isRecording) {
            try {
                // close the file writer to the temporary storage file
                fileWriter.close();

                // create an input stream to retrieve everything that was stored in the temporary file
                FileInputStream inputStream = new FileInputStream(tempFile);

                // create an audio input stream from the audio format and the given buffer
                AudioInputStream audioSample = new AudioInputStream(inputStream, format, tempFile.length() / format.getFrameSize());

                // output the contents of the temporary file to the output wave file
                AudioSystem.write(audioSample, AudioFileFormat.Type.WAVE, recordingFile);

                // cleanup the data streams
                audioSample.close();
                inputStream.close();
                // delete the temporary file
                boolean deletedTempFile = tempFile.delete();
            } catch (IOException ioException) {
                // if there's an exception stop the recording and notify the user
                ioException.printStackTrace();
                JOptionPane.showMessageDialog(null, "An error has occured during the recording, please retry");
            }
            // set the recording flag to false so the watches stop sending data
            isRecording = false;
        }
    }

    public boolean isRecording() {
        return isRecording;
    }
}