package pl.bodzioch.damian.mapper;

import pl.bodzioch.damian.entity.ServiceDbEntity;
import pl.bodzioch.damian.entity.ServiceProviderDb;
import pl.bodzioch.damian.model.ServiceModel;
import pl.bodzioch.damian.model.ServiceProvider;

import java.util.Arrays;

public class EntityMapper {

    public static ServiceDbEntity map(ServiceModel service) {
        return ServiceDbEntity.builder()
                .burId(service.getId())
                .status(service.getStatus().name())
                .title(service.getTitle())
                .dateBeginningOfService(service.getDateBeginningOfService())
                .dateCompletionOfService(service.getDateCompletionOfService())
                .numberOfHours(service.getNumberOfHours())
                .serviceProviderId(service.getServiceProviderId())
                .serviceProviderName(mapServiceProviderName(service.getServiceProviderId()))
                .location(service.getLocation().orElse(null))
                .build();
    }

    private static ServiceProviderDb mapServiceProviderName(Long serviceProviderId) {
        return Arrays.stream(ServiceProvider.values())
                .filter(provider -> provider.getId() == serviceProviderId)
                .findFirst()
                .map(ServiceProvider::name)
                .map(name -> name.replace(" ", "_"))
                .map(ServiceProviderDb::valueOf)
                .orElseThrow(() -> new IllegalStateException("Service Provider should exists"));
    }
}
