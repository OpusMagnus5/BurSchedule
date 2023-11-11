package pl.bodzioch.damian.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Builder
@Getter
public class ScheduleEntry {

    private String subject;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
}