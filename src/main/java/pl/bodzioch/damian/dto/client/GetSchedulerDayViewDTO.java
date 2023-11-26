package pl.bodzioch.damian.dto.client;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class GetSchedulerDayViewDTO implements Serializable {

    private String email;
    private LocalDate date;
    private List<GetSchedulerRecordViewDTO> records;
}
