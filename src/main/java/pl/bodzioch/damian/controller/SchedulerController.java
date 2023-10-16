package pl.bodzioch.damian.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.bodzioch.damian.dto.client.GenerateFileRequestDTO;
import pl.bodzioch.damian.dto.client.SchedulerDayDTO;
import pl.bodzioch.damian.dto.client.SchedulerListViewDTO;
import pl.bodzioch.damian.dto.client.SchedulerViewDTO;
import pl.bodzioch.damian.exception.SchedulerNotFoundException;
import pl.bodzioch.damian.mapper.ClientMapper;
import pl.bodzioch.damian.model.ApiError;
import pl.bodzioch.damian.model.ScheduleEntry;
import pl.bodzioch.damian.model.SchedulerDayParams;
import pl.bodzioch.damian.service.GenerateFileService;
import pl.bodzioch.damian.service.SchedulerService;
import pl.bodzioch.damian.session.SessionBean;

import java.util.Iterator;
import java.util.List;

@RestController
@RequestMapping("/app/scheduler")
@AllArgsConstructor
public class SchedulerController {

    private final MessageSource messageSource;
    private final SchedulerService schedulerService;
    private final GenerateFileService generateFileService;
    private final SessionBean sessionBean;
    private final ClientMapper clientMapper;

    @GetMapping("/{serviceId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<SchedulerListViewDTO> getScheduler(@PathVariable String serviceId) {
        List<SchedulerViewDTO> scheduler = schedulerService.getSchedulerForService(serviceId);
        sessionBean.setNumberOfDaysInScheduler(scheduler.size());
        return ResponseEntity.ok(new SchedulerListViewDTO(scheduler));
    }

    @PostMapping("/generate")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<byte[]> generateFile(@Valid @RequestBody GenerateFileRequestDTO request) {
        validateNumberOfDays(request.getScheduleDays());
        Iterator<ScheduleEntry> iterator = sessionBean.getScheduleEntries().iterator();
        List<SchedulerDayParams> dayParams = request.getScheduleDays().stream()
                .map(day -> clientMapper.map(day, iterator.next()))
                .toList();
        byte[] fileBytes = generateFileService.generateFile(dayParams, sessionBean.getScheduleEntries());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "harmonogram.csv");

        return ResponseEntity.ok()
                .headers(headers)
                .body(fileBytes);
    }

    private void validateNumberOfDays(List<SchedulerDayDTO> schedulerDays) {
        if (sessionBean.getNumberOfDaysInScheduler() != schedulerDays.size()) {
            throw new SecurityException("scheduler.generate.scheduleDays.size");
        }
    }

    @ExceptionHandler(SchedulerNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ApiError> handleServicesNotFound(SchedulerNotFoundException ex) {
        ApiError response = ApiError.builder()
                .messages(List.of(messageSource.getMessage("scheduler.not.found", new Object[]{}, LocaleContextHolder.getLocale())))
                .build();
        return ResponseEntity.ofNullable(response);
    }
}
