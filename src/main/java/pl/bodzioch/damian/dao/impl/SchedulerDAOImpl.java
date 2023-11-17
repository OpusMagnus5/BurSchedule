package pl.bodzioch.damian.dao.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import pl.bodzioch.damian.dao.SchedulerDAO;
import pl.bodzioch.damian.entity.SchedulerDbEntity;
import pl.bodzioch.damian.exception.AppException;
import pl.bodzioch.damian.mapper.EntityMapper;
import pl.bodzioch.damian.model.Scheduler;
import pl.bodzioch.damian.model.SchedulerInfo;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
                                    "WHERE scheduler.name = :name", SchedulerDbEntity.class)
                    .setParameter("name", name)
                    .getSingleResult();

            return EntityMapper.map(scheduler);
        } catch (NoResultException | NonUniqueResultException ex) {
            log.warn("Scheduler with name {} not found", name, ex);
            throw new AppException("scheduler.dao.scheduler.notFound", List.of(name), HttpStatus.BAD_REQUEST, ex);

        }
    }

    @Override
    public List<SchedulerInfo> getAllSchedulersInfo() {
        List<SchedulerDbEntity> resultList = entityManager.createQuery(
                        "SELECT scheduler " +
                                "FROM SchedulerDbEntity scheduler " +
                                "ORDER BY scheduler.createDate, scheduler.modifyDate, scheduler.name DESC", SchedulerDbEntity.class)
                .getResultList();

        if (CollectionUtils.isEmpty(resultList)) {
            throw new AppException("scheduler.dao.list.notFound", Collections.emptyList(), HttpStatus.NOT_FOUND);
        }

        return resultList.stream()
                .map(EntityMapper::mapToSchedulerInfo)
                .toList();
    }

    @Override
    public Scheduler getScheduler(UUID id) {
        return Optional.ofNullable(entityManager.find(SchedulerDbEntity.class, id))
                .map(EntityMapper::map)
                .orElseThrow(() -> new AppException("scheduler.dao.scheduler.notFound.byId", Collections.emptyList(), HttpStatus.NOT_FOUND));
    }
}
