package pl.bodzioch.damian.dto.client;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@Getter
@Builder //TODO przerobic na edycje harmonogramu
public class ListSchedulersViewDTO implements Serializable {

    private List<ListSchedulerDayViewDTO> days;

    private String name;

    private String id;
}
