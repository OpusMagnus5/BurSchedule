package pl.bodzioch.damian.dto.client;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public class ServiceViewDTO {

    private String id;
    private String status;
    private String number;
    private String title;
    private LocalDate dateBeginningOfService;
    private LocalDate dateCompletionOfService;
    private int numberOfHours;
    private String serviceProviderId;
    private String serviceProviderName;
    private String location;
    private String street;
    private String postcode;
    private String buildingNumber;
    private String localeNumber;
}
