package pl.bodzioch.damian.model;

import lombok.Builder;
import lombok.Getter;
import pl.bodzioch.damian.configuration.security.UserRoles;

import java.util.List;
import java.util.UUID;

@Builder
@Getter
public class UserModel {

    private UUID id;
    private String username;
    private String password;
    private List<UserRoles> roles;
}
