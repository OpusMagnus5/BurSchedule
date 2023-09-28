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
                .numberOfHours(serviceModel.getNumberOfHours().orElse(null))
                .serviceProviderId(securityService.encryptMessage(Long.toString(serviceModel.getServiceProviderId())))
                .serviceProviderName(serviceModel.getServiceProviderName())
                .location(serviceModel.getLocation().orElse(null))
                .street(serviceModel.getStreet().orElse(null))
                .postcode(serviceModel.getPostcode().orElse(null))
                .buildingNumber(serviceModel.getBuildingNumber().orElse(null))
                .localeNumber(serviceModel.getLocaleNumber().orElse(null))
                .build();

    }
}
