package pl.bodzioch.damian.mapper;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import pl.bodzioch.damian.configuration.security.UserRoles;
import pl.bodzioch.damian.entity.*;
import pl.bodzioch.damian.exception.AppException;
import pl.bodzioch.damian.model.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static java.util.stream.Collectors.*;

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

    public static SchedulerInfo mapToSchedulerInfo(SchedulerDbEntity entity) {
        return SchedulerInfo.builder()
                .id(entity.getId())
                .userName(entity.getUser().getUsername())
                .name(entity.getName())
                .daysNumber(entity.getDaysNumber())
                .createDate(entity.getCreateDate().toLocalDate())
                .modifyDate(entity.getModifyDate().
                        map(LocalDateTime::toLocalDate)
                        .orElse(null))
                .build();
    }

    public static SchedulerModel map(SchedulerDbEntity entity) {
        Map<LocalDate, List<SchedulerEntryDbEntity>> entriesByDate = entity.getEntries().stream()
                .collect(groupingBy(SchedulerEntryDbEntity::getDate));

        List<SchedulerDay> days = entriesByDate.entrySet().stream()
                .map(EntityMapper::map)
                .sorted(Comparator.comparing(SchedulerDay::getDate))
                .toList();


        return SchedulerModel.builder()
                .id(entity.getId())
                .name(entity.getName())
                .days(days)
                .build();
    }

    public static SchedulerDbEntity map(SchedulerModel schedulerModel, UserModel userModel) {
        SchedulerDbEntity entity = SchedulerDbEntity.builder()
                .id(schedulerModel.getId().orElse(null))
                .daysNumber(schedulerModel.getDays().size())
                .createDate(schedulerModel.getCreateDate().orElse(null))
                .modifyDate(schedulerModel.getModifyDate().orElse(null))
                .user(EntityMapper.map(userModel))
                .entries(schedulerModel.getDays().stream()
                        .map(day -> day.getEntries().stream()
                                .map(entry -> map(day, entry))
                                .toList())
                        .flatMap(Collection::stream)
                        .toList())
                .build();

        entity.getEntries().forEach(entry -> entry.setScheduler(entity));

        return entity;
    }

    private static SchedulerEntryDbEntity map(SchedulerDay day, SchedulerEntry entry) {
        return SchedulerEntryDbEntity.builder()
                .id(entry.getId().orElse(null))
                .email(day.getEmail().orElseThrow(AppException::getGeneralInternalError))
                .date(day.getDate())
                .subject(entry.getSubject())
                .startTime(entry.getStartTime())
                .endTime(entry.getEndTime())
                .createDate(entry.getCreateDate().orElse(null))
                .modifyDate(entry.getModifyDate().orElse(null))
                .build();
    }

    private static SchedulerDay map(Map.Entry<LocalDate, List<SchedulerEntryDbEntity>> dayEntries) {
        return SchedulerDay.builder()
                .email(dayEntries.getValue().get(0).getEmail())
                .date(dayEntries.getKey())
                .entries(dayEntries.getValue().stream()
                        .map(EntityMapper::map)
                        .sorted(Comparator.comparing(SchedulerEntry::getStartTime))
                        .toList())
                .build();
    }

    private static SchedulerEntry map(SchedulerEntryDbEntity entry) {
        return SchedulerEntry.builder()
                .id(entry.getId())
                .subject(entry.getSubject())
                .startTime(entry.getStartTime())
                .endTime(entry.getEndTime())
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
