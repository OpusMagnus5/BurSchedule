package pl.bodzioch.damian.dto.client;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@Getter
@Builder
public class SchedulerListResponseViewDTO implements Serializable {

    private List<SchedulerInfoViewDTO> schedulers;
}
