package imstrument.sound.openal;

import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;

import java.util.function.Supplier;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.ALC10.*;

public class AudioThread extends Thread {
    /**
     * Buffer size a single stream of audio sample
     */
    public static final int BUFFER_SIZE = 1024;
    /**
     * Number of buffers created
     */
    static final int BUFFER_COUNT = 8;
    /**
     * sample rate of the audio
     */
    public static final int SAMPLE_RATE = 44100;

    private final Supplier<short[]> bufferSupplier;
    /**
     * array to store the buffers references
     */
    private final int[] buffers = new int[BUFFER_COUNT];
    /**
     * device used to playback the samples
     */
    private final long device = alcOpenDevice(alcGetString(0, ALC_DEFAULT_DEVICE_SPECIFIER));
    /**
     * context of the device
     */
    private final long context = alcCreateContext(device, new int[1]);

    /**
     * source of the audio
     */
    private final int source;

    private int bufferIndex;

    /**
     *  tells if the thread should close
     */
    private boolean closed;
    /**
     * tells if the thread is running
     */
    private boolean running;

    public AudioThread(Supplier<short[]> bufferSupplier){
        // save the buffer supplier that will retrieve the samples arrays
        this.bufferSupplier = bufferSupplier;
        // set the current context to the context of the device
        alcMakeContextCurrent(context);
        // set up the current device
        AL.createCapabilities(ALC.createCapabilities(device));
        // generate a single source of audio
        source = alGenSources();

        for(int i = 0; i < BUFFER_COUNT; i++){
            /*
             * new short[0] passes a dummy buffers so the buffers
             * get added up without external noise added
             */
            bufferSamples(new short[0]);
        }
        // sets which source is playing
        alSourcePlay(source);
        // error catching because OpenAL doesn't have classic exceptions
        catchInternalException();
        // start the thread execution loop
        start();
    }

    @Override
    public synchronized void run() {
        while(!closed){
            while(!running) {
                // if it's not running wait
                try{
                    wait();
                } catch(Exception e){
                    e.printStackTrace();
                }
            }
            // get the number of buffers to process
            int processsedBufs = alGetSourcei(source, AL_BUFFERS_PROCESSED);
            for(int i = 0; i < processsedBufs; i++){
                // retrieve a single array of samples
                short[] samples = bufferSupplier.get();
                if(samples == null){
                    // if there's no more samples stop running but don't close
                    running = false;
                    break;
                }
                // delete all the buffers
                alDeleteBuffers(alSourceUnqueueBuffers(source));
                // generate a new buffer and save the reference to the buffer index of buffers
                buffers[bufferIndex] = alGenBuffers();
                bufferSamples(samples);
            }
            // start playing if it's not doing it already
            if(alGetSourcei(source, AL_SOURCE_STATE) != AL_PLAYING){
                alSourcePlay(source);
            }
            // error catching
            catchInternalException();
        }
        // before the thread closes clean up the buffers, context and device
        alDeleteSources(source);
        alDeleteBuffers(buffers);
        alcDestroyContext(context);
        alcCloseDevice(device);
    }

    /**
     * tells if the thread is the running state
     * @return if it's running
     */
    public boolean isNotRunning(){return !running;}

    /**
     * start playing back the audio sent trought the buffer supplier
     */
    public synchronized void triggerPlayback(){
        running = true;
        notify();
    }

    /**
     * close the audio thread
     */
    public void close(){
        closed = true;
        // to exit loop
        triggerPlayback();
    }

    /**
     * set the samples to the current buffer index
     * @param samples audio samples
     */
    private void bufferSamples(short[] samples){
        // get the current buffer reference
        int buf = buffers[bufferIndex++];
        // fill the buffer with the samples
        alBufferData(buf, AL_FORMAT_MONO16, samples, SAMPLE_RATE);
        // queue the buffers to the source
        alSourceQueueBuffers(source, buf);
        // update the index to loop back if it needs to
        bufferIndex %= BUFFER_COUNT;
    }

    /**
     * Errore checking function
     */
    private void catchInternalException(){
        // gets a possibile error
        int err = alcGetError(device);
        // if there's an error then throw a custom exception
        if(err != ALC_NO_ERROR){
            throw new OpenAlException(err);
        }
    }
}
