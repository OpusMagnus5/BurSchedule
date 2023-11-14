package pl.bodzioch.damian.dao;

import pl.bodzioch.damian.entity.SchedulerDbEntity;
import pl.bodzioch.damian.model.Scheduler;

import java.util.List;

public interface SchedulerDAO {

    Scheduler saveScheduler(SchedulerDbEntity entity);
    Scheduler getByName(String name);
    List<Scheduler> getAll();
}
