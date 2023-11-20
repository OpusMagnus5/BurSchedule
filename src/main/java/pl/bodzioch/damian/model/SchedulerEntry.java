package pl.bodzioch.damian.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import java.util.UUID;

@Builder
@Getter
@Setter
public class SchedulerEntry {

    private String subject;
    private LocalTime startTime;
    private LocalTime endTime;
    private UUID id;
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;

    public Optional<LocalDateTime> getCreateDate() {
        return Optional.ofNullable(createDate);
    }

    public Optional<LocalDateTime> getModifyDate() {
        return Optional.ofNullable(modifyDate);
    }

    public Optional<UUID> getId() {
        return Optional.ofNullable(id);
    }
}
