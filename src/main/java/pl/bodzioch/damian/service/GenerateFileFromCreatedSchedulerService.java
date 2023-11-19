package pl.bodzioch.damian.service;

import pl.bodzioch.damian.model.SchedulerDay;

import java.util.List;

public interface GenerateFileFromCreatedSchedulerService {

    byte[] generateFile(List<SchedulerDay> days);
}
