package pl.bodzioch.damian.dao.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import pl.bodzioch.damian.dao.ServiceDAO;
import pl.bodzioch.damian.entity.ServiceDbEntity;
import pl.bodzioch.damian.exception.AppException;
import pl.bodzioch.damian.mapper.EntityMapper;
import pl.bodzioch.damian.model.ServiceModel;

import java.util.Collections;
import java.util.List;

@Slf4j
@Repository
public class ServiceDAOImpl implements ServiceDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void saveServices(List<ServiceDbEntity> services) {
        services.forEach(entityManager::persist);
    }

    @Override
    public List<Long> getAllServiceIds() {
        return entityManager.createQuery("SELECT service.burId FROM ServiceDbEntity service", Long.class).getResultList();
    }

    @Override
    public List<ServiceModel> getAllServices() {
            List<ServiceDbEntity> services = entityManager.createQuery("SELECT service FROM ServiceDbEntity service", ServiceDbEntity.class).getResultList();
        if (services.isEmpty()) {
            log.info("Services not found");
            throw new AppException("services.not.found", Collections.emptyList(), HttpStatus.NOT_FOUND);
        }
        return services.stream()
                .map(EntityMapper::map)
                .toList();
    }
}
