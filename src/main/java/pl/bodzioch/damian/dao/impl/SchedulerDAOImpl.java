package pl.bodzioch.damian.dao.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import pl.bodzioch.damian.dao.SchedulerDAO;
import pl.bodzioch.damian.entity.SchedulerDbEntity;
import pl.bodzioch.damian.exception.AppException;
import pl.bodzioch.damian.mapper.EntityMapper;
import pl.bodzioch.damian.model.Scheduler;

import java.util.List;

@Slf4j
@Repository
public class SchedulerDAOImpl implements SchedulerDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public Scheduler saveScheduler(SchedulerDbEntity entity) {
        entity.getEntries().forEach(entry -> entry.setScheduler(entity));
        entityManager.merge(entity);
        entityManager.flush();
        return EntityMapper.map(entity);
    }

    @Override
    public Scheduler getByName(String name) {
        try {
            SchedulerDbEntity scheduler = entityManager.createQuery(
                            "SELECT scheduler " +
                                    "FROM SchedulerDbEntity scheduler " +
                                    "JOIN FETCH scheduler.entries entry " +
                                    "WHERE scheduler.name = :name " +
                                    "ORDER BY entry.date, entry.startTime", SchedulerDbEntity.class)
                    .setParameter("name", name)
                    .getSingleResult();

            return EntityMapper.map(scheduler);
        } catch (NoResultException | NonUniqueResultException ex) {
            log.warn("Scheduler with name {} not found", name, ex);
            throw new AppException("scheduler.dao.scheduler.notFound", List.of(name), HttpStatus.BAD_REQUEST, ex);

        }
    }
}
