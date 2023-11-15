package pl.bodzioch.damian.dto.client;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

@Builder
@Getter
public class SchedulerInfoViewDTO implements Serializable {

    private UUID id;
    private String name;
    private Integer daysNumber;
    private LocalDate createDate;
    private LocalDate modifyDate;
}
