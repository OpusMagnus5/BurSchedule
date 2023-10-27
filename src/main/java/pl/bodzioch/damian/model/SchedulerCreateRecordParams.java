package pl.bodzioch.damian.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalTime;

@Builder
@Getter
public class SchedulerCreateRecordParams {

    private String subject;
    private LocalTime startTime;
    private LocalTime endTime;
}
