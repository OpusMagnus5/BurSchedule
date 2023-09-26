package pl.bodzioch.damian.exception;

public class HttpClientException extends HttpException {

    public HttpClientException() {
    }

    public HttpClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
