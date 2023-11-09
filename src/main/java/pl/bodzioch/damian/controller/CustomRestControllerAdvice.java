package pl.bodzioch.damian.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import pl.bodzioch.damian.exception.AppException;
import pl.bodzioch.damian.exception.HttpClientException;
import pl.bodzioch.damian.model.ApiError;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Order(1)
@RestControllerAdvice
@AllArgsConstructor
@Slf4j
public class CustomRestControllerAdvice {

    private final MessageSource messageSource;
    private final Locale locale = LocaleContextHolder.getLocale();


    @ExceptionHandler({HttpClientException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiError> handleClientException(HttpClientException ex, WebRequest webRequest) {
        log.error(ex.getMessage(), ex);
        ApiError apiError = ApiError.builder()
                .messages(List.of(messageSource.getMessage("general.error", null, locale)))
                .build();

        return ResponseEntity.badRequest().body(apiError);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiError> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest webRequest) {
        log.error(ex.getMessage(), ex);
        List<String> errorMessages = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> errorMessages.add(messageSource.getMessage(error.getDefaultMessage(),
                null, locale)));

        return ResponseEntity.badRequest().body(ApiError.builder()
                                    .messages(errorMessages)
                                    .build());
    }

    @ExceptionHandler({SecurityException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiError> handleSecurityException(SecurityException ex, WebRequest webRequest) {
        log.error(ex.getMessage(), ex);
        ApiError apiError = ApiError.builder()
                .messages(List.of(messageSource.getMessage(ex.getMessage(), null, locale)))
                .build();

        return ResponseEntity.badRequest().body(apiError);
    }

    @ExceptionHandler({AppException.class})
    public ResponseEntity<ApiError> handleAppException(AppException ex, WebRequest webRequest) {
        log.warn("Error message: {}", ex.getMessage(), ex);
        ApiError apiError = ApiError.builder()
                .messages(List.of(messageSource.getMessage(ex.getMessage(), ex.getMessageParams().toArray(), locale)))
                .build();

        return ResponseEntity.status(ex.getHttpStatus()).body(apiError);
    }
}
