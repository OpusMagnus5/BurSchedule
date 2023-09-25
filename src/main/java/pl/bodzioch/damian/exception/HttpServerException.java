package pl.bodzioch.damian.exception;

public class HttpServerException extends HttpException {

    public HttpServerException(String message, Throwable cause) {
        super(message, cause);
    }
}
