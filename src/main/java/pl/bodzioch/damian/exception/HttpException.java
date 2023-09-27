package pl.bodzioch.damian.exception;

public abstract class HttpException extends RuntimeException {

    public HttpException() {
    }

    public HttpException(String message, Throwable cause) {
        super(message, cause);
    }
}
