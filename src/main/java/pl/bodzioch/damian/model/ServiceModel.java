package pl.bodzioch.damian.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Optional;

@Builder
@Getter
public class ServiceModel {

    private long id;
    private ServiceStatus status;
    private String number;
    private String title;
    private LocalDate dateBeginningOfService;
    private LocalDate dateCompletionOfService;
    private int numberOfHours;
    private long serviceProviderId;
    private String serviceProviderName;
    private String location;
    private String street;
    private String postcode;
    private String buildingNumber;
    private String localeNumber;

    public Optional<String> getLocation() {
        return Optional.ofNullable(location);
    }

    public Optional<String> getStreet() {
        return Optional.ofNullable(street);
    }

    public Optional<String> getPostcode() {
        return Optional.ofNullable(postcode);
    }

    public Optional<String> getBuildingNumber() {
        return Optional.ofNullable(buildingNumber);
    }

    public Optional<String> getLocaleNumber() {
        return Optional.ofNullable(localeNumber);
    }
}
