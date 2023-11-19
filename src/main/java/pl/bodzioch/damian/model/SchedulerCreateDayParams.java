package pl.bodzioch.damian.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Builder
@Getter
public class SchedulerCreateDayParams {

    private String email;
    private LocalDate date;
    private List<SchedulerCreateRecordParams> entries;
}
