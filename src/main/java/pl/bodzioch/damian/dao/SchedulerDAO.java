package pl.bodzioch.damian.dao;

import pl.bodzioch.damian.entity.SchedulerDbEntity;
import pl.bodzioch.damian.entity.SchedulerEntryDbEntity;
import pl.bodzioch.damian.model.Scheduler;

import java.util.List;
import java.util.UUID;

public interface SchedulerDAO {

    List<UUID> saveScheduler(SchedulerDbEntity entity, List<SchedulerEntryDbEntity> entries);
    Scheduler getByName(String name);
}
