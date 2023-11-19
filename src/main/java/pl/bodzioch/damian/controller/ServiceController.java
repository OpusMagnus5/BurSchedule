package pl.bodzioch.damian.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.bodzioch.damian.dto.client.ProviderListViewDTO;
import pl.bodzioch.damian.dto.client.ServiceListViewDTO;
import pl.bodzioch.damian.dto.client.ServiceStatusListViewDTO;
import pl.bodzioch.damian.model.ServiceProvider;
import pl.bodzioch.damian.model.ServiceStatus;
import pl.bodzioch.damian.service.ServiceForBurClient;
import pl.bodzioch.damian.service.ServicesService;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/app/services")
@AllArgsConstructor
@Slf4j
public class ServiceController {

    private final ServiceForBurClient serviceForBurClient;
    private final ServicesService servicesService;
    private final MessageSource messageSource;

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/synchronization")
    public HttpStatus synchronize() {
        serviceForBurClient.synchronizeServices();
        return HttpStatus.OK;
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("")
    public ResponseEntity<ServiceListViewDTO> getAllServices() {
        return ResponseEntity.ok(servicesService.getAllServices());
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/providers")
    public ResponseEntity<ProviderListViewDTO> getServiceProviders() {
        List<String> providers = Arrays.stream(ServiceProvider.values())
                .map(ServiceProvider::getName)
                .toList();

        return ResponseEntity.ok(new ProviderListViewDTO(providers));
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/statuses")
    public ResponseEntity<ServiceStatusListViewDTO> getServiceStatuses() {
        List<String> statuses = Arrays.stream(ServiceStatus.values())
                .map(ServiceStatus::getCode)
                .map(code -> messageSource.getMessage("service.status." + code, null, LocaleContextHolder.getLocale()))
                .toList();

        return ResponseEntity.ok(new ServiceStatusListViewDTO(statuses));
    }
}
