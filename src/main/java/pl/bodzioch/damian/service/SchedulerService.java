package pl.bodzioch.damian.service;

import pl.bodzioch.damian.dto.client.SchedulerViewDTO;

import java.util.List;

public interface SchedulerService {

    List<SchedulerViewDTO> getSchedulerForService(String serviceId);
}
