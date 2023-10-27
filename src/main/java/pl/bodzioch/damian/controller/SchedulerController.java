package pl.bodzioch.damian.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.bodzioch.damian.dto.client.*;
import pl.bodzioch.damian.exception.FileProcessingException;
import pl.bodzioch.damian.exception.SchedulerNotFoundException;
import pl.bodzioch.damian.mapper.ClientMapper;
import pl.bodzioch.damian.model.ApiError;
import pl.bodzioch.damian.model.ScheduleEntry;
import pl.bodzioch.damian.model.SchedulerCreateDayParams;
import pl.bodzioch.damian.model.SchedulerGenerateDayParams;
import pl.bodzioch.damian.service.GenerateFileFromCreatedSchedulerService;
import pl.bodzioch.damian.service.GenerateFileService;
import pl.bodzioch.damian.service.SchedulerService;
import pl.bodzioch.damian.session.SessionBean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RestController
@RequestMapping("/app/scheduler")
@AllArgsConstructor
@Slf4j
public class SchedulerController {

    private final MessageSource messageSource;
    private final SchedulerService schedulerService;
    private final GenerateFileService generateFileService;
    private final GenerateFileFromCreatedSchedulerService generateFileFromCreatedSchedulerService;
    private final SessionBean sessionBean;
    private final ClientMapper clientMapper;

    @GetMapping("/{serviceId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<SchedulerListViewDTO> getScheduler(@PathVariable String serviceId) {
        List<SchedulerViewDTO> scheduler = schedulerService.getSchedulerForService(serviceId);
        return ResponseEntity.ok(new SchedulerListViewDTO(scheduler));
    }

    @PostMapping("/file")
    public ResponseEntity<SchedulerListViewDTO> getSchedulerFromFile(@RequestParam("file") MultipartFile file) {
        List<SchedulerViewDTO> scheduler = new ArrayList<>();
        try {
            scheduler = schedulerService.getSchedulerForService(file.getInputStream());
        } catch (IOException ex) {
            log.error(ex.getMessage(), ex);
            throw new FileProcessingException(ex);
        }
        return ResponseEntity.ok(new SchedulerListViewDTO(scheduler));
    }

    @PostMapping("/generate")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<byte[]> generateFile(@Valid @RequestBody GenerateFileRequestDTO request) {
        List<ScheduleEntry> beginningsOfDays = schedulerService.getBeginningsOfDays(sessionBean.getScheduleEntries());
        validateNumberOfDays(beginningsOfDays.size(), request.getScheduleDays());
        Iterator<ScheduleEntry> iterator = beginningsOfDays.iterator();
        List<SchedulerGenerateDayParams> dayParams = request.getScheduleDays().stream()
                .map(day -> clientMapper.map(day, iterator.next()))
                .toList();
        byte[] fileBytes = generateFileService.generateFile(dayParams, sessionBean.getScheduleEntries());

        HttpHeaders headers = getHeadersToSendSchedulerFile();

        return ResponseEntity.ok()
                .headers(headers)
                .body(fileBytes);
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<byte[]> createScheduler(@Valid @RequestBody CreateSchedulerRequestDTO request) {
        List<SchedulerCreateDayParams> daysParams = request.getDays().stream()
                .map(clientMapper::map)
                .toList();

        byte[] fileBytes = generateFileFromCreatedSchedulerService.generateFile(daysParams);

        return ResponseEntity.ok()
                .headers(getHeadersToSendSchedulerFile())
                .body(fileBytes);
    }

    private HttpHeaders getHeadersToSendSchedulerFile() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "harmonogram.csv");
        return headers;
    }

    private void validateNumberOfDays(Integer days, List<SchedulerDayDTO> schedulerDays) {
        if (days != schedulerDays.size()) {
            throw new SecurityException("scheduler.generate.scheduleDays.size");
        }
    }

    @ExceptionHandler(SchedulerNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ApiError> handleServicesNotFound(SchedulerNotFoundException ex) {
        log.error(ex.getMessage(), ex);
        ApiError response = ApiError.builder()
                .messages(List.of(messageSource.getMessage("scheduler.not.found", null, LocaleContextHolder.getLocale())))
                .build();
        return ResponseEntity.ofNullable(response);
    }

    @ExceptionHandler(FileProcessingException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ApiError> handleFileProcessingException(FileProcessingException ex) {
        log.error(ex.getMessage(), ex);
        ApiError response = ApiError.builder()
                .messages(List.of(messageSource.getMessage("scheduler.file.processing.error", null, LocaleContextHolder.getLocale())))
                .build();
        return ResponseEntity.ofNullable(response);
    }
}
