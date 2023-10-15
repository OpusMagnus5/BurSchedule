package pl.bodzioch.damian.controller;

import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.bodzioch.damian.dto.client.SchedulerListViewDTO;
import pl.bodzioch.damian.dto.client.SchedulerViewDTO;
import pl.bodzioch.damian.exception.SchedulerNotFoundException;
import pl.bodzioch.damian.model.ApiError;
import pl.bodzioch.damian.service.SchedulerService;

import java.util.List;

@RestController
@RequestMapping("/app/scheduler")
@AllArgsConstructor
public class SchedulerController {

    private final MessageSource messageSource;
    private final SchedulerService schedulerService;

    @GetMapping("/{serviceId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<SchedulerListViewDTO> getScheduler(@PathVariable String serviceId) {
        List<SchedulerViewDTO> scheduler = schedulerService.getSchedulerForService(serviceId);
        return ResponseEntity.ok(new SchedulerListViewDTO(scheduler));
    }

    @ExceptionHandler(SchedulerNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ApiError> handleServicesNotFound(SchedulerNotFoundException ex) {
        ApiError response = ApiError.builder()
                .message(messageSource.getMessage("scheduler.not.found", new Object[]{}, LocaleContextHolder.getLocale()))
                .build();
        return ResponseEntity.ofNullable(response);
    }
}
