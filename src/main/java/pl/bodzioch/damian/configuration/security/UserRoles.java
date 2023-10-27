package pl.bodzioch.damian.configuration.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserRoles {
    USER("USER");

    private final String roleCode;
}
