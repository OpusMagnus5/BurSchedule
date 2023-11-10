package pl.bodzioch.damian.service;

import pl.bodzioch.damian.model.SchedulerEntry;
import pl.bodzioch.damian.model.SchedulerGenerateDayParams;

import java.util.List;

public interface GenerateFileService {

    byte[] generateFile(List<SchedulerGenerateDayParams> params, List<SchedulerEntry> burScheduler);
}
