package pl.bodzioch.damian.service;

import pl.bodzioch.damian.dto.client.SchedulerViewDTO;
import pl.bodzioch.damian.model.SaveSchedulerParams;
import pl.bodzioch.damian.model.Scheduler;
import pl.bodzioch.damian.model.SchedulerEntry;
import pl.bodzioch.damian.model.SchedulerInfo;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

public interface SchedulerService {

    List<SchedulerViewDTO> getSchedulerForService(String serviceId);
    List<SchedulerEntry> getBeginningsOfDays(List<SchedulerEntry> scheduler);
    List<SchedulerViewDTO> getSchedulerFromFile(InputStream inputStream) throws IOException;
    Scheduler saveScheduler(SaveSchedulerParams params);
    List<SchedulerInfo> getAllSchedulers();
    Scheduler getScheduler(UUID id);
}
