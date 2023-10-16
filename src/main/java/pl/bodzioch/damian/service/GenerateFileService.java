package pl.bodzioch.damian.service;

import pl.bodzioch.damian.model.ScheduleEntry;
import pl.bodzioch.damian.model.SchedulerDayParams;

import java.util.List;

public interface GenerateFileService {

    byte[] generateFile(List<SchedulerDayParams> params, List<ScheduleEntry> burScheduler);
}
