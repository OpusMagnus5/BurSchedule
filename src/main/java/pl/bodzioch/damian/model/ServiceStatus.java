package pl.bodzioch.damian.model;

import lombok.Getter;

@Getter
public enum ServiceStatus {

    PUBLISHED("published"),
    CANCELED("canceled"),
    SUSPENDED("suspended"),
    COMPLETED("completed"),
    BLOCKED("blocked"),
    NOT_COMPLETE("not_complete");

    private final String code;

    ServiceStatus(String code) {
        this.code = code;
    }
}
