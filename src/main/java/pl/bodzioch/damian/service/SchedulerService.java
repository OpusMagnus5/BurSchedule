package pl.bodzioch.damian.service;

import pl.bodzioch.damian.dto.client.SchedulerViewDTO;
import pl.bodzioch.damian.model.SchedulerEntry;
import pl.bodzioch.damian.model.SchedulerInfo;
import pl.bodzioch.damian.model.SchedulerModel;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

public interface SchedulerService {

    List<SchedulerViewDTO> getSchedulerForService(String serviceId);
    List<SchedulerEntry> getBeginningsOfDays(SchedulerModel scheduler);
    List<SchedulerViewDTO> getSchedulerFromFile(InputStream inputStream) throws IOException;
    SchedulerModel saveScheduler(SchedulerModel schedulerModel);
    List<SchedulerInfo> getAllSchedulers();
    SchedulerModel getScheduler(UUID id);
}
