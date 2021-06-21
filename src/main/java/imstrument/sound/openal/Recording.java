package imstrument.sound.openal;

import imstrument.start.StartApp;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class Recording {
    public boolean isRecording = false;
    private final byte[] byteArray;
    public static final File recordingFolder = new File(StartApp.defaultFolder.toString() + "/recordings/");
    File newRecord;

    static {
        if (!recordingFolder.exists()) {
            boolean mkdir = recordingFolder.mkdir(); // TODO notify user if default folder is not created
        }
    }

    public Recording() {
        byteArray = new byte[AudioThread.BUFFER_SIZE * Short.BYTES];
    }

    public void record(short[] input) throws IOException {

        int short_index, byte_index;
        int iterations = input.length;

        byte[] buffer = new byte[input.length * 2];

        short_index = byte_index = 0;

        for (; short_index != iterations; ) {
            buffer[byte_index] = (byte) (input[short_index] & 0x00FF);
            buffer[byte_index + 1] = (byte) ((input[short_index] & 0xFF00) >> 8);

            ++short_index;
            byte_index += 2;
        }
        InputStream byteArrayInputStream = new ByteArrayInputStream(buffer);
        AudioFormat format = new AudioFormat(8000f, 16, 1, true, false);
        AudioInputStream audioSample = new AudioInputStream(byteArrayInputStream, format, buffer.length);
        AudioSystem.write(audioSample, AudioFileFormat.Type.WAVE, newRecord);
    }

    public void startRecording() {
        newRecord = new File(StartApp.defaultFolder.toString() + "/recordings/" + "NewRecords_" + java.time.LocalTime.now() + ".wav");
        isRecording = true;
    }

    public void stopRecording() {
        isRecording = false;
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
