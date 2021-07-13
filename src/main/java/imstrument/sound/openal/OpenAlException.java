package imstrument.sound.openal;

import static org.lwjgl.openal.AL10.*;

/**
 * used for error checking in OpenAL because it's error checking
 * doesn't use exceptino but it's more of a C style error catching
 */
public class OpenAlException extends RuntimeException{
    OpenAlException(int errorCode){
        super(
            "Internal " +
                (
                errorCode == AL_INVALID_NAME ? "invalid name" :
                errorCode == AL_INVALID_ENUM ? "invalid enum" :
                errorCode == AL_INVALID_VALUE ? "invalid valued" :
                errorCode == AL_INVALID_OPERATION ? "invalid operation" :
                "unknown"
                )
            + " OpenAL exception"
        );
    }
}
