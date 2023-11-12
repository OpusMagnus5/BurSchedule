package pl.bodzioch.damian.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Builder
@Getter
public class SchedulerEntry {

    private String subject;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private UUID id;
}
