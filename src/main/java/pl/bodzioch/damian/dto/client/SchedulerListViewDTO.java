package pl.bodzioch.damian.dto.client;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@Getter
public class SchedulerListViewDTO implements Serializable {

    private List<SchedulerViewDTO> schedulerEntries;

    private GetSchedulerResponseViewDTO schedulerToEdit;
}
