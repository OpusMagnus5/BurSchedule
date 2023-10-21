package pl.bodzioch.damian.service.impl;

import lombok.AllArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.BOMInputStream;
import org.springframework.stereotype.Service;
import pl.bodzioch.damian.client.BurClient;
import pl.bodzioch.damian.dto.client.SchedulerViewDTO;
import pl.bodzioch.damian.exception.SchedulerNotFoundException;
import pl.bodzioch.damian.mapper.ClientMapper;
import pl.bodzioch.damian.model.ScheduleEntry;
import pl.bodzioch.damian.service.SchedulerService;
import pl.bodzioch.damian.service.SecurityService;
import pl.bodzioch.damian.session.SessionBean;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class SchedulerServiceImpl implements SchedulerService {

    private final BurClient burClient;
    private final SecurityService securityService;
    private final ClientMapper clientMapper;
    private final SessionBean sessionBean;

    @Override
    public List<SchedulerViewDTO> getSchedulerForService(String serviceId) {
        String encryptedId = securityService.decryptMessage(serviceId);
        List<ScheduleEntry> scheduler = burClient.getScheduleForService(Long.parseLong(encryptedId));
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
    public List<SchedulerViewDTO> getSchedulerForService(InputStream inputStream) throws IOException {
        BOMInputStream streamWithoutBOM = getStreamWithoutBOM(inputStream);
        List<String> lines = IOUtils.readLines(streamWithoutBOM, StandardCharsets.UTF_8);
        List<ScheduleEntry> scheduleEntries = mapToScheduleEntries(lines);
        sortScheduler(scheduleEntries);
        sessionBean.setScheduleEntries(scheduleEntries);

        return getBeginningsOfDays(scheduleEntries).stream()
                .map(clientMapper::map)
                .toList();
    }

    @Override
    public List<ScheduleEntry> getBeginningsOfDays(List<ScheduleEntry> scheduler) {
        return scheduler.stream()
                .filter(distinctByKey(ScheduleEntry::getDate))
                .toList();
    }

    private void sortScheduler(List<ScheduleEntry> scheduler) {
        scheduler.sort(Comparator.comparing(ScheduleEntry::getDate).thenComparing(ScheduleEntry::getStartTime));
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

    private List<ScheduleEntry> mapToScheduleEntries(List<String> lines) {
        Pattern pattern = Pattern.compile("[^;\"\\n]+");

        return lines.stream()
                .map(pattern::matcher)
                .map(Matcher::results)
                .map(Stream::toList)
                .map(this::mapToDateLine)
                .toList();
    }

    private ScheduleEntry mapToDateLine(List<MatchResult> results) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        return ScheduleEntry.builder()
                .subject(results.get(0).group())
                .startTime(LocalTime.parse(results.get(3).group(), formatter))
                .endTime(LocalTime.parse(results.get(4).group(), formatter))
                .build();
    }
}
