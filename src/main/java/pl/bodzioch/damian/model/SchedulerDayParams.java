package pl.bodzioch.damian.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class SchedulerDayParams {
    private String email;
    private LocalDate date;
    private long timeDifference;
}
