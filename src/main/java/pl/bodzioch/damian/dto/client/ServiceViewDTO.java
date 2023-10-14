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

    public Optional<String> getLocation() {
        return Optional.ofNullable(location);
    }
}
