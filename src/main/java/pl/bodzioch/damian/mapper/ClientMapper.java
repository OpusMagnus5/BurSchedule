package pl.bodzioch.damian.mapper;

import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import pl.bodzioch.damian.configuration.security.UserRoles;
import pl.bodzioch.damian.dto.client.*;
import pl.bodzioch.damian.model.*;
import pl.bodzioch.damian.service.SecurityService;

import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Component
@AllArgsConstructor
public class ClientMapper {

    private final SecurityService securityService;
    private final MessageSource messageSource;

    public ServiceViewDTO map(ServiceModel serviceModel) {
        return ServiceViewDTO.builder()
                .id(securityService.encryptMessage(Long.toString(serviceModel.getId())))
                .status(mapStatus(serviceModel.getStatus().getCode()))
                .number(serviceModel.getNumber())
                .title(serviceModel.getTitle())
                .dateBeginningOfService(serviceModel.getDateBeginningOfService())
                .dateCompletionOfService(serviceModel.getDateCompletionOfService())
                .numberOfHours(serviceModel.getNumberOfHours())
                .serviceProviderId(securityService.encryptMessage(Long.toString(serviceModel.getServiceProviderId())))
                .serviceProviderName(serviceModel.getServiceProviderName().getName())
                .location(serviceModel.getLocation().orElse(null))
                .build();
    }

    private String mapStatus(String statusCode) {
        return messageSource.getMessage("service.status." + statusCode, null, LocaleContextHolder.getLocale());
    }

    public SchedulerViewDTO map(SchedulerEntry schedulerEntry) {
        return SchedulerViewDTO.builder()
                .date(schedulerEntry.getDate())
                .startTime(schedulerEntry.getStartTime())
                .endTime(schedulerEntry.getEndTime())
                .build();
    }

    public SchedulerGenerateDayParams map(SchedulerDayDTO schedulerDay, SchedulerEntry schedulerEntry) {
        return SchedulerGenerateDayParams.builder()
                .email(schedulerDay.getEmail())
                .date(schedulerDay.getDate())
                .timeDifference(schedulerEntry.getStartTime().until(schedulerDay.getStartTime(), ChronoUnit.MINUTES))
                .build();
    }

    public SchedulerCreateDayParams map(CreateSchedulerDayDTO day) {
        return SchedulerCreateDayParams.builder()
                .email(day.getEmail())
                .date(day.getDate())
                .records(day.getRecords().stream()
                        .map(this::map)
                        .toList())
                .build();
    }

    public SchedulerCreateDayParams map(SaveSchedulerDayDTO day) {
        return SchedulerCreateDayParams.builder()
                .email(day.getEmail())
                .date(day.getDate())
                .records(day.getRecords().stream()
                        .map(this::map)
                        .toList())
                .build();
    }

    public UserRolesViewDTO map(UserRoles[] roles) {
        List<String> result = Arrays.stream(roles)
                .map(UserRoles::getRoleCode)
                .toList();

        return new UserRolesViewDTO(result);
    }

    public UserModel map(CreateUserRequestDTO request) {
        return UserModel.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .roles(request.getRoles().stream()
                        .map(role -> UserRoles.valueOf(role.replace(" ", "_")))
                        .toList())
                .build();
    }

    private SchedulerCreateRecordParams map(CreateSchedulerRecordDTO record) {
        return SchedulerCreateRecordParams.builder()
                .id(record.getId()
                        .map(securityService::decryptMessage)
                        .map(UUID::fromString)
                        .orElse(null))
                .subject(record.getSubject())
                .startTime(record.getStartTime())
                .endTime(record.getEndTime())
                .build();
    }
}
