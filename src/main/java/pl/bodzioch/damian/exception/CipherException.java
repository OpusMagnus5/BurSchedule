package pl.bodzioch.damian.exception;

public class CipherException extends RuntimeException {

    public CipherException() {
    }

    public CipherException(String message, Throwable cause) {
        super(message, cause);
    }
}
