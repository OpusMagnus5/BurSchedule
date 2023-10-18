package pl.bodzioch.damian.controller;

import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import pl.bodzioch.damian.exception.HttpClientException;
import pl.bodzioch.damian.model.ApiError;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
@AllArgsConstructor
public class CustomControllerAdvice {

    private final MessageSource messageSource;

    @ExceptionHandler({HttpClientException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiError> handleClientException(HttpClientException ex, WebRequest webRequest) {
        ApiError apiError = ApiError.builder()
                .messages(List.of(messageSource.getMessage("general.error", null, LocaleContextHolder.getLocale())))
                .build();

        return ResponseEntity.badRequest().body(apiError);
    }

    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ApiError> handleServerException(Exception ex, WebRequest webRequest) {
        ApiError apiError = ApiError.builder()
                .messages(List.of(messageSource.getMessage("general.error", null, LocaleContextHolder.getLocale())))
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiError> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest webRequest) {
        List<String> errorMessages = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> errorMessages.add(messageSource.getMessage(error.getDefaultMessage(),
                null, LocaleContextHolder.getLocale())));

        return ResponseEntity.badRequest().body(ApiError.builder()
                                    .messages(errorMessages)
                                    .build());
    }

    @ExceptionHandler({SecurityException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiError> handleSecurityException(SecurityException ex, WebRequest webRequest) {
        ApiError apiError = ApiError.builder()
                .messages(List.of(messageSource.getMessage(ex.getMessage(), null, LocaleContextHolder.getLocale())))
                .build();

        return ResponseEntity.badRequest().body(apiError);
    }
}
