package pl.bodzioch.damian.dao;

import pl.bodzioch.damian.entity.SchedulerDbEntity;
import pl.bodzioch.damian.model.Scheduler;

import java.util.UUID;

public interface SchedulerDAO {

    UUID saveScheduler(SchedulerDbEntity entity);
    Scheduler getByName(String name);
}
