package pl.bodzioch.damian.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CurrentTimestamp;
import org.hibernate.annotations.NaturalId;
import org.hibernate.generator.EventType;
import pl.bodzioch.damian.configuration.database.GeneratedUuidValue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@Entity
@Table(name = "services")
public class ServiceDbEntity {

    @Id
    @GeneratedUuidValue(types = EventType.INSERT)
    private UUID id;
    @NaturalId
    @Column(name = "bur_id")
    private Long burId;
    private String status;
    private String title;
    @Column(name = "date_beginning_of_service")
    private LocalDate dateBeginningOfService;
    @Column(name = "date_completed_of_service")
    private LocalDate dateCompletionOfService;
    @Column(name = "number_of_hours")
    private Integer numberOfHours;
    @Column(name = "service_provider_bur_id")
    private Long serviceProviderId;
    @Column(name = "service_provider_name")
    private String serviceProviderName;
    private String location;
    @Column(name = "create_date")
    private LocalDateTime createDate;
    @Column(name = "modify_date")
    @CurrentTimestamp(event = EventType.UPDATE)
    private LocalDateTime modifyDate;
}
