package pl.bodzioch.damian.dto.client;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDate;

@Builder
@Getter
public class SchedulerInfoViewDTO implements Serializable {

    private String id;
    private String name;
    private Integer daysNumber;
    private LocalDate createDate;
    private LocalDate modifyDate;
}
