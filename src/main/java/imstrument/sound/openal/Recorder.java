package imstrument.sound.openal;

import imstrument.sound.utils.DatatypeConversion;
import imstrument.sound.wavetables.Wavetable;
import imstrument.start.StartApp;
import org.lwjgl.system.CallbackI;

import javax.sound.sampled.*;
import javax.swing.*;
import java.io.*;

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
            try {
                fileWriter.write(buffer);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

//            // create an input stream with the created buffer as the source
//            InputStream byteArrayInputStream = new ByteArrayInputStream(buffer);
//            // create an audio input stream from the audio format and the given buffer
//            AudioInputStream audioSample = new AudioInputStream(byteArrayInputStream, format, (long) buffer.length * Short.BYTES /format.getFrameSize());
//            // write everything
//            try {
//                AudioSystem.write(audioSample, AudioFileFormat.Type.WAVE, recordingFile);
//                audioSample.close();
//                byteArrayInputStream.close();
//                AudioSystem.
//            } catch (IOException ioException) {
//                // if there's an exception stop the recording and notify the user
//                ioException.printStackTrace();
//                stop();
//                JOptionPane.showMessageDialog(null, "An error has occured during the recording, please retry");
//            }
        }
    }

    public void start() {
        // start recording only if it wasn't before
        if(!isRecording) {
            recordingFile = new File(DEFAULT_FOLDER + "/Recording_1" + "");
            try {
                // create a file writer in append mode
                fileWriter = new FileOutputStream(recordingFile, true);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            isRecording = true;
        }
    }

    public void stop() {
         //create an input stream with the created buffer as the source
        try {
            FileInputStream inputStream = new FileInputStream(recordingFile);

            // create an audio input stream from the audio format and the given buffer
            AudioInputStream audioSample = new AudioInputStream(inputStream, format, recordingFile.length() / format.getFrameSize());
            // write everything
            recordingFile = new File(DEFAULT_FOLDER + "/Recording_1" + ".wav");

            AudioSystem.write(audioSample, AudioFileFormat.Type.WAVE, recordingFile);
            audioSample.close();
            inputStream.close();
        } catch (IOException ioException) {
            // if there's an exception stop the recording and notify the user
            ioException.printStackTrace();
            JOptionPane.showMessageDialog(null, "An error has occured during the recording, please retry");
        }
        isRecording = false;
    }

    public boolean isRecording() {
        return isRecording;
    }
}
//    private class SampleInputStream extends InputStream{
//
//        private float[] sample;
//        private int framesCounter;
//        private int idx;
//        private int cursor;
//        private int[] readSample = new int[2];
//        private int framesToRead;
//
//        public void setDataFrames(short[] sample)
//        {
//            float[] floaters = new float[sample.length];
//            for (int i = 0; i < sample.length; i++) {
//                floaters[i] = sample[i];
//            }
//            this.sample =  floaters;
//            framesToRead= sample.length;
//        }
//        @Override
//        public int read() throws IOException {
//            while (available() > 0)
//            {
//                idx &=1;
//                if(idx == 0)
//                {
//                    framesCounter++;
//
//                    readSample[0]=(char) (sample[cursor++]*Short.MAX_VALUE);
//                    readSample[1]=(char) ((int)(sample[cursor++]*Short.MAX_VALUE) >> 8);
//
//                }
//                return readSample[idx++];
//            }
//            return -1;
//        }
//
//        @Override
//        public int available()
//        {
//            return 2*((framesToRead-1)-framesCounter) + (2-(idx%2));
//        }
//
//        @Override
//        public void reset()
//        {
//            cursor = 0;
//            framesCounter = 0;
//            idx = 0;
//        }
//        @Override
//        public void close()
//        {
//            System.out.println(
//                    "Stopped after reading frames:"
//                            + framesCounter);
//        }
//    }
//
//}
