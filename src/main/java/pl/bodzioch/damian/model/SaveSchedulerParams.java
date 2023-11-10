package pl.bodzioch.damian.model;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class SaveSchedulerParams {

    private List<SchedulerCreateDayParams> schedulerDays;
    private String schedulerName;
}
