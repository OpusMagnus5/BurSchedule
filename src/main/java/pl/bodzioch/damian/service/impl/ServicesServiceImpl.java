package pl.bodzioch.damian.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.bodzioch.damian.dao.ServiceDAO;
import pl.bodzioch.damian.dto.client.ServiceListViewDTO;
import pl.bodzioch.damian.dto.client.ServiceViewDTO;
import pl.bodzioch.damian.mapper.ClientMapper;
import pl.bodzioch.damian.service.ServicesService;

import java.util.List;

@Service
@AllArgsConstructor
public class ServicesServiceImpl implements ServicesService {

    private final ServiceDAO serviceDAO;
    private final ClientMapper clientMapper;

    @Override
    public ServiceListViewDTO getAllServices() {
        List<ServiceViewDTO> services = serviceDAO.getAllServices().stream()
                .map(clientMapper::map)
                .toList();

        return ServiceListViewDTO.builder()
                .services(services)
                .build();
    }
}
