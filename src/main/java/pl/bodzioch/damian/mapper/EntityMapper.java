package pl.bodzioch.damian.mapper;

import pl.bodzioch.damian.configuration.security.UserRoles;
import pl.bodzioch.damian.entity.ServiceDbEntity;
import pl.bodzioch.damian.entity.ServiceProviderDb;
import pl.bodzioch.damian.entity.ServiceStatusDb;
import pl.bodzioch.damian.entity.UserDbEntity;
import pl.bodzioch.damian.model.ServiceModel;
import pl.bodzioch.damian.model.ServiceProvider;
import pl.bodzioch.damian.model.ServiceStatus;
import pl.bodzioch.damian.model.UserModel;

import java.util.Arrays;

public class EntityMapper {

    public static ServiceDbEntity map(ServiceModel service) {
        return ServiceDbEntity.builder()
                .burId(service.getId())
                .status(mapServiceStatus(service.getStatus()))
                .number(service.getNumber())
                .title(service.getTitle())
                .dateBeginningOfService(service.getDateBeginningOfService())
                .dateCompletionOfService(service.getDateCompletionOfService())
                .numberOfHours(service.getNumberOfHours())
                .serviceProviderId(service.getServiceProviderId())
                .serviceProviderName(mapServiceProviderName(service.getServiceProviderId()))
                .location(service.getLocation().orElse(null))
                .build();
    }

    public static ServiceModel map(ServiceDbEntity service) {
        return ServiceModel.builder()
                .id(service.getBurId())
                .status(ServiceStatus.valueOf(service.getStatus().name().replace(" ", "_")))
                .number(service.getNumber())
                .title(service.getTitle())
                .dateBeginningOfService(service.getDateBeginningOfService())
                .dateCompletionOfService(service.getDateCompletionOfService())
                .numberOfHours(service.getNumberOfHours())
                .serviceProviderId(service.getServiceProviderId())
                .serviceProviderName(ServiceProvider.valueOf(service.getServiceProviderName().name().replace(" ", "_")))
                .location(service.getLocation().orElse(null))
                .build();
    }

    public static UserModel map(UserDbEntity user) {
        return UserModel.builder()
                .id(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRoles().stream()
                        .map(role -> UserRoles.valueOf(role.name().replace(" ", "_")))
                        .toList())
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

    private static ServiceStatusDb mapServiceStatus(ServiceStatus serviceStatus) {
        return ServiceStatusDb.valueOf(serviceStatus.name().replace(" ", "_"));
    }
}
