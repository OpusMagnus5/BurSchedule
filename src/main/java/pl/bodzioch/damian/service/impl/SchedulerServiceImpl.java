package pl.bodzioch.damian.service.impl;

import lombok.AllArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.BOMInputStream;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pl.bodzioch.damian.client.BurClient;
import pl.bodzioch.damian.dao.SchedulerDAO;
import pl.bodzioch.damian.dto.client.SchedulerViewDTO;
import pl.bodzioch.damian.entity.SchedulerDbEntity;
import pl.bodzioch.damian.exception.AppException;
import pl.bodzioch.damian.mapper.ClientMapper;
import pl.bodzioch.damian.mapper.EntityMapper;
import pl.bodzioch.damian.model.*;
import pl.bodzioch.damian.service.SchedulerService;
import pl.bodzioch.damian.service.SecurityService;
import pl.bodzioch.damian.session.SessionBean;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class SchedulerServiceImpl implements SchedulerService {

    private final BurClient burClient;
    private final SecurityService securityService;
    private final ClientMapper clientMapper;
    private final SessionBean sessionBean;
    private final SchedulerDAO schedulerDAO;

    @Override
    public List<SchedulerViewDTO> getSchedulerForService(String serviceId) {
        String encryptedId = securityService.decryptMessage(serviceId);
        Optional<SchedulerModel> scheduler = burClient.getScheduleForService(Long.parseLong(encryptedId));
        if (scheduler.isEmpty()) {
            throw new AppException("scheduler.not.found", Collections.emptyList(), HttpStatus.NOT_FOUND);
        }

        scheduler.ifPresent(sessionBean::setScheduler);

        return scheduler.map(SchedulerModel::getDays)
                .map(days -> days.stream()
                        .map(clientMapper::mapToSchedulerView)
                        .toList())
                .orElseThrow(AppException::getGeneralInternalError);
    }

    @Override
    public List<SchedulerViewDTO> getSchedulerFromFile(InputStream inputStream) throws IOException {
        BOMInputStream streamWithoutBOM = getStreamWithoutBOM(inputStream);
        List<String> lines = IOUtils.readLines(streamWithoutBOM, StandardCharsets.UTF_8);
        lines.remove(0);
        SchedulerModel scheduler = mapToScheduler(lines);
        sessionBean.setScheduler(scheduler);

        return scheduler.getDays().stream()
                        .map(clientMapper::mapToSchedulerView)
                        .toList();
    }

    @Override
    public SchedulerModel saveScheduler(SchedulerModel scheduler) {
        SchedulerDbEntity schedulerDbEntity = EntityMapper.map(scheduler, sessionBean.getUser()
                .orElseThrow(AppException::getGeneralInternalError));

        if (scheduler.getId().isPresent()) {
            return schedulerDAO.saveScheduler(schedulerDbEntity);
        }
        return saveSchedulerIfNotExists(schedulerDbEntity);
    }

    @Override
    public List<SchedulerInfo> getAllSchedulers() {
        return schedulerDAO.getAllSchedulersInfo();
    }

    @Override
    public SchedulerModel getScheduler(UUID id) {
        return schedulerDAO.getScheduler(id);
    }

    @Override
    public List<SchedulerEntry> getBeginningsOfDays(SchedulerModel scheduler) {
       return scheduler.getDays().stream()
                .map(SchedulerDay::getEntries)
                .map(entries -> entries.get(0))
                .toList();
    }

    private SchedulerModel saveSchedulerIfNotExists(SchedulerDbEntity schedulerDbEntity) {
        String schedulerName = schedulerDbEntity.getName();
        try {
            schedulerDAO.getByName(schedulerName);
        } catch (AppException ex) {
            return schedulerDAO.saveScheduler(schedulerDbEntity);
        }
        throw new AppException("save.scheduler.exists", List.of(schedulerName), HttpStatus.BAD_REQUEST);
    }

    private BOMInputStream getStreamWithoutBOM(InputStream inputStream) throws IOException {
        return BOMInputStream.builder()
                .setInputStream(inputStream)
                .setInclude(false)
                .get();
    }

    private SchedulerModel mapToScheduler(List<String> lines) {
        Set<Map.Entry<String, List<List<MatchResult>>>> dateToResults = getResultsGroupedByDay(lines);

        List<SchedulerDay> days = dateToResults.stream()
                .map(this::mapToDay)
                .sorted(Comparator.comparing(SchedulerDay::getDate))
                .toList();

        return SchedulerModel.builder()
                .days(days)
                .build();
    }

    private SchedulerDay mapToDay(Map.Entry<String, List<List<MatchResult>>> entry) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        return SchedulerDay.builder()
                .date(LocalDate.parse(entry.getValue()
                        .get(0).get(2).group(), dateFormatter))
                .entries(entry.getValue().stream()
                        .map(this::mapToDateLine)
                        .sorted(Comparator.comparing(SchedulerEntry::getStartTime))
                        .toList())
                .build();
    }

    private  Set<Map.Entry<String, List<List<MatchResult>>>> getResultsGroupedByDay(List<String> lines) {
        Pattern pattern = Pattern.compile("[^;\"\\n]+");

        return lines.stream()
                .map(pattern::matcher)
                .map(Matcher::results)
                .map(Stream::toList)
                .collect(Collectors.groupingBy(list -> list.get(2).group()))
                .entrySet();
    }

    private SchedulerEntry mapToDateLine(List<MatchResult> results) {
        DateTimeFormatter dimeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        

        return SchedulerEntry.builder()
                .subject(results.get(0).group())
                .startTime(LocalTime.parse(results.get(3).group(), dimeFormatter))
                .endTime(LocalTime.parse(results.get(4).group(), dimeFormatter))
                .build();
    }
}
