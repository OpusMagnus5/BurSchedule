package pl.bodzioch.damian.dto.client;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@Builder
@Getter
public class ServiceListViewDTO implements Serializable {

    private List<ServiceViewDTO> services;
}
