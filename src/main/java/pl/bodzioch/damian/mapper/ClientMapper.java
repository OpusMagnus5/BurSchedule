package pl.bodzioch.damian.mapper;

import org.springframework.stereotype.Component;
import pl.bodzioch.damian.dto.client.ServiceViewDTO;
import pl.bodzioch.damian.model.ServiceModel;
import pl.bodzioch.damian.service.SecurityService;

@Component
public class ClientMapper {

    private final SecurityService securityService;

    public ClientMapper(SecurityService securityService) {
        this.securityService = securityService;
    }

    public ServiceViewDTO map(ServiceModel serviceModel) {
        return ServiceViewDTO.builder()
                .id(securityService.encryptMessage(Long.toString(serviceModel.getId())))
                .status(serviceModel.getStatus())
                .number(serviceModel.getNumber())
                .title(serviceModel.getTitle())
                .dateBeginningOfService(serviceModel.getDateBeginningOfService())
                .dateCompletionOfService(serviceModel.getDateCompletionOfService())
                .numberOfHours(serviceModel.getNumberOfHours())
                .serviceProviderId(securityService.encryptMessage(Long.toString(serviceModel.getServiceProviderId())))
                .serviceProviderName(serviceModel.getServiceProviderName())
                .location(serviceModel.getLocation())
                .street(serviceModel.getStreet())
                .postcode(serviceModel.getPostcode())
                .buildingNumber(serviceModel.getBuildingNumber())
                .localeNumber(serviceModel.getLocaleNumber())
                .build();

    }
}
