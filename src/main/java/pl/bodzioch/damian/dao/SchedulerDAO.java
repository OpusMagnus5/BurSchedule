package pl.bodzioch.damian.dao;

import pl.bodzioch.damian.entity.SchedulerDbEntity;
import pl.bodzioch.damian.model.Scheduler;
import pl.bodzioch.damian.model.SchedulerInfo;

import java.util.List;
import java.util.UUID;

public interface SchedulerDAO {

    Scheduler saveScheduler(SchedulerDbEntity entity);
    Scheduler getByName(String name);
    List<SchedulerInfo> getAllSchedulersInfo();
    Scheduler getScheduler(UUID id);
}
