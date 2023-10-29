package pl.bodzioch.damian.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;

@Getter
public class AppException extends RuntimeException {

    private final List<String> messageParams;
    private final HttpStatus httpStatus;

    public AppException() {
        this.messageParams = Collections.emptyList();
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public AppException(String message, List<String> messageParams, HttpStatus httpStatus) {
        super(message);
        this.messageParams = messageParams;
        this.httpStatus = httpStatus;
    }

    public AppException(String message, List<String> messageParams, HttpStatus httpStatus, Throwable cause) {
        super(message, cause);
        this.messageParams = messageParams;
        this.httpStatus = httpStatus;
    }

    public AppException(Throwable cause) {
        super(cause);
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        this.messageParams = Collections.emptyList();
    }
}
