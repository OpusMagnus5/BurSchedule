package pl.bodzioch.damian.mapper;

import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import pl.bodzioch.damian.configuration.security.UserRoles;
import pl.bodzioch.damian.dto.client.*;
import pl.bodzioch.damian.exception.AppException;
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

    public SchedulerViewDTO mapToSchedulerView(SchedulerDay schedulerDay) {
        return SchedulerViewDTO.builder()
                .date(schedulerDay.getDate())
                .startTime(schedulerDay.getEntries().get(0).getStartTime())
                .endTime(schedulerDay.getEntries().get(0).getEndTime())
                .build();
    }

    public ModifiedSchedulerFromServiceParams map(SchedulerDayDTO schedulerDay, SchedulerEntry schedulerEntry) {
        return ModifiedSchedulerFromServiceParams.builder()
                .email(schedulerDay.getEmail())
                .date(schedulerDay.getDate())
                .timeDifference(schedulerEntry.getStartTime().until(schedulerDay.getStartTime(), ChronoUnit.MINUTES))
                .build();
    }

    public SchedulerDay map(CreateSchedulerDayDTO day) {
        return SchedulerDay.builder()
                .email(day.getEmail())
                .date(day.getDate())
                .entries(day.getRecords().stream()
                        .map(this::map)
                        .toList())
                .build();
    }

    public SchedulerDay map(SaveSchedulerDayDTO day) {
        return SchedulerDay.builder()
                .email(day.getEmail())
                .date(day.getDate())
                .entries(day.getRecords().stream()
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

    public SchedulerListResponseViewDTO mapToSchedulerListResponse(List<SchedulerInfo> schedulerInfoList) {
        return SchedulerListResponseViewDTO.builder()
                .schedulers(schedulerInfoList.stream()
                        .map(scheduler -> SchedulerInfoViewDTO.builder()
                                .id(securityService.encryptMessage(scheduler.getId().toString()))
                                .name(scheduler.getName())
                                .userName(scheduler.getUserName())
                                .daysNumber(scheduler.getDaysNumber())
                                .createDate(scheduler.getCreateDate())
                                .modifyDate(scheduler.getModifyDate())
                                .build())
                        .toList())
                .build();
    }

    public GetSchedulerResponseViewDTO mapToGetSchedulerResponse(SchedulerModel schedulerModel) {
        return GetSchedulerResponseViewDTO.builder()
                .name(schedulerModel.getName().orElseThrow(AppException::getGeneralInternalError))
                .id(securityService.encryptMessage(schedulerModel.getId().toString()))
                .days(schedulerModel.getDays().stream()
                        .map(this::mapToGetSchedulerDayView)
                        .toList())
                .build();
    }

    private GetSchedulerDayViewDTO mapToGetSchedulerDayView(SchedulerDay day) {
        return GetSchedulerDayViewDTO.builder()
                .date(day.getDate())
                .email(day.getEmail().orElse(null))
                .records(day.getEntries().stream()
                        .map(this::mapToGetSchedulerRecordView)
                        .toList())
                .build();
    }

    private GetSchedulerRecordViewDTO mapToGetSchedulerRecordView(SchedulerEntry entry) {
        return GetSchedulerRecordViewDTO.builder()
                .subject(entry.getSubject())
                .id(securityService.encryptMessage(entry.getId().toString()))
                .startTime(entry.getStartTime())
                .endTime(entry.getEndTime())
                .build();
    }

    private SchedulerEntry map(CreateSchedulerRecordDTO record) {
        return SchedulerEntry.builder()
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
