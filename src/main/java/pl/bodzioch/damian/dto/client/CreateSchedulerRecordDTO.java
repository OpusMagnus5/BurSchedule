package pl.bodzioch.damian.dto.client;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.Optional;

@Getter
public class CreateSchedulerRecordDTO implements Serializable {

    private String id;
    @NotEmpty(message = "scheduler.create.record.subject.notEmpty")
    private String subject;
    @NotNull(message = "scheduler.create.record.startTime.notEmpty")
    private LocalTime startTime;
    @NotNull(message = "scheduler.create.record.endTime.notEmpty")
    private LocalTime endTime;

    public Optional<String> getId() {
        return Optional.ofNullable(id);
    }

}
