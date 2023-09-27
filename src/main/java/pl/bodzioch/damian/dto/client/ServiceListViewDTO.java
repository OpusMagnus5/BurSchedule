package pl.bodzioch.damian.dto.client;

import lombok.Builder;

import java.util.List;

@Builder
public class ServiceListViewDTO {

    private List<ServiceViewDTO> services;
}
