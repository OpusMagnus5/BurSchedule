package pl.bodzioch.damian.model;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Builder
@Getter
public class Scheduler {

    private List<SchedulerDay> days;
    private String name;
    private UUID id;
}
