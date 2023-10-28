package pl.bodzioch.damian.dto.client;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class LoginRequestDTO implements Serializable {

    private String username;
    private String password;
}
