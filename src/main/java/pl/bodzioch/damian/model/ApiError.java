package pl.bodzioch.damian.model;

import lombok.Builder;

import java.util.List;

@Builder
public class ApiError {

    private List<String> messages;
}
