package pl.bodzioch.damian.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Builder
@Getter
public class SchedulerModel {

    private List<SchedulerDay> days;
    private UserModel userModel;
    private String name;
    private UUID id;
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;

    public Optional<UUID> getId() {
        return Optional.ofNullable(id);
    }

    public Optional<String> getName() {
        return Optional.ofNullable(name);
    }

    public Optional<LocalDateTime> getCreateDate() {
        return Optional.ofNullable(createDate);
    }

    public Optional<LocalDateTime> getModifyDate() {
        return Optional.ofNullable(modifyDate);
    }
}
