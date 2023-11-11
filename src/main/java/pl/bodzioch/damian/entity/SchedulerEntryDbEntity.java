package pl.bodzioch.damian.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.CurrentTimestamp;
import org.hibernate.generator.EventType;
import pl.bodzioch.damian.configuration.database.GeneratedUuidValue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import java.util.UUID;

@Entity
@Table(name = "scheduler_entries")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SchedulerEntryDbEntity {

    @Id
    @GeneratedUuidValue(types = EventType.INSERT)
    private UUID id;

    @Column(name = "scheduler_id")
    private UUID schedulerId;

    private String subject;

    private String email;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    private LocalDate date;

    @CurrentTimestamp(event = EventType.INSERT)
    private LocalDateTime createDate;

    @CurrentTimestamp(event = EventType.UPDATE)
    private LocalDateTime modifyDate;

    public Optional<LocalDateTime> getModifyDate() {
        return Optional.ofNullable(modifyDate);
    }
}