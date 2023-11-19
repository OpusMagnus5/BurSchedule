package pl.bodzioch.damian.service;

import pl.bodzioch.damian.model.ModifiedSchedulerFromServiceParams;
import pl.bodzioch.damian.model.SchedulerModel;

import java.util.List;

public interface GenerateFileService {

    byte[] generateFile(List<ModifiedSchedulerFromServiceParams> params, SchedulerModel scheduler);
}
