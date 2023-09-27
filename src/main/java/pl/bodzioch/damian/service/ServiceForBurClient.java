package pl.bodzioch.damian.service;

import pl.bodzioch.damian.dto.client.ServiceListViewDTO;
import pl.bodzioch.damian.exception.ServicesNotFoundException;

public interface ServiceForBurClient {

    ServiceListViewDTO getAllServicesForAllProviders() throws ServicesNotFoundException;
}
