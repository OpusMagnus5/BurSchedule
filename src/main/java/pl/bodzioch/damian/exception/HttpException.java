package pl.bodzioch.damian.exception;

public abstract class HttpException extends RuntimeException {

    public HttpException(String message, Throwable cause) {
        super(message, cause);
    }
}
