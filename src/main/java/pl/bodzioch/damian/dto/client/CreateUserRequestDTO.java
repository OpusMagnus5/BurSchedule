package pl.bodzioch.damian.dto.client;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@Getter
public class CreateUserRequestDTO implements Serializable {

    @NotEmpty(message = "create.user.username.notEmpty")
    private String username;
    @NotEmpty(message = "create.user.password.notEmpty")
    private String password;
    @NotEmpty(message = "create.user.roles.notEmpty")
    private List<String> roles;
}
