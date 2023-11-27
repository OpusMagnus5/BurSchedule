package pl.bodzioch.damian.dao.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import pl.bodzioch.damian.dao.SchedulerDAO;
import pl.bodzioch.damian.entity.SchedulerDbEntity;
import pl.bodzioch.damian.entity.SchedulerEntryDbEntity;
import pl.bodzioch.damian.entity.UserDbEntity;
import pl.bodzioch.damian.exception.AppException;
import pl.bodzioch.damian.mapper.EntityMapper;
import pl.bodzioch.damian.model.SchedulerInfo;
import pl.bodzioch.damian.model.SchedulerModel;
import pl.bodzioch.damian.session.SessionBean;

import java.util.*;

@Slf4j
@Repository
public class SchedulerDAOImpl implements SchedulerDAO {

    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private SessionBean sessionBean;

    @Override
    @Transactional
    public SchedulerModel saveScheduler(SchedulerDbEntity entity) {
        if (entity.getId().isPresent()) {
            removeEntriesRemovedByUser(entity);
        }
        UUID userId = entity.getUser().getId();
        UserDbEntity user = entityManager.find(UserDbEntity.class, userId);
        entity.setUser(user);
        entityManager.merge(entity);
        entityManager.flush();
        entityManager.clear();
        return EntityMapper.map(entity);
    }

    private void removeEntriesRemovedByUser(SchedulerDbEntity entity) {
        SchedulerDbEntity existedEntity = entityManager.find(SchedulerDbEntity.class, entity.getId());
        entity.setCreateDate(existedEntity.getCreateDate());
        List<SchedulerEntryDbEntity> entriesToRemove = existedEntity.getEntries().stream()
                .filter(entry -> !entity.getEntries().stream()
                        .map(SchedulerEntryDbEntity::getId)
                        .filter(Objects::nonNull)
                        .toList().contains(entry.getId()))
                .toList();
        entriesToRemove.forEach(entityManager::remove);
        entityManager.flush();
        entityManager.clear();
    }

    @Override
    public SchedulerModel getByName(String name) {
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
                                "LEFT JOIN FETCH scheduler.user user " +
                                "ORDER BY scheduler.createDate, scheduler.modifyDate DESC", SchedulerDbEntity.class)
                .getResultList();

        if (CollectionUtils.isEmpty(resultList)) {
            throw new AppException("scheduler.dao.list.notFound", Collections.emptyList(), HttpStatus.NOT_FOUND);
        }

        return resultList.stream()
                .map(EntityMapper::mapToSchedulerInfo)
                .toList();
    }

    @Override
    public SchedulerModel getScheduler(UUID id) {
        return Optional.ofNullable(entityManager.find(SchedulerDbEntity.class, id))
                .map(EntityMapper::map)
                .orElseThrow(() -> new AppException("scheduler.dao.scheduler.notFound.byId", Collections.emptyList(), HttpStatus.NOT_FOUND));
    }

    @Override
    @Transactional
    public void deleteScheduler(SchedulerDbEntity scheduler) {
        SchedulerDbEntity mergedEntity = entityManager.merge(scheduler);
        entityManager.remove(mergedEntity);
    }
}
