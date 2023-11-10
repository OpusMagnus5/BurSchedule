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

import java.util.List;

@Slf4j
@Repository
public class SchedulerDAOImpl implements SchedulerDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void saveScheduler(SchedulerDbEntity entity) {
        entityManager.persist(entity);
    }

    public SchedulerDbEntity getByName(String name) {
        try {
            SchedulerDbEntity scheduler = entityManager.createQuery(
                            "SELECT scheduler " +
                                    "FROM SchedulerDbEntity scheduler " +
                                    "WHERE scheduler.name = :name", SchedulerDbEntity.class)
                    .setParameter("name", name)
                    .getSingleResult();
//TODO
            entityManager.
        } catch (NoResultException | NonUniqueResultException ex) {
            log.warn("Scheduler with name {} not found", name, ex);
            throw new AppException("scheduler.dao.scheduler.notFound", List.of(name), HttpStatus.BAD_REQUEST, ex);

        }
    }
}
