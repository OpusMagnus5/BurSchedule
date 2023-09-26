package pl.bodzioch.damian.client;

import pl.bodzioch.damian.model.ScheduleEntry;

import java.util.List;

public interface BurClient {

    List<ScheduleEntry> getScheduleForService(long serviceId);
    List<pl.bodzioch.damian.model.Service> getServicesById(long serviceId);
    List<pl.bodzioch.damian.model.Service> getServicesByNip(long nip);
}
