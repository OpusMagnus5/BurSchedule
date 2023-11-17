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
import pl.bodzioch.damian.exception.SchedulerNotFoundException;
import pl.bodzioch.damian.mapper.ClientMapper;
import pl.bodzioch.damian.mapper.EntityMapper;
import pl.bodzioch.damian.model.SaveSchedulerParams;
import pl.bodzioch.damian.model.Scheduler;
import pl.bodzioch.damian.model.SchedulerEntry;
import pl.bodzioch.damian.model.SchedulerInfo;
import pl.bodzioch.damian.service.SchedulerService;
import pl.bodzioch.damian.service.SecurityService;
import pl.bodzioch.damian.session.SessionBean;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
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
        List<SchedulerEntry> scheduler = burClient.getScheduleForService(Long.parseLong(encryptedId));
        if (scheduler.isEmpty()) {
            throw new SchedulerNotFoundException();
        }

        sortScheduler(scheduler);
        sessionBean.setScheduleEntries(scheduler);

        return getBeginningsOfDays(scheduler).stream()
                .map(clientMapper::map)
                .toList();
    }

    @Override
    public List<SchedulerViewDTO> getSchedulerFromFile(InputStream inputStream) throws IOException {
        BOMInputStream streamWithoutBOM = getStreamWithoutBOM(inputStream);
        List<String> lines = IOUtils.readLines(streamWithoutBOM, StandardCharsets.UTF_8);
        lines.remove(0);
        List<SchedulerEntry> scheduleEntries = mapToScheduleEntries(lines);
        sortScheduler(scheduleEntries);
        sessionBean.setScheduleEntries(scheduleEntries);

        return getBeginningsOfDays(scheduleEntries).stream()
                .map(clientMapper::map)
                .toList();
    }

    @Override
    public Scheduler saveScheduler(SaveSchedulerParams params) {
        SchedulerDbEntity schedulerDbEntity = EntityMapper.map(params);

        if (params.getId().isPresent()) {
            return schedulerDAO.saveScheduler(schedulerDbEntity);
        }
        return saveSchedulerIfNotExists(schedulerDbEntity);
    }

    @Override
    public List<SchedulerInfo> getAllSchedulers() {
        return schedulerDAO.getAllSchedulersInfo();
    }

    @Override
    public Scheduler getScheduler(UUID id) {
        return schedulerDAO.getScheduler(id);
    }

    @Override
    public List<SchedulerEntry> getBeginningsOfDays(List<SchedulerEntry> scheduler) {
        return scheduler.stream()
                .filter(distinctByKey(SchedulerEntry::getDate))
                .toList();
    }

    private Scheduler saveSchedulerIfNotExists(SchedulerDbEntity schedulerDbEntity) {
        String schedulerName = schedulerDbEntity.getName();
        try {
            schedulerDAO.getByName(schedulerName);
        } catch (AppException ex) {
            return schedulerDAO.saveScheduler(schedulerDbEntity);
        }
        throw new AppException("save.scheduler.exists", List.of(schedulerName), HttpStatus.BAD_REQUEST);
    }

    private void sortScheduler(List<SchedulerEntry> scheduler) {
        scheduler.sort(Comparator.comparing(SchedulerEntry::getDate).thenComparing(SchedulerEntry::getStartTime));
    }

    public <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    private BOMInputStream getStreamWithoutBOM(InputStream inputStream) throws IOException {
        return BOMInputStream.builder()
                .setInputStream(inputStream)
                .setInclude(false)
                .get();
    }

    private List<SchedulerEntry> mapToScheduleEntries(List<String> lines) {
        Pattern pattern = Pattern.compile("[^;\"\\n]+");

        return lines.stream()
                .map(pattern::matcher)
                .map(Matcher::results)
                .map(Stream::toList)
                .map(this::mapToDateLine)
                .collect(Collectors.toList());
    }

    private SchedulerEntry mapToDateLine(List<MatchResult> results) {
        DateTimeFormatter dimeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        return SchedulerEntry.builder()
                .subject(results.get(0).group())
                .date(LocalDate.parse(results.get(2).group(), dateFormatter))
                .startTime(LocalTime.parse(results.get(3).group(), dimeFormatter))
                .endTime(LocalTime.parse(results.get(4).group(), dimeFormatter))
                .build();
    }
}
