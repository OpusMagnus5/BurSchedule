package pl.bodzioch.damian.entity;

import lombok.Getter;

@Getter
public enum ServiceStatusDb {
    PUBLISHED,
    CANCELED,
    SUSPENDED,
    COMPLETED,
    BLOCKED,
    NOT_COMPLETE
}
