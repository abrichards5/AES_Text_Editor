package model.exception;

/**
 * Created by Alex on 12-Jul-17.
 */
public class DecryptFailedException extends Exception{
    public DecryptFailedException(String message) {
        super(message);
    }

    public DecryptFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public DecryptFailedException(Throwable cause) {
        super(cause);
    }
}
