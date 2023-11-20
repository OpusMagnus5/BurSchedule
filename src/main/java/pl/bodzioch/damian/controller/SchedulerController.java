package pl.bodzioch.damian.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
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
import pl.bodzioch.damian.exception.AppException;
import pl.bodzioch.damian.mapper.ClientMapper;
import pl.bodzioch.damian.model.*;
import pl.bodzioch.damian.service.GenerateSchedulerFileService;
import pl.bodzioch.damian.service.SchedulerService;
import pl.bodzioch.damian.service.SecurityService;
import pl.bodzioch.damian.session.SessionBean;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/app/scheduler")
@AllArgsConstructor
public class SchedulerController {

    private final MessageSource messageSource;
    private final SchedulerService schedulerService;
    private final GenerateSchedulerFileService generateSchedulerFileService;
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
    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<SchedulerListResponseViewDTO> getAllScheduler() {
        List<SchedulerInfo> schedulers = schedulerService.getAllSchedulers();
        SchedulerListResponseViewDTO response = clientMapper.mapToSchedulerListResponse(schedulers);

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/file")
    public ResponseEntity<SchedulerListViewDTO> getSchedulerFromFile(@RequestParam("file") MultipartFile file) {
        List<SchedulerViewDTO> scheduler;
        try {
            scheduler = schedulerService.getSchedulerFromFile(file.getInputStream());
        } catch (IOException ex) {
            throw new AppException("scheduler.file.processing.error", Collections.emptyList(), HttpStatus.INTERNAL_SERVER_ERROR, ex);
        }
        return ResponseEntity.ok(new SchedulerListViewDTO(scheduler));
    }

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/generate")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<byte[]> generateFile(@Valid @RequestBody GenerateFileRequestDTO request) {
        SchedulerModel sessionScheduler = sessionBean.getScheduler()
                .orElseThrow(AppException::getGeneralInternalError);
        List<SchedulerEntry> beginningsOfDays = schedulerService.getBeginningsOfDays(sessionScheduler);
        validateNumberOfDays(beginningsOfDays.size(), request.getScheduleDays());
        Iterator<SchedulerEntry> iterator = beginningsOfDays.iterator();
        List<ModifiedSchedulerFromServiceParams> dayParams = request.getScheduleDays().stream()
                .map(day -> clientMapper.map(day, iterator.next()))
                .toList();
        byte[] fileBytes = generateSchedulerFileService.generateFile(dayParams, sessionScheduler);

        HttpHeaders headers = getHeadersToSendSchedulerFile();

        return ResponseEntity.ok()
                .headers(headers)
                .body(fileBytes);
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/generate")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<byte[]> generateById(@RequestParam String id) {
        UUID uuid = UUID.fromString(securityService.decryptMessage(id));
        SchedulerModel scheduler = schedulerService.getScheduler(uuid);

        byte[] fileBytes = generateSchedulerFileService.generateFile(scheduler.getDays());

        return ResponseEntity.ok()
                .headers(getHeadersToSendSchedulerFile())
                .body(fileBytes);
    }

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<byte[]> createScheduler(@Valid @RequestBody CreateSchedulerRequestDTO request) {
        List<SchedulerDay> days = request.getDays().stream()
                .map(clientMapper::map)
                .toList();

        byte[] fileBytes = generateSchedulerFileService.generateFile(days);

        return ResponseEntity.ok()
                .headers(getHeadersToSendSchedulerFile())
                .body(fileBytes);
    }

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<SaveSchedulerResponseDTO> save(@Valid @RequestBody SaveSchedulerRequestDTO request) {
        SchedulerModel schedulerModel = map(request);

        SchedulerModel scheduler = schedulerService.saveScheduler(schedulerModel);

        List<String> encryptedIds = scheduler.getDays().stream()
                .map(SchedulerDay::getEntries)
                .flatMap(Collection::stream)
                .map(SchedulerEntry::getId)
                .flatMap(Optional::stream)
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

    private SchedulerModel map(SaveSchedulerRequestDTO request) {
        List<SchedulerDay> daysParams = request.getDays().stream()
                .map(clientMapper::map)
                .toList();

        UUID id = request.getId()
                .map(securityService::decryptMessage)
                .map(UUID::fromString).orElse(null);

        return SchedulerModel.builder()
                .days(daysParams)
                .name(request.getName())
                .id(id)
                .build();
    }

    private HttpHeaders getHeadersToSendSchedulerFile() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "harmonogram.csv");
        return headers;
    }

    private void validateNumberOfDays(Integer days, List<SchedulerDayDTO> schedulerDays) {
        if (days != schedulerDays.size()) {
            throw new AppException("scheduler.generate.scheduleDays.size", Collections.emptyList(), HttpStatus.BAD_REQUEST);
        }
    }
}
