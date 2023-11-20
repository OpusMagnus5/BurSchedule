package pl.bodzioch.damian.service;

import pl.bodzioch.damian.model.ModifiedSchedulerFromServiceParams;
import pl.bodzioch.damian.model.SchedulerDay;
import pl.bodzioch.damian.model.SchedulerModel;

import java.util.List;

public interface GenerateSchedulerFileService {

    byte[] generateFile(List<SchedulerDay> days);
    byte[] generateFile(List<ModifiedSchedulerFromServiceParams> params, SchedulerModel scheduler);
}
