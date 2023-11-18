package pl.bodzioch.damian.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.UUID;

@Builder
@Getter
public class SchedulerInfo {

    private UUID id;
    private String userName;
    private String name;
    private Integer daysNumber;
    private LocalDate createDate;
    private LocalDate modifyDate;
}
