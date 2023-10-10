package pl.bodzioch.damian.controller;

import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.bodzioch.damian.dto.client.ServiceListViewDTO;
import pl.bodzioch.damian.exception.ServicesNotFoundException;
import pl.bodzioch.damian.model.ApiError;
import pl.bodzioch.damian.service.ServiceForBurClient;
import pl.bodzioch.damian.service.ServicesService;

@RestController
@RequestMapping("/app/services")
@AllArgsConstructor
public class CustomController {

    private final ServiceForBurClient serviceForBurClient;
    private final ServicesService servicesService;
    private final MessageSource messageSource;

    @GetMapping("/synchronization")
    @ResponseStatus(HttpStatus.OK)
    public void synchronize() {
        serviceForBurClient.synchronizeServices();
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ServiceListViewDTO> getAllServices() {
        return ResponseEntity.ok(servicesService.getAllServices());
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
