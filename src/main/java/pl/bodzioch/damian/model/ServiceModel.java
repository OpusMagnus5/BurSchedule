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
    private ServiceProvider serviceProviderName;
    private String location;

    public Optional<String> getLocation() {
        return Optional.ofNullable(location);
    }
}
