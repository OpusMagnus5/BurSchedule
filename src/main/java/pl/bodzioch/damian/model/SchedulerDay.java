package pl.bodzioch.damian.model;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class SchedulerDay {

    private List<SchedulerEntry> entries;
    private String email;
}
