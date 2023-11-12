package pl.bodzioch.damian.dto.client;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@Getter
@Builder
public class SaveSchedulerResponseDTO implements Serializable {

    private String schedulerId;
    private String message;
    private List<String> entriesIds;
}
