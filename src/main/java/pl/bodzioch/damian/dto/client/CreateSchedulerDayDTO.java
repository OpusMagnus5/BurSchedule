package pl.bodzioch.damian.dto.client;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Getter
public class CreateSchedulerDayDTO implements Serializable {

    @Email(message = "scheduler.create.day.email.notValid")
    private String email;
    @NotNull(message = "scheduler.create.day.date.notNull")
    private LocalDate date;
    @Valid
    @NotEmpty(message = "scheduler.create.day.records.notEmpty")
    private List<CreateSchedulerRecordDTO> records;
}
