package pl.bodzioch.damian.mapper;

import pl.bodzioch.damian.dto.bur.AddressDTO;
import pl.bodzioch.damian.dto.bur.ServiceDTO;
import pl.bodzioch.damian.dto.bur.ServiceScheduleDTO;
import pl.bodzioch.damian.model.ScheduleEntry;
import pl.bodzioch.damian.model.ServiceModel;
import pl.bodzioch.damian.model.ServiceStatus;

import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class BurMapper {

    public static ScheduleEntry map(ServiceScheduleDTO dto) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        return ScheduleEntry.builder()
                .subject(dto.getTemat())
                .date(ZonedDateTime.parse(dto.getData()).toLocalDate())
                .startTime(LocalTime.parse(dto.getGodzinaRozpoczecia(), formatter))
                .endTime(LocalTime.parse(dto.getGodzinaRozpoczecia(), formatter))
                .build();
    }

    public static ServiceModel map(ServiceDTO dto) {  //TODO dodac opcionale
        return ServiceModel.builder()
                .id(dto.getId())
                .status(mapServiceStatus(dto.getStatus().getCode()))
                .number(dto.getNumer())
                .title(dto.getTytul())
                .dateBeginningOfService(ZonedDateTime.parse(dto.getDataRozpoczeciaUslugi()).toLocalDate())
                .dateCompletionOfService(ZonedDateTime.parse(dto.getDataZakonczeniaUslugi()).toLocalDate())
                .numberOfHours(dto.getLiczbaGodzin())
                .serviceProviderId(dto.getDostawcaUslug().getId())
                .serviceProviderName(dto.getDostawcaUslug().getNazwa())
                .location(dto.getAdres().map(AddressDTO::getNazwaMiejscowosci).orElse(null))
                .build();
    }

    private static ServiceStatus mapServiceStatus(String serviceCode) {
        return Arrays.stream(ServiceStatus.values())
                .filter(e -> serviceCode.equals(e.getCode()))
                .findAny()
                .orElseThrow(IllegalStateException::new);
    }
}
