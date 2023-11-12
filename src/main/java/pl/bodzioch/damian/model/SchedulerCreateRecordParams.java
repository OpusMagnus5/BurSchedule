package pl.bodzioch.damian.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalTime;
import java.util.Optional;
import java.util.UUID;

@Builder
@Getter
public class SchedulerCreateRecordParams {

    private UUID id;
    private String subject;
    private LocalTime startTime;
    private LocalTime endTime;

    public Optional<UUID> getId() {
        return Optional.ofNullable(id);
    }
}
