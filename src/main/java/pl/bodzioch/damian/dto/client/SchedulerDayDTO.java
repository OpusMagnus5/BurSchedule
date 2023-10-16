package pl.bodzioch.damian.dto.client;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
public class SchedulerDayDTO implements Serializable {

    @Email(message = "scheduler.generate.day.email")
    private String email;
    @NotNull(message = "scheduler.generate.day.date.notNull")
    private LocalDate date;
    @NotNull(message = "scheduler.generate.day.startTime.notNull")
    private LocalTime startTime;
}
