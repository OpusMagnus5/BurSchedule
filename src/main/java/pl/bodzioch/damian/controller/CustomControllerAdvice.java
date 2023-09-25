package pl.bodzioch.damian.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import pl.bodzioch.damian.exception.HttpClientException;
import pl.bodzioch.damian.model.ApiError;

import java.util.Optional;

@RestControllerAdvice
public class CustomControllerAdvice {

    private final MessageSource messageSource;

    @Autowired
    public CustomControllerAdvice(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler({HttpClientException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiError> handleException(Exception ex, WebRequest webRequest) {
        ApiError apiError = ApiError.builder()
                .message(messageSource.getMessage("http.client.error", new Object[]{}, LocaleContextHolder.getLocale()))
                .build();

        return ResponseEntity.of(Optional.of(apiError));
    }
}
