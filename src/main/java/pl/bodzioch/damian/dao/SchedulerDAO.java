package pl.bodzioch.damian.dao;

import pl.bodzioch.damian.entity.SchedulerDbEntity;
import pl.bodzioch.damian.model.Scheduler;

public interface SchedulerDAO {

    Scheduler saveScheduler(SchedulerDbEntity entity);
    Scheduler getByName(String name);
}
