package pl.bodzioch.damian.dao;

import pl.bodzioch.damian.entity.SchedulerDbEntity;
import pl.bodzioch.damian.model.SchedulerModel;
import pl.bodzioch.damian.model.SchedulerInfo;

import java.util.List;
import java.util.UUID;

public interface SchedulerDAO {

    SchedulerModel saveScheduler(SchedulerDbEntity entity);
    SchedulerModel getByName(String name);
    List<SchedulerInfo> getAllSchedulersInfo();
    SchedulerModel getScheduler(UUID id);
}
