package pl.bodzioch.damian.service;

import pl.bodzioch.damian.dto.client.SchedulerViewDTO;
import pl.bodzioch.damian.model.ScheduleEntry;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface SchedulerService {

    List<SchedulerViewDTO> getSchedulerForService(String serviceId);
    List<ScheduleEntry> getBeginningsOfDays(List<ScheduleEntry> scheduler);
    List<SchedulerViewDTO> getSchedulerForService(InputStream inputStream) throws IOException;
}
