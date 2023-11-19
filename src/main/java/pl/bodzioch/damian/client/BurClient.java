package pl.bodzioch.damian.client;

import pl.bodzioch.damian.model.SchedulerModel;
import pl.bodzioch.damian.model.ServiceModel;

import java.util.List;
import java.util.Optional;

public interface BurClient {

    Optional<SchedulerModel> getScheduleForService(long serviceId);
    List<ServiceModel> getServiceById(long serviceId);
    List<ServiceModel> getServicesByNip(long nip);
}
