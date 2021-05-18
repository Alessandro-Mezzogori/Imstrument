package imstrument.sound.openal;

import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;

import java.util.function.Supplier;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.ALC10.*;

/**
 * @author G223 Productions
 */
public class AudioThread extends Thread {
    public static final int BUFFER_SIZE = 1024;
    static final int BUFFER_COUNT = 8;
    public static final int SAMPLE_RATE = 44100;

    private final Supplier<short[]> bufferSupplier;
    private final int[] buffers = new int[BUFFER_COUNT];
    private final long device = alcOpenDevice(alcGetString(0, ALC_DEFAULT_DEVICE_SPECIFIER));
    private final long context = alcCreateContext(device, new int[1]);
    private final int source;

    private int bufferIndex;

    private boolean closed;
    private boolean running;

    public AudioThread(Supplier<short[]> bufferSupplier){
        this.bufferSupplier = bufferSupplier;
        alcMakeContextCurrent(context);
        AL.createCapabilities(ALC.createCapabilities(device));
        source = alGenSources();
        for(int i = 0; i < BUFFER_COUNT; i++){
            /*
             * new short[0] passes a dummy buffers so the buffers
             * get added up without external noise added
             */
            bufferSamples(new short[0]);
        }
        alSourcePlay(source);
        catchInternalException();
        start();
    }

    @Override
    public synchronized void run() {
        while(!closed){
            while(!running) {
                try{
                    wait();
                } catch(Exception e){
                    e.printStackTrace();
                }
            }
            int processsedBufs = alGetSourcei(source, AL_BUFFERS_PROCESSED);
            for(int i = 0; i < processsedBufs; i++){
                short[] samples = bufferSupplier.get();
                if(samples == null){
                    running = false;
                    break;
                }
                alDeleteBuffers(alSourceUnqueueBuffers(source));
                buffers[bufferIndex] = alGenBuffers();
                bufferSamples(samples);
            }
            if(alGetSourcei(source, AL_SOURCE_STATE) != AL_PLAYING){
                alSourcePlay(source);
            }
            catchInternalException();
        }
        alDeleteSources(source);
        alDeleteBuffers(buffers);
        alcDestroyContext(context);
        alcCloseDevice(device);
    }

    public boolean isRunning(){return running;}

    public synchronized void triggerPlayback(){
        running = true;
        notify();
    }

    public void close(){
        closed = true;
        // to exit loop
        triggerPlayback();
    }

    private void bufferSamples(short[] samples){
        int buf = buffers[bufferIndex++];
        alBufferData(buf, AL_FORMAT_MONO16, samples, SAMPLE_RATE);
        alSourceQueueBuffers(source, buf);
        bufferIndex %= BUFFER_COUNT;
    }

    private void catchInternalException(){
        int err = alcGetError(device);
        if(err != ALC_NO_ERROR){
            throw new OpenAlException(err);
        }
    }
}
