package pl.bodzioch.damian.mapper;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import pl.bodzioch.damian.configuration.security.UserRoles;
import pl.bodzioch.damian.entity.*;
import pl.bodzioch.damian.model.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class EntityMapper {

    private final static BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

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

    public static UserDbEntity map(UserModel user) {
        return UserDbEntity.builder()
                .username(user.getUsername())
                .password(passwordEncoder.encode(user.getPassword()))
                .roles(user.getRoles().stream()
                        .map(role -> RoleDbEntity.valueOf(role.name().replace(" ", "_")))
                        .toList())
                .build();
    }

    public static SchedulerDbEntity map(SaveSchedulerParams params) {
        return SchedulerDbEntity.builder()
                .name(params.getSchedulerName())
                .entries(map(params.getSchedulerDays()))
                .build();
    }

    private static List<SchedulerEntryDbEntity> map(List<SchedulerCreateDayParams> params) {
        return params.stream()
                .map(EntityMapper::map)
                .flatMap(Collection::stream)
                .toList();

    }

    private static List<SchedulerEntryDbEntity> map(SchedulerCreateDayParams params) {
        return params.getRecords().stream()
                .map(record -> SchedulerEntryDbEntity.builder()
                        .email(params.getEmail())
                        .date(params.getDate())
                        .startTime(record.getStartTime())
                        .endTime(record.getEndTime())
                        .subject(record.getSubject())
                        .build())
                .toList();
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
