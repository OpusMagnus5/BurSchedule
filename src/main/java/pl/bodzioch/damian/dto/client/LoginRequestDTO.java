package pl.bodzioch.damian.dto.client;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class LoginRequestDTO implements Serializable {

    @NotEmpty(message = "login.username.notEmpty")
    private String username;
    @NotEmpty(message = "login.password.notEmpty")
    private String password;
}
