package pl.bodzioch.damian.model;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@Builder
@Getter
public class ApiError implements Serializable {

    private List<String> messages;
}
