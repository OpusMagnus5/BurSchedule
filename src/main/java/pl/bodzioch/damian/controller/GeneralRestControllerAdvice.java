package pl.bodzioch.damian.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import pl.bodzioch.damian.model.ApiError;

import java.util.List;
import java.util.Locale;

@Order(3)
@RestControllerAdvice
@AllArgsConstructor
@Slf4j
public class GeneralRestControllerAdvice {

    private final MessageSource messageSource;
    private final Locale locale = LocaleContextHolder.getLocale();

    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ApiError> handleServerException(Exception ex, WebRequest webRequest) {
        log.error(ex.getMessage(), ex);
        ApiError apiError = ApiError.builder()
                .messages(List.of(messageSource.getMessage("general.error", null, locale)))
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
    }
}
