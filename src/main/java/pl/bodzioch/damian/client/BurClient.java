package pl.bodzioch.damian.client;

import pl.bodzioch.damian.model.ScheduleEntry;
import pl.bodzioch.damian.model.ServiceModel;

import java.util.List;

public interface BurClient {

    List<ScheduleEntry> getScheduleForService(long serviceId);
    List<ServiceModel> getServicesById(long serviceId);
    List<ServiceModel> getServicesByNip(long nip);
}
