package pl.bodzioch.damian.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.CurrentTimestamp;
import org.hibernate.annotations.NaturalId;
import org.hibernate.generator.EventType;
import pl.bodzioch.damian.configuration.database.GeneratedUuidValue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Getter
@Setter
@Builder
@Entity
@Table(name = "services")
@AllArgsConstructor
@NoArgsConstructor
public class ServiceDbEntity {

    @Id
    @GeneratedUuidValue(types = EventType.INSERT)
    private UUID id;

    @NaturalId
    @Column(name = "bur_id")
    private Long burId;

    @Enumerated(EnumType.STRING)
    private ServiceStatusDb status;

    private String number;

    private String title;

    @Column(name = "date_beginning_of_service")
    private LocalDate dateBeginningOfService;

    @Column(name = "date_completed_of_service")
    private LocalDate dateCompletionOfService;

    @Column(name = "number_of_hours")
    private Integer numberOfHours;

    @Column(name = "service_provider_bur_id")
    private Long serviceProviderId;

    @Enumerated(EnumType.STRING)
    @Column(name = "service_provider_name")
    private ServiceProviderDb serviceProviderName;

    private String location;

    @Column(name = "create_date")
    @CreationTimestamp
    private LocalDateTime createDate;

    @Column(name = "modify_date")
    @CurrentTimestamp(event = EventType.UPDATE)
    private LocalDateTime modifyDate;

    public Optional<String> getLocation() {
        return Optional.ofNullable(location);
    }

    public Optional<LocalDateTime> getModifyDate() {
        return Optional.ofNullable(modifyDate);
    }
}
