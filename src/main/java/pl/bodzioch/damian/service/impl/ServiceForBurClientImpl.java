package pl.bodzioch.damian.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.bodzioch.damian.client.BurClient;
import pl.bodzioch.damian.dto.client.ServiceListViewDTO;
import pl.bodzioch.damian.dto.client.ServiceViewDTO;
import pl.bodzioch.damian.exception.ServicesNotFoundException;
import pl.bodzioch.damian.mapper.ClientMapper;
import pl.bodzioch.damian.model.ServiceModel;
import pl.bodzioch.damian.model.ServiceProvider;
import pl.bodzioch.damian.service.ServiceForBurClient;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class ServiceForBurClientImpl implements ServiceForBurClient {

    private final BurClient burClient;
    private final ClientMapper clientMapper;

    @Autowired
    public ServiceForBurClientImpl(BurClient burClient, ClientMapper clientMapper) {
        this.burClient = burClient;
        this.clientMapper = clientMapper;
    }

    @Override
    public ServiceListViewDTO getAllServicesForAllProviders() throws ServicesNotFoundException {
        List<ServiceModel> serviceModels = Arrays.stream(ServiceProvider.values())
                .map(ServiceProvider::getNip)
                .map(burClient::getServicesByNip)
                .flatMap(List::stream)
                .toList();

        if (serviceModels.isEmpty()) {
            log.info("Services not found");
            throw new ServicesNotFoundException();
        }

        List<ServiceViewDTO> services = serviceModels.stream()
                .map(clientMapper::map)
                .toList();
        return ServiceListViewDTO.builder()
                .services(services)
                .build();
    }
}
