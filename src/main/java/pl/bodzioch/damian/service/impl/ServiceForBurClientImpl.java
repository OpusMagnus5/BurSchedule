package pl.bodzioch.damian.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pl.bodzioch.damian.client.BurClient;
import pl.bodzioch.damian.dao.ServiceDAO;
import pl.bodzioch.damian.dto.client.ServiceListViewDTO;
import pl.bodzioch.damian.dto.client.ServiceViewDTO;
import pl.bodzioch.damian.entity.ServiceDbEntity;
import pl.bodzioch.damian.exception.AppException;
import pl.bodzioch.damian.mapper.ClientMapper;
import pl.bodzioch.damian.mapper.EntityMapper;
import pl.bodzioch.damian.model.ServiceModel;
import pl.bodzioch.damian.model.ServiceProvider;
import pl.bodzioch.damian.service.ServiceForBurClient;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@AllArgsConstructor
@Slf4j
@Service
public class ServiceForBurClientImpl implements ServiceForBurClient {

    private final BurClient burClient;
    private final ClientMapper clientMapper;
    private final ServiceDAO serviceDAO;

    @Override
    public void synchronizeServices() {
        List<ServiceModel> servicesFromApi = getAllServicesFromApi();
        if (servicesFromApi.isEmpty()) {
            log.info("Services not found");
            throw new AppException("services.not.found", Collections.emptyList(), HttpStatus.NOT_FOUND);
        }

        List<Long> serviceIds = serviceDAO.getAllServiceIds();

        List<ServiceDbEntity> servicesToSave = servicesFromApi.stream()
                .filter(service -> !serviceIds.contains(service.getId()))
                .map(EntityMapper::map)
                .toList();

        serviceDAO.saveServices(servicesToSave);
    }

    private List<ServiceModel> getAllServicesFromApi() {
        return Arrays.stream(ServiceProvider.values())
                .map(ServiceProvider::getNip)
                .map(burClient::getServicesByNip)
                .flatMap(List::stream)
                .toList();
    }
}
