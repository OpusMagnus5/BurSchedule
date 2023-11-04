package pl.bodzioch.damian.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Order(2)
@ControllerAdvice
@Slf4j
public class CustomControllerAdvice {

    @ExceptionHandler({AccessDeniedException.class})
    public String handleAccessDeniedException(AccessDeniedException ex) {
        log.warn("Access Denied!", ex);
        return "redirect:/login";
    }
}
