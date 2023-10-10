package pl.bodzioch.damian.mapper;

import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import pl.bodzioch.damian.dto.client.ServiceViewDTO;
import pl.bodzioch.damian.model.ServiceModel;
import pl.bodzioch.damian.service.SecurityService;

@Component
@AllArgsConstructor
public class ClientMapper {

    private final SecurityService securityService;
    private final MessageSource messageSource;

    public ServiceViewDTO map(ServiceModel serviceModel) {
        return ServiceViewDTO.builder()
                .id(securityService.encryptMessage(Long.toString(serviceModel.getId())))
                .status(mapStatus(serviceModel.getStatus().getCode()))
                .number(serviceModel.getNumber())
                .title(serviceModel.getTitle())
                .dateBeginningOfService(serviceModel.getDateBeginningOfService())
                .dateCompletionOfService(serviceModel.getDateCompletionOfService())
                .numberOfHours(serviceModel.getNumberOfHours())
                .serviceProviderId(securityService.encryptMessage(Long.toString(serviceModel.getServiceProviderId())))
                .serviceProviderName(serviceModel.getServiceProviderName())
                .location(serviceModel.getLocation().orElse(null))
                .build();
    }

    private String mapStatus(String statusCode) {
        return messageSource.getMessage("service.status." + statusCode, null, LocaleContextHolder.getLocale());
    }
}
