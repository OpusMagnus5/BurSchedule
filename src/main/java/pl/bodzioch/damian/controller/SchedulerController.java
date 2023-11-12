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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.bodzioch.damian.dto.client.*;
import pl.bodzioch.damian.exception.FileProcessingException;
import pl.bodzioch.damian.exception.SchedulerNotFoundException;
import pl.bodzioch.damian.mapper.ClientMapper;
import pl.bodzioch.damian.model.*;
import pl.bodzioch.damian.service.GenerateFileFromCreatedSchedulerService;
import pl.bodzioch.damian.service.GenerateFileService;
import pl.bodzioch.damian.service.SchedulerService;
import pl.bodzioch.damian.service.SecurityService;
import pl.bodzioch.damian.session.SessionBean;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

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
    private final SecurityService securityService;

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/{serviceId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<SchedulerListViewDTO> getScheduler(@PathVariable String serviceId) {
        List<SchedulerViewDTO> scheduler = schedulerService.getSchedulerForService(serviceId);
        return ResponseEntity.ok(new SchedulerListViewDTO(scheduler));
    }

    @PreAuthorize("hasAuthority('USER')")
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

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/generate")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<byte[]> generateFile(@Valid @RequestBody GenerateFileRequestDTO request) {
        List<SchedulerEntry> beginningsOfDays = schedulerService.getBeginningsOfDays(sessionBean.getScheduleEntries());
        validateNumberOfDays(beginningsOfDays.size(), request.getScheduleDays());
        Iterator<SchedulerEntry> iterator = beginningsOfDays.iterator();
        List<SchedulerGenerateDayParams> dayParams = request.getScheduleDays().stream()
                .map(day -> clientMapper.map(day, iterator.next()))
                .toList();
        byte[] fileBytes = generateFileService.generateFile(dayParams, sessionBean.getScheduleEntries());

        HttpHeaders headers = getHeadersToSendSchedulerFile();

        return ResponseEntity.ok()
                .headers(headers)
                .body(fileBytes);
    }

    @PreAuthorize("hasAuthority('USER')")
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

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<SaveSchedulerResponseDTO> save(@Valid @RequestBody SaveSchedulerRequestDTO request) {
        List<SchedulerCreateDayParams> daysParams = request.getDays().stream()
                .map(clientMapper::map)
                .toList();

        UUID id = request.getId()
                .map(securityService::decryptMessage)
                .map(UUID::fromString).orElse(null);
        SaveSchedulerParams params = SaveSchedulerParams.builder()
                .schedulerDays(daysParams)
                .schedulerName(request.getName())
                .id(id)
                .build();

        Scheduler scheduler = schedulerService.saveScheduler(params);
        List<String> encryptedIds = scheduler.getDays().stream()
                .map(SchedulerDay::getEntries)
                .flatMap(Collection::stream)
                .map(SchedulerEntry::getId)
                .map(UUID::toString)
                .map(securityService::encryptMessage)
                .collect(Collectors.toList());

        SaveSchedulerResponseDTO response = SaveSchedulerResponseDTO.builder()
                .schedulerId(securityService.encryptMessage(scheduler.getId().toString()))
                .entriesIds(encryptedIds)
                .message(messageSource.getMessage("save.scheduler.success", new String[]{request.getName()}, LocaleContextHolder.getLocale()))
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
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
