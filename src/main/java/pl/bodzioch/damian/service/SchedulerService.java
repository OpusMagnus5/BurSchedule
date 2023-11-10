package pl.bodzioch.damian.service;

import pl.bodzioch.damian.dto.client.SchedulerViewDTO;
import pl.bodzioch.damian.model.SaveSchedulerParams;
import pl.bodzioch.damian.model.SchedulerEntry;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface SchedulerService {

    List<SchedulerViewDTO> getSchedulerForService(String serviceId);
    List<SchedulerEntry> getBeginningsOfDays(List<SchedulerEntry> scheduler);
    List<SchedulerViewDTO> getSchedulerForService(InputStream inputStream) throws IOException;
    void saveScheduler(SaveSchedulerParams params);
}
