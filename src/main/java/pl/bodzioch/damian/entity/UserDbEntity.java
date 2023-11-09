package pl.bodzioch.damian.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CurrentTimestamp;
import org.hibernate.annotations.NaturalId;
import org.hibernate.generator.EventType;
import pl.bodzioch.damian.configuration.database.GeneratedUuidValue;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Getter
@Setter
@Builder
@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
public class UserDbEntity {

    @Id
    @GeneratedUuidValue(types = EventType.INSERT)
    private UUID id;

    @NaturalId
    private String username;

    private String password;

    @ElementCollection(targetClass = RoleDbEntity.class)
    @CollectionTable(name = "users_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private List<RoleDbEntity> roles;

    @Column(name = "create_date")
    @CurrentTimestamp(event = EventType.INSERT)
    private LocalDateTime createDate;

    @Column(name = "modify_date")
    @CurrentTimestamp(event = EventType.UPDATE)
    private LocalDateTime modifyDate;

    public Optional<LocalDateTime> getModifyDate() {
        return Optional.ofNullable(modifyDate);
    }
}
