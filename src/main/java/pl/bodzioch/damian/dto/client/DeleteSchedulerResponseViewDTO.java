package pl.bodzioch.damian.dto.client;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Getter
@Builder
public class DeleteSchedulerResponseViewDTO implements Serializable {

    private String message;
}
