package pl.bodzioch.damian.exception;

import jakarta.annotation.Nonnull;
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

    public AppException(@Nonnull String message, @Nonnull List<String> messageParams, @Nonnull HttpStatus httpStatus) {
        super(message);
        this.messageParams = messageParams;
        this.httpStatus = httpStatus;
    }

    public AppException(@Nonnull String message, @Nonnull List<String> messageParams, @Nonnull HttpStatus httpStatus, @Nonnull Throwable cause) {
        super(message, cause);
        this.messageParams = messageParams;
        this.httpStatus = httpStatus;
    }

    public AppException(@Nonnull Throwable cause) {
        super(cause);
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        this.messageParams = Collections.emptyList();
    }

    public static AppException getGeneralInternalError() {
        return new AppException("general.error", Collections.emptyList(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
