package pl.bodzioch.damian.dao;

import pl.bodzioch.damian.entity.ServiceDbEntity;
import pl.bodzioch.damian.exception.ServicesNotFoundException;
import pl.bodzioch.damian.model.ServiceModel;

import java.util.List;

public interface ServiceDAO {

    List<Long> getAllServiceIds() throws ServicesNotFoundException;
    void saveServices(List<ServiceDbEntity> services);
    List<ServiceModel> getAllServices();
}
