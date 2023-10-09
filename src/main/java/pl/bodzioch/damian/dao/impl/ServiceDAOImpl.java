package pl.bodzioch.damian.dao.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import pl.bodzioch.damian.dao.ServiceDAO;
import pl.bodzioch.damian.entity.ServiceDbEntity;
import pl.bodzioch.damian.exception.ServicesNotFoundException;

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
    public List<Long> getAllServiceIds() throws ServicesNotFoundException {
        List<Long> serviceIds = entityManager.createQuery("SELECT service.burId FROM ServiceDbEntity service", Long.class).getResultList();
        if (serviceIds.isEmpty()) {
            log.info("Services not found");
            throw new ServicesNotFoundException(); //TODO dodac kolumne location
        }
        return serviceIds;
    }
}
