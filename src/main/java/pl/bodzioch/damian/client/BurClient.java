package pl.bodzioch.damian.client;

import pl.bodzioch.damian.model.SchedulerEntry;
import pl.bodzioch.damian.model.ServiceModel;

import java.util.List;

public interface BurClient {

    List<SchedulerEntry> getScheduleForService(long serviceId);
    List<ServiceModel> getServiceById(long serviceId);
    List<ServiceModel> getServicesByNip(long nip);
}
