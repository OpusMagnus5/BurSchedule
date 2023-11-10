package pl.bodzioch.damian.model;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Builder
@Getter
public class SaveSchedulerParams {

    private List<SchedulerCreateDayParams> schedulerDays;
    private String schedulerName;
    private UUID id;

    public Optional<UUID> getId() {
        return Optional.ofNullable(id);
    }
}
