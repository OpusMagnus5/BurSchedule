package pl.bodzioch.damian.dto.client;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@Getter
public class CreateSchedulerRequestDTO implements Serializable {

    @Valid
    @NotEmpty(message = "scheduler.create.days.notEmpty")
    private List<CreateSchedulerDayDTO> days;
}
