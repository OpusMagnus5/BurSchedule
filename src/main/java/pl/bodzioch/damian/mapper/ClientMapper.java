package pl.bodzioch.damian.mapper;

import pl.bodzioch.damian.dto.client.ServiceViewDTO;
import pl.bodzioch.damian.model.Service;

public class ClientMapper {

    public static ServiceViewDTO map(Service service) {
        return ServiceViewDTO.builder()
                .id(service.getId())
                .status(service.getStatus())
                .number(service.getNumber())
                .title(service.getTitle())
                .dateBeginningOfService(service.getDateBeginningOfService())
                .dateCompletionOfService(service.getDateCompletionOfService())
                .numberOfHours(service.getNumberOfHours())
                .serviceProviderId(service.getServiceProviderId())
                .serviceProviderName(service.getServiceProviderName())
                .location(service.getLocation())
                .street(service.getStreet())
                .postcode(service.getPostcode())
                .buildingNumber(service.getBuildingNumber())
                .localeNumber(service.getLocaleNumber())
                .build();

    }
}
