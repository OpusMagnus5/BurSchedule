package pl.bodzioch.damian.mapper;

import pl.bodzioch.damian.dto.bur.ServiceScheduleDTO;
import pl.bodzioch.damian.model.ScheduleEntry;

import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class BurMapper {

    public static ScheduleEntry map(ServiceScheduleDTO dto) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        return ScheduleEntry.builder()
                .subject(dto.getTemat())
                .date(ZonedDateTime.parse(dto.getData()).toLocalDate())
                .startTime(LocalTime.parse(dto.getGodzinaRozpoczecia(), formatter))
                .endTime(LocalTime.parse(dto.getGodzinaRozpoczecia(), formatter))
                .build();
    }
}
