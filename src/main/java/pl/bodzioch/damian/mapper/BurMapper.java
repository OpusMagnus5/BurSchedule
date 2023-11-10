package pl.bodzioch.damian.mapper;

import pl.bodzioch.damian.dto.bur.AddressDTO;
import pl.bodzioch.damian.dto.bur.ServiceDTO;
import pl.bodzioch.damian.dto.bur.ServiceScheduleDTO;
import pl.bodzioch.damian.model.SchedulerEntry;
import pl.bodzioch.damian.model.ServiceModel;
import pl.bodzioch.damian.model.ServiceProvider;
import pl.bodzioch.damian.model.ServiceStatus;

import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class BurMapper {

    public static SchedulerEntry map(ServiceScheduleDTO dto) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        return SchedulerEntry.builder()
                .subject(dto.getTemat())
                .date(ZonedDateTime.parse(dto.getData()).toLocalDate())
                .startTime(LocalTime.parse(dto.getGodzinaRozpoczecia(), formatter))
                .endTime(LocalTime.parse(dto.getGodzinaRozpoczecia(), formatter))
                .build();
    }

    public static ServiceModel map(ServiceDTO dto) {
        return ServiceModel.builder()
                .id(dto.getId())
                .status(mapServiceStatus(dto.getStatus().getCode()))
                .number(dto.getNumer())
                .title(dto.getTytul())
                .dateBeginningOfService(ZonedDateTime.parse(dto.getDataRozpoczeciaUslugi()).toLocalDate())
                .dateCompletionOfService(ZonedDateTime.parse(dto.getDataZakonczeniaUslugi()).toLocalDate())
                .numberOfHours(dto.getLiczbaGodzin())
                .serviceProviderId(dto.getDostawcaUslug().getId())
                .serviceProviderName(mapServiceProvider(dto.getDostawcaUslug().getId()))
                .location(dto.getAdres().map(AddressDTO::getNazwaMiejscowosci).orElse(null))
                .build();
    }

    private static ServiceStatus mapServiceStatus(String serviceCode) {
        return Arrays.stream(ServiceStatus.values())
                .filter(e -> serviceCode.equals(e.getCode()))
                .findAny()
                .orElseThrow(IllegalStateException::new);
    }

    private static ServiceProvider mapServiceProvider(Long providerId) {
        return Arrays.stream(ServiceProvider.values())
                .filter(provider -> provider.getId() == providerId)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Service Provider should exists"));
    }
}
