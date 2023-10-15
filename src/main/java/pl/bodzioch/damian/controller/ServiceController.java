package pl.bodzioch.damian.controller;

import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.bodzioch.damian.dto.client.ProviderListViewDTO;
import pl.bodzioch.damian.dto.client.ServiceListViewDTO;
import pl.bodzioch.damian.dto.client.ServiceStatusListViewDTO;
import pl.bodzioch.damian.exception.ServicesNotFoundException;
import pl.bodzioch.damian.model.ApiError;
import pl.bodzioch.damian.model.ServiceProvider;
import pl.bodzioch.damian.model.ServiceStatus;
import pl.bodzioch.damian.service.ServiceForBurClient;
import pl.bodzioch.damian.service.ServicesService;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/app/services")
@AllArgsConstructor
public class ServiceController {

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

    @GetMapping("/providers")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ProviderListViewDTO> getServiceProviders() {
        List<String> providers = Arrays.stream(ServiceProvider.values())
                .map(ServiceProvider::getName)
                .toList();

        return ResponseEntity.ok(new ProviderListViewDTO(providers));
    }

    @GetMapping("/statuses")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ServiceStatusListViewDTO> getServiceStatuses() {
        List<String> statuses = Arrays.stream(ServiceStatus.values())
                .map(ServiceStatus::getCode)
                .map(code -> messageSource.getMessage("service.status." + code, null, LocaleContextHolder.getLocale()))
                .toList();

        return ResponseEntity.ok(new ServiceStatusListViewDTO(statuses));
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
