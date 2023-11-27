package pl.bodzioch.damian.dto.client;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@Getter
@Builder
public class GetSchedulerResponseViewDTO implements Serializable {

    private List<GetSchedulerDayViewDTO> days;

    private String name;

    private String id;
}
