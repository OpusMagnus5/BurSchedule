package pl.bodzioch.damian.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Getter
@Builder
public class SchedulerDay {

    private List<SchedulerEntry> entries;
    private LocalDate date;
    private String email;

    public Optional<String> getEmail() {
        return Optional.ofNullable(email);
    }
}
