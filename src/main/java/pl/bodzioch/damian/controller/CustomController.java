package pl.bodzioch.damian.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.bodzioch.damian.exception.ServicesNotFoundException;
import pl.bodzioch.damian.model.ApiError;
import pl.bodzioch.damian.service.ServiceForBurClient;

@RestController
@RequestMapping("/app")
public class CustomController {

    private final ServiceForBurClient serviceForBurClient;
    private final MessageSource messageSource;

    @Autowired
    public CustomController(ServiceForBurClient serviceForBurClient, MessageSource messageSource) {
        this.serviceForBurClient = serviceForBurClient;
        this.messageSource = messageSource;
    }

    @GetMapping("/services")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getAllServices() {
        serviceForBurClient.synchronizeServices();
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(ServicesNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ApiError> handleServicesNotFound(ServicesNotFoundException ex) {
        ApiError response = ApiError.builder()
                .message(messageSource.getMessage("services.not.found", new Object[]{}, LocaleContextHolder.getLocale()))
                .build();
        return ResponseEntity.ofNullable(response);
    }
}
