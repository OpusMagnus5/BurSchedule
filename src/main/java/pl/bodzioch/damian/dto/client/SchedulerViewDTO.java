package pl.bodzioch.damian.dto.client;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

@Builder
@Getter
public class SchedulerViewDTO implements Serializable {

    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
}
