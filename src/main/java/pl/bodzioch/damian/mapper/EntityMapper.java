package pl.bodzioch.damian.mapper;

import pl.bodzioch.damian.entity.ServiceDbEntity;
import pl.bodzioch.damian.model.ServiceModel;

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
                .serviceProviderName(service.getServiceProviderName())
                .location(service.getLocation())
                .build();
    }
}
