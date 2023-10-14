package pl.bodzioch.damian.dto.client;

import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalTime;

@Builder
public class SchedulerViewDTO {

    private String subject;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
}
