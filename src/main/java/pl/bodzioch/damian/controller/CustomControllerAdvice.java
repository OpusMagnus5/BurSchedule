package pl.bodzioch.damian.controller;

import org.springframework.beans.factory.annotation.Autowired;
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
public class CustomControllerAdvice {

    private final MessageSource messageSource;

    @Autowired
    public CustomControllerAdvice(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler({HttpClientException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiError> handleClientException(HttpClientException ex, WebRequest webRequest) {
        ApiError apiError = ApiError.builder()
                .messages(List.of(messageSource.getMessage("general.error", null, LocaleContextHolder.getLocale())))
                .build();

        return ResponseEntity.ofNullable(apiError);
    }

    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ApiError> handleServerException(Exception ex, WebRequest webRequest) {
        ApiError apiError = ApiError.builder()
                .messages(List.of(messageSource.getMessage("general.error", null, LocaleContextHolder.getLocale())))
                .build();

        return ResponseEntity.ofNullable(apiError);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiError> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest webRequest) {
        List<String> errorMessages = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> errorMessages.add(error.getField() + ": " +
                messageSource.getMessage(error.getDefaultMessage(), null, LocaleContextHolder.getLocale())));

        return ResponseEntity.ok(ApiError.builder()
                                    .messages(errorMessages)
                                    .build());
    }
}
