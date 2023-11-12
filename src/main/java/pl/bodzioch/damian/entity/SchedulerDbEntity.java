package pl.bodzioch.damian.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.CurrentTimestamp;
import org.hibernate.annotations.NaturalId;
import org.hibernate.generator.EventType;
import pl.bodzioch.damian.configuration.database.GeneratedUuidValue;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "schedulers")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SchedulerDbEntity {

    @GeneratedUuidValue(types = EventType.INSERT)
    @Id
    private UUID id;

    @NaturalId
    private String name;

    @OneToMany(mappedBy = "scheduler", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SchedulerEntryDbEntity> entries;

    @CreationTimestamp
    @Column(name = "create_date")
    private LocalDateTime createDate;

    @CurrentTimestamp(event = EventType.UPDATE)
    @Column(name = "modify_date")
    private LocalDateTime modifyDate;  //TODO dodanie usuwania session do strony logowania, sprobowac wylczyc kaskade i zobczyc czy zadziala
}