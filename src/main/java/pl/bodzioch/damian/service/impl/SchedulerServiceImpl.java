package pl.bodzioch.damian.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.bodzioch.damian.client.BurClient;
import pl.bodzioch.damian.dto.client.SchedulerViewDTO;
import pl.bodzioch.damian.exception.SchedulerNotFoundException;
import pl.bodzioch.damian.mapper.ClientMapper;
import pl.bodzioch.damian.model.ScheduleEntry;
import pl.bodzioch.damian.service.SchedulerService;
import pl.bodzioch.damian.service.SecurityService;
import pl.bodzioch.damian.session.SessionBean;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

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

    private void sortScheduler(List<ScheduleEntry> scheduler) {
        scheduler.sort(Comparator.comparing(ScheduleEntry::getDate).thenComparing(ScheduleEntry::getStartTime));
    }

    @Override
    public List<ScheduleEntry> getBeginningsOfDays(List<ScheduleEntry> scheduler) {
        return scheduler.stream()
                .filter(distinctByKey(ScheduleEntry::getDate))
                .toList();
    }

    public  <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
}
