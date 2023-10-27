package pl.bodzioch.damian.service;

import pl.bodzioch.damian.model.SchedulerCreateDayParams;

import java.util.List;

public interface GenerateFileFromCreatedSchedulerService {

    byte[] generateFile(List<SchedulerCreateDayParams> paramsList);
}
