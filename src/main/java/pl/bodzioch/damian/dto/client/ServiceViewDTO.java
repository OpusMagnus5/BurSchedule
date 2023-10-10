package pl.bodzioch.damian.dto.client;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Optional;

@Builder
@Getter
public class ServiceViewDTO implements Serializable {

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
