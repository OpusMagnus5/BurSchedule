package pl.bodzioch.damian.dto.client;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalTime;

@Getter
@Builder //TODO przerobic na edycje harmonogramu
public class ListSchedulerRecordViewDTO implements Serializable {

    private String id;
    private String subject;
    private LocalTime startTime;
    private LocalTime endTime;
}
