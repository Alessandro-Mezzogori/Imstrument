package imstrument.sound.algorithm;

import static org.lwjgl.openal.AL10.*;

/**
 * @author G223 Productions
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
