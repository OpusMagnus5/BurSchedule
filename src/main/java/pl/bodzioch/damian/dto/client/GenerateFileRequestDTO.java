package pl.bodzioch.damian.dto.client;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@Getter
public class GenerateFileRequestDTO implements Serializable {

    @NotEmpty(message = "scheduler.generate.scheduleDays.notNull")
    List<SchedulerDayDTO> scheduleDays;
}
